package edu.cs524.WorkerImpl

import edu.cs524.{Master, Worker}

class SimpleWorker extends Worker{

  override def getMainSleepInterval(): Long = 15

  override def Main(): Unit = {
    //do {
      val task = TaskQueue.poll()
      if (task == null) return

      task.run()
      netLayer.PerformRPC(getID(), master, _.asInstanceOf[Master].CompleteTask(task.getID()))
    //}while(true)
  }
}
