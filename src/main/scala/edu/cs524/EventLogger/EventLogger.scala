package edu.cs524.EventLogger

import edu.cs524.EventLogger.EventType._
import org.joda.time.DateTime
import scala.collection.JavaConversions._

/**
 * Created by Joey on 4/9/15.
 */
object EventLogger {
  var CompletedTasks:Seq[(/*ID*/String, EventType, /*StartTime*/Long, /*EndTime*/Long)] = Seq.empty
  val InProgressTasks:java.util.concurrent.ConcurrentMap[(String,EventType), Long] 
    = new java.util.concurrent.ConcurrentHashMap[(String, EventType), Long]()

  def StartEvent(event:EventType,ID:String)={
    InProgressTasks.putIfAbsent ((ID, event), DateTime.now.getMillis)
  }

  def EndEvent(event:EventType,ID:String)={

    this.synchronized {
      val EndTime = DateTime.now.getMillis

      val StartTime = InProgressTasks.get(ID, event)
      InProgressTasks.remove((ID, event))

      //CompletedTasks.add((ID, event, StartTime, EndTime))
      CompletedTasks = CompletedTasks :+(ID, event, StartTime, EndTime)
    }

  }

  def median(s: Seq[Long]):Double  =
  {
    val (lower, upper) = s.sortWith(_<_).splitAt(s.size / 2)
    if (s.size % 2 == 0) (lower.last + upper.head) / 2.0 else upper.head
  }
  object Tabulator {
    def format(table: Seq[Seq[Any]]) = table match {
      case Seq() => ""
      case _ =>
        val sizes = for (row <- table) yield (for (cell <- row) yield if (cell == null) 0 else cell.toString.length)
        val colSizes = for (col <- sizes.transpose) yield col.max
        val rows = for (row <- table) yield formatRow(row, colSizes)
        formatRows(rowSeparator(colSizes), rows)
    }

    def formatRows(rowSeparator: String, rows: Seq[String]): String = (
      rowSeparator ::
        rows.head ::
        rowSeparator ::
        rows.tail.toList :::
        rowSeparator ::
        List()).mkString("\n")

    def formatRow(row: Seq[Any], colSizes: Seq[Int]) = {
      val cells = (for ((item, size) <- row.zip(colSizes)) yield if (size == 0) "" else ("%" + size + "s").format(item))
      cells.mkString("|", "|", "|")
    }

    def rowSeparator(colSizes: Seq[Int]) = colSizes map { "-" * _ } mkString("+", "+", "+")
  }

  def getInProgressNetworkEvents():Int 
    = InProgressTasks.keySet().filter(_._2 == NETWORK).size()

  def CollectResults={
    //group tasks by EventType
    val TimeDeltas:Map[EventType, Seq[Long]] = CompletedTasks.map(t => (t._2, t._4 - t._3 ))
      .groupBy(_._1).mapValues(_.map(_._2))
    val Averages = TimeDeltas.mapValues(a => a.sum.toDouble/a.length)
    println()
    println("Average Times by Event type")
    println("="*20)
    Averages.foreach(a => println(a._1.toString + ":\t" + a._2))
    println()

    val Medians = TimeDeltas.mapValues(a => median(a))
    println("Median Times by Event type")
    println("="*20)
    Medians.foreach(a => println(a._1.toString + ":\t" + a._2))
    println()

    val Maxs = TimeDeltas.mapValues(a => a.max)
    println("Max Times by Event type")
    println("="*20)
    Maxs.foreach(a => println(a._1.toString + ":\t" + a._2))
    println()

    val Mins = TimeDeltas.mapValues(a => a.min)
    println("Min Times by Event type")
    println("="*20)
    Mins.foreach(a => println(a._1.toString + ":\t" + a._2))
    println()

    //build header seq
    val headers:Seq[String] = Seq("") ++ TimeDeltas.keySet.map(_.toString)
    val avg:Seq[String] = Seq("Averages") ++ Averages.values.map("%.2f".format(_))
    val med:Seq[String] = Seq("Medians") ++ Medians.values.map(_.toString)
    val max:Seq[String] = Seq("Maxs") ++ Maxs.values.map(_.toString)
    val min:Seq[String] = Seq("Mins") ++ Mins.values.map(_.toString)
    println(Tabulator.format(Seq(headers, avg, med, max, min)))
  }

  }