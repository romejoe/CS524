package Experiments

import edu.cs524.Builders.JobBuilder
import edu.cs524.Job
import edu.cs524.Tasks._

/**
 * Created by Joey on 4/7/15.
 */
object Experiment1 {
  def main(args: Array[String]) {
    val a:Job = (new JobBuilder)
      .CreateTask(classOf[SleeperTask])

      .SetTaskProperty("Timeout", 10000L)
      .Build()

    a.GetTasks()

  }
}
