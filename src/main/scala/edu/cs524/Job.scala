package edu.cs524

import scala.collection.mutable

class Job(Id: String, taskConfigs: Map[String, Map[String, Any]]) {
  var StartTime = 0L
  var EndTime = 0L

  val completedTasks: mutable.Set[String] = new mutable.HashSet[String]
   //= new mutable.HashMap[String, Task]
   val taskMap:Map[String, Task] = taskConfigs.map {
    case (taskId: String, props: Map[String, Any]) => {
      val t: Task = props.get("Type").get.asInstanceOf[Class[_ <: Task]].newInstance()
      t.Init(props)
      t.setID(taskId)
      (taskId, t)
    }
  }


  def Start(): Unit = StartTime = java.lang.System.currentTimeMillis()

  def GetTasks(): Map[String, Task] = taskMap.toMap

  def CompleteTask(id: String) = {
    completedTasks.add(id)
    if (completedTasks.size == taskMap.size) {
      EndTime = java.lang.System.currentTimeMillis()
    }
  }

}
