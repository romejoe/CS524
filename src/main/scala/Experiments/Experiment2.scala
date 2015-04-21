package Experiments

import edu.cs524.Builders.{EnvironmentBuilder, JobBuilder}
import edu.cs524.EventLogger.EventLogger
import edu.cs524.MasterImpl.{BulkRoundRobinMaster, SimpleRoundRobinMaster}
import edu.cs524.NetworkImpl.SimpleNet
import edu.cs524.Tasks._
import edu.cs524.WorkerImpl.SimpleWorker
import edu.cs524.{Environment, Job}

/**
 * Created by Joey on 4/7/15.
 */
object Experiment2 {
  def main(args: Array[String]) {
    var canProceed:Boolean = false
    val jobBuilder = new JobBuilder
    for(i <- 1 to 100){
      jobBuilder.CreateTask(classOf[SleeperTask])
        .SetTaskProperty("Timeout", 1000L)

    }

    jobBuilder.SetJobCallback(()=>canProceed = true)
    val job:Job = jobBuilder.Build()

    val envBuilder = (new EnvironmentBuilder)
      .SetMaster(classOf[BulkRoundRobinMaster])
      .SetNetworkLayer(classOf[SimpleNet])

    for(i <- 1 to 100){
      envBuilder.CreateWorker(classOf[SimpleWorker])
    }

    val env:Environment = envBuilder.Build()

    env.StartEnvironment()
    env.SubmitJob(job)


    while(!canProceed){Thread.sleep(15)}

    env.StopEnvironment()
    EventLogger.CollectResults


  }
}
