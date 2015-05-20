package edu.cs524.WorkerImpl

import edu.cs524.{Master, Task, Worker}
/**
 * Created by jstanton on 4/19/15.
 */
class WorkStealerWorker extends Worker{
  def StealWork(worker: Worker): Seq[Task] = {
    val task: Task = worker.TaskQueue.poll()
    if(task != null){
      //TaskQueue.add(task)
      return Seq(task)
    }
    Seq.empty
  }

  override def ShouldDoWork():Boolean = {
    if(TaskQueue.size() == 0){
      for(worker <- Neighbors){
        val newTasks:Seq[Task] = netLayer.PerformRPC(getID(), worker, (w)=>StealWork(w.asInstanceOf[Worker])).asInstanceOf[Seq[Task]]
        if(newTasks.size > 0)
          newTasks.foreach(TaskQueue.add(_))
          //TaskQueue.addAll(newTasks)
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
