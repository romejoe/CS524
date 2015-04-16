package edu.cs524.MasterImpl

import edu.cs524.{Master, Task, Worker}

class RoundRobinMaster extends Master{

  override def Setup() = {}

  override def ShouldDoWork(): Boolean = !(currentJob == null || isJobRunning)

  override def Main(): Unit = {
    if(currentJob == null || isJobRunning) return
    //else do schedule

    val tasks:Iterable[Task] = currentJob.GetTasks().values

    var workerIterator:Iterator[Worker] = Neighbors.iterator.asInstanceOf[Iterator[Worker]]
    val taskIterator = tasks.toIterator

    while(taskIterator.hasNext){
      val t = taskIterator.next()

      if(!workerIterator.hasNext)
        workerIterator = Neighbors.iterator.asInstanceOf[Iterator[Worker]]
      val worker = workerIterator.next()
      netLayer.PerformRPC(getID(), worker, _.asInstanceOf[Worker].SubmitTask(t))
    }
    isJobRunning = true
  }

}
