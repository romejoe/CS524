package edu.cs524.EventLogger

import edu.cs524.EventLogger.EventType._
import org.joda.time.DateTime

import scala.collection.mutable

/**
 * Created by Joey on 4/9/15.
 */
object EventLogger {
  var CompletedTasks:Seq[(/*ID*/String, EventType, /*StartTime*/Long, /*EndTime*/Long)] = Seq.empty
  val InProgressTasks:mutable.Map[(String,EventType), Long] = new mutable.HashMap[(String,EventType), Long]

  def StartEvent(event:EventType,ID:String)={
    InProgressTasks.update ((ID, event), DateTime.now.getMillis)
  }

  def EndEvent(event:EventType,ID:String)={
    val EndTime = DateTime.now.getMillis
    val StartTime = InProgressTasks.remove(ID, event).get
    CompletedTasks = CompletedTasks :+ (ID, event, StartTime, EndTime)
  }

  def median(s: Seq[Long]):Double  =
  {
    val (lower, upper) = s.sortWith(_<_).splitAt(s.size / 2)
    if (s.size % 2 == 0) (lower.last + upper.head) / 2.0 else upper.head
  }

  def CollectResults={
    //group tasks by EventType
    val TimeDeltas:Map[EventType, Seq[Long]] = CompletedTasks.map(t => (t._2, t._4 - t._3 )).groupBy(_._1).mapValues(_.map(_._2))
    val Averages = TimeDeltas.mapValues(a => a.sum.toDouble/a.length)
    println("Average Times by Event type")
    println("="*20)
    Averages.foreach(a => println(a._1.toString + ":\t" + a._2))
    println()

    val Medians = TimeDeltas.mapValues(a => median(a))
    println("Median Times by Event type")
    println("="*20)
    Medians.foreach(a => println(a._1.toString + ":\t" + a._2))
    println()



    //TimeDeltas.map(a => a.fold(None,0L)((b,c) => (c._1, b._2 + c._2))).map(a => (a._1, a.))
    //TimeDeltas.mapValues(s => s.fold((EventType, Seq[(EventType, Long)]))((a,b) => (a._1, a._2 + b._2)))
    //TimeDeltas.map({case (e, seq) => (e, seq.aggregate({case (a,b) => a+b)})))
  }

}
