package edu.cs524.WorkerImpl

import edu.cs524.{Master, Task, Worker}

/**
 * Created by jstanton on 4/19/15.
 */
class WorkStealerWorker extends Worker{
  def StealWork(worker: Worker): Boolean = {
    val task: Task = worker.TaskQueue.poll()
    if(task != null){
      TaskQueue.add(task)
      return true
    }
    false
  }

  override def ShouldDoWork():Boolean = {
    if(TaskQueue.size() == 0){
      for(worker <- Neighbors){
        if(netLayer.PerformRPC(getID(), worker, (w)=>StealWork(w.asInstanceOf[Worker])).asInstanceOf[Boolean])
          return true
      }
    }

    TaskQueue.size() > 0
  }
  override def Main(): Unit = {
    do {
      val task = TaskQueue.poll()
      if (task == null) return

      task.run()
      netLayer.PerformRPC(getID(), master, _.asInstanceOf[Master].CompleteTask(task.getID()))
    }while(true)
  }
}
