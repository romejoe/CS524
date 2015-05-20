package Experiments

import edu.cs524.Builders.{EnvironmentBuilder, JobBuilder}
import edu.cs524.EventLogger.EventLogger
import edu.cs524.MasterImpl.BulkRoundRobinMaster
import edu.cs524.NetworkImpl.SimpleNet
import edu.cs524.Tasks.SleeperTask
import edu.cs524.WorkerImpl.{GreedyWorkStealerWorker, WorkStealerWorker}
import edu.cs524._

object MasterExperiment {
  def runExperiment(ExperimentName:String, taskCount:Int, workerCount:Int, WorkerClass:Class[_ <: Worker], MasterClass:Class[_ <: Master], NetClass:Class[_ <: NetworkLayer], NeighborAssigner:Set[Worker]=>Unit = null)={
    EventLogger.Reset()
    println(ExperimentName)

    var canProceed:Boolean = false
    val jobBuilder = new JobBuilder

    for(i <- 1 to taskCount){
      jobBuilder.CreateTask(classOf[SleeperTask])
        .SetTaskProperty("Timeout", 1000L)

    }

    jobBuilder.SetJobCallback(()=>canProceed = true)
    val job:Job = jobBuilder.Build()

    val envBuilder = (new EnvironmentBuilder)
      .SetMaster(MasterClass)
      .SetNetworkLayer(NetClass)

    if(NeighborAssigner != null) {
      envBuilder.SetNeighborAssigner(NeighborAssigner)
    }

    for(i <- 1 to workerCount){
      envBuilder.CreateWorker(WorkerClass)
    }

    val env:Environment = envBuilder.Build()

    env.StartEnvironment()
    env.SubmitJob(job)


    while(!canProceed){Thread.sleep(15)}

    env.StopEnvironment()
    EventLogger.CollectResults()

  }

  def main(args: Array[String]) {
    val RingAssigner = (workers: Set[Worker]) => {
      var start: Worker = null
      var end: Worker = null
      workers.sliding(2).foreach(p => {
        val a: Worker = p.head
        val b: Worker = p.last
        a.SetNeighborNodes(Set() + b)
        if (start == null) start = a
        end = b
      })
      end.SetNeighborNodes(Set() + start)
    }
    //runExperiment("BRRa/Simple Worker", 1000, 100, classOf[SimpleWorker], classOf[BulkRoundRobinMaster], classOf[SimpleNet])

    //runExperiment("BRRa/Lazy Work Stealer/all", 1000, 100, classOf[WorkStealerWorker], classOf[BulkRoundRobinMaster], classOf[SimpleNet])
    //runExperiment("BRRs/Lazy Work Stealer/all", 1000, 100, classOf[WorkStealerWorker], classOf[BulkRoundRobinSubsetMaster], classOf[SimpleNet])

    runExperiment("BRRa/Lazy Work Stealer/ring", 1000, 100, classOf[WorkStealerWorker], classOf[BulkRoundRobinMaster], classOf[SimpleNet], RingAssigner)
    //runExperiment("BRRs/Lazy Work Stealer/ring", 1000, 100, classOf[WorkStealerWorker], classOf[BulkRoundRobinSubsetMaster], classOf[SimpleNet], RingAssigner)

    //runExperiment("BRRa/Greedy Work Stealer/all", 1000, 100, classOf[GreedyWorkStealerWorker], classOf[BulkRoundRobinMaster], classOf[SimpleNet])
    //runExperiment("BRRs/Greedy Work Stealer/all", 1000, 100, classOf[GreedyWorkStealerWorker], classOf[BulkRoundRobinSubsetMaster], classOf[SimpleNet])

    runExperiment("BRRa/Greedy Work Stealer/ring", 1000, 100, classOf[GreedyWorkStealerWorker], classOf[BulkRoundRobinMaster], classOf[SimpleNet], RingAssigner)
    //runExperiment("BRRs/Greedy Work Stealer/ring", 1000, 100, classOf[GreedyWorkStealerWorker], classOf[BulkRoundRobinSubsetMaster], classOf[SimpleNet], RingAssigner)

    //rerun to verify warmup
    //runExperiment("BRRa/Simple Worker", 1000, 100, classOf[SimpleWorker], classOf[BulkRoundRobinMaster], classOf[SimpleNet])
  }
  /**/

}
