package edu.cs524.EventLogger

import java.util.concurrent.{ConcurrentMap, ConcurrentHashMap}

import edu.cs524.EventLogger.EventType._
import org.joda.time.DateTime
import scala.collection.JavaConversions._

/**
 * Created by Joey on 4/9/15.
 */
object EventLogger {
  var CompletedTasks:Seq[(/*ID*/String, EventType, /*StartTime*/Long, /*EndTime*/Long)] = Seq.empty
  var InProgressTasks:ConcurrentMap[(String,EventType), Long]
    = new ConcurrentHashMap[(String, EventType), Long]()

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

  def Reset() = {
    CompletedTasks = Seq.empty
    InProgressTasks = new ConcurrentHashMap[(String, EventType), Long]()
  }

  def CollectResults(DisplayOldFormat:Boolean = false)={
    //group tasks by EventType
    val TimeDeltas:Map[EventType, Seq[Long]] = CompletedTasks.map(t => (t._2, t._4 - t._3 ))
      .groupBy(_._1).mapValues(_.map(_._2))
    val Averages = TimeDeltas.mapValues(a => a.sum.toDouble/a.length)
    if(DisplayOldFormat) {
      println()
      println("Average Times by Event type")
      println("=" * 20)
      Averages.foreach(a => println(a._1.toString + ":\t" + a._2))
      println()
    }

    val Medians = TimeDeltas.mapValues(a => median(a))
    if(DisplayOldFormat) {
      println("Median Times by Event type")
      println("=" * 20)
      Medians.foreach(a => println(a._1.toString + ":\t" + a._2))
      println()
    }

    val Maxs = TimeDeltas.mapValues(a => a.max)
    if(DisplayOldFormat) {
      println("Max Times by Event type")
      println("=" * 20)
      Maxs.foreach(a => println(a._1.toString + ":\t" + a._2))
      println()
    }
    val Mins = TimeDeltas.mapValues(a => a.min)
      if(DisplayOldFormat) {
        println("Min Times by Event type")
        println("=" * 20)
        Mins.foreach(a => println(a._1.toString + ":\t" + a._2))
        println()
      }
    println()
    //build header seq
    //val headers:Seq[String] = Seq("") ++ TimeDeltas.keySet.toSeq.sortBy(_).map(_.toString)
    //val headers:Seq[String] = Seq("") ++ TimeDeltas.keySet.map(a => (a,a.toString))
    //println(headers)
    //val avg:Seq[String] = Seq("Average") ++ Averages.entrySet().toSeq.sortBy(_.getKey).map("%.2f".format(_.getValue.asInstanceOf[Double]))
    val avg = /*Seq("Average") ++*/ Averages.mapValues("%.2f".format(_))
    val med = /*Seq("Median") ++*/ Medians.mapValues(_.toString)
    val max = /*Seq("Max") ++*/ Maxs.mapValues(_.toString)
    val min = /*Seq("Min") ++*/ Mins.mapValues(_.toString)

    var Rows:Seq[Seq[String]] = Seq.empty
    def AppendToRows(Ordering:Seq[EventType], data:Map[EventType, String], label:String)={
      val newRow = Seq(label) ++ Ordering.map(data.getOrDefault(_,"").asInstanceOf[String])
      Rows = Rows ++ Seq(newRow)
    }

    val colOrdering = TimeDeltas.keySet.toSeq
    Rows = Rows ++ Seq(Seq("") ++ colOrdering.map(_.toString))
    AppendToRows(colOrdering, avg, "Average")
    AppendToRows(colOrdering, med, "Median")
    AppendToRows(colOrdering, max, "Max")
    AppendToRows(colOrdering, min, "Min")


    //println(Tabulator.format(Seq(headers, avg, med, max, min)))
    println(Tabulator.format(Rows))
  }

  }