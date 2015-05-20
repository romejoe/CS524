package edu.cs524.WorkerImpl

import edu.cs524.{Task, Worker}

/**
 * Created by jstanton on 4/19/15.
 */
class GreedyWorkStealerWorker extends WorkStealerWorker {
  override def StealWork(worker: Worker): Seq[Task] = {
    val maxAmountToSteal = worker.TaskQueue.size() / 2
    var i = 0
    var Ret:Seq[Task] = Seq.empty[Task]
    while (i < maxAmountToSteal) {
      val task: Task = worker.TaskQueue.poll()
      if (task != null)
        Ret = Ret ++ Seq(task)
      else
        i = maxAmountToSteal

      i += 1
    }
    Ret
  }

}
