package Experiments

import edu.cs524.Builders.JobBuilder
import edu.cs524.EventLogger.{EventType, EventLogger}
import edu.cs524.Job
import edu.cs524.Tasks._

/**
 * Created by Joey on 4/7/15.
 */
object Experiment1 {
  def main(args: Array[String]) {
    EventLogger.StartEvent(EventType.IDLE, "Ex1")
    val a:Job = (new JobBuilder)
      .CreateTask(classOf[SleeperTask])

      .SetTaskProperty("Timeout", 10000L)
      .Build()

    a.GetTasks()
    EventLogger.EndEvent(EventType.IDLE, "Ex1")

    (1 to 100).foreach(a => {
      EventLogger.StartEvent(EventType.WORK,"Ex1")
      Thread sleep a
      EventLogger.EndEvent(EventType.WORK,"Ex1")
    })
    EventLogger.CollectResults


  }
}
