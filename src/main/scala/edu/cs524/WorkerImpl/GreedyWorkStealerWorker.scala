package edu.cs524.WorkerImpl

import edu.cs524.{Master, Task, Worker}

/**
 * Created by jstanton on 4/19/15.
 */
class GreedyWorkStealerWorker extends WorkStealerWorker {
  override def StealWork(worker: Worker): Boolean = {
    val maxAmountToSteal = worker.TaskQueue.size() / 2
    var i = 0
    while (i < maxAmountToSteal) {
      val task: Task = worker.TaskQueue.peek()
      if (task != null)
        TaskQueue.add(task)
      else
        return i > 0

      i += 1
    }
    i > 0
  }

}
