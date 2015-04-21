package edu.cs524.MasterImpl

import edu.cs524.{NetworkNode, Worker, Task, Master}

/**
 * Created by jstanton on 4/20/15.
 */
class BulkRoundRobinMaster extends Master{
  override def ShouldDoWork(): Boolean = !(currentJob == null || isJobRunning)

  def GetWorkersToTask():Set[_ <: NetworkNode]={
    Neighbors
  }

  override def Main(): Unit = {
    if(currentJob == null || isJobRunning) return
    //else do schedule

    val tasks:Iterable[Task] = currentJob.GetTasks().values

    val Workers = GetWorkersToTask()

    val WindowSize = Workers.size
    val TaskSlider = tasks.sliding(WindowSize, WindowSize)

    val workerIterator: Iterator[Worker] = Workers.iterator.asInstanceOf[Iterator[Worker]]

    while(workerIterator.hasNext && TaskSlider.hasNext){
      val worker = workerIterator.next()
      netLayer.PerformRPC(getID(), worker, _.asInstanceOf[Worker].SubmitTasks(TaskSlider.next().toSeq))
    }
    isJobRunning = true
  }
}
