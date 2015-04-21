package edu.cs524

import edu.cs524.EventLogger.EventType

import scala.collection.mutable

class Job(Id: String, taskConfigs: Map[String, Map[String, Any]], completionCallback:()=>Unit) {

  var taskMap:Map[String, Task] = null

  val completedTasks: mutable.Set[String] = new mutable.HashSet[String]


  taskMap = taskConfigs.map {
    case (taskId: String, props: Map[String, Any]) => {
      val t: Task = props.get("Type").get.asInstanceOf[Class[_ <: Task]].newInstance()
      t.Init(props)
      assert(taskId != null)
      t.setID(taskId)
      (taskId, t)
    }
  }


  def Start() = {
    EventLogger.EventLogger.StartEvent(EventType.JOB, Id)
  }

  def GetTasks(): Map[String, Task] = taskMap

  def CompleteTask(id: String): Boolean = {
    completedTasks.add(id)
    print(".")
    if (completedTasks.size == taskMap.size) {
      EventLogger.EventLogger.EndEvent(EventType.JOB, Id)
      completionCallback()
      return true
    }
    return false
  }

}
