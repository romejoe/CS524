package edu.cs524

class Environment(netLayer:NetworkLayer, master:Master, workers:Set[Worker]) {
  var threads:Set[Thread] = Set()
  var workerThreads:ThreadGroup = new ThreadGroup("Workers")
  var masterThread:Thread = null

  def StartEnvironment()={

    threads = threads ++ workers.map(new Thread(workerThreads,_))
    masterThread = new Thread(master)
    threads += masterThread

    //startThreads
    masterThread.start()
    threads.foreach(t => if(t != masterThread) t.start())

  }

  def StopEnvironment()={
    workers.foreach(_.Exit())
    master.Exit()
    threads.foreach(_.join())
  }

}
