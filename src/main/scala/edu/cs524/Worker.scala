package edu.cs524

import java.util.concurrent.ConcurrentLinkedQueue


trait Worker extends NetworkNode{
  var master:Master = null
  var TaskQueue:ConcurrentLinkedQueue[Task] = new ConcurrentLinkedQueue[Task]()
  def SetMasterNode(m: Master) = master = m

  var props:Map[String, Any] = Map()

  def setProperties(map: Map[String, Any]) = props = map

  def SubmitTask(task: Task)={
    TaskQueue.add(task)
  }

}
