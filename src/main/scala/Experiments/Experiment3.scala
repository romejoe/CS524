package Experiments

import edu.cs524.Builders.{EnvironmentBuilder, JobBuilder}
import edu.cs524.EventLogger.EventLogger
import edu.cs524.MasterImpl.BulkRoundRobinSubsetMaster
import edu.cs524.NetworkImpl.SimpleNet
import edu.cs524.Tasks._
import edu.cs524.WorkerImpl.GreedyWorkStealerWorker
import edu.cs524.{Environment, Job, Worker}

/**
 * Created by Joey on 4/7/15.
 */
object Experiment3 {
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
      .SetMaster(classOf[BulkRoundRobinSubsetMaster])
      .SetNetworkLayer(classOf[SimpleNet])

    envBuilder.SetNeighborAssigner((workers:Set[Worker])=>{
        var start:Worker = null
        var end:Worker = null
        workers.sliding(2).foreach(p => {
          val a:Worker = p.head
          val b:Worker = p.last
          a.SetNeighborNodes(Set() + b)
          if (start == null) start = a
          end = b
        })
        end.SetNeighborNodes(Set() + start)
    })

    for(i <- 1 to 100){
      envBuilder.CreateWorker(classOf[GreedyWorkStealerWorker])
    }

    val env:Environment = envBuilder.Build()

    env.StartEnvironment()
    env.SubmitJob(job)


    while(!canProceed){Thread.sleep(15)}

    env.StopEnvironment()
    EventLogger.CollectResults


  }
}
