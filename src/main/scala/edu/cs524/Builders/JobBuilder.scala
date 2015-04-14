package edu.cs524.Builders

import edu.cs524.{Job, Task}

import scala.collection.mutable

class JobBuilder {

  var currentTask:mutable.Map[String, Any] = null

  val taskConfig:mutable.Map[String, mutable.Map[String, Any]] = new mutable.HashMap
  val jobConfig:mutable.Map[String, Any] = new mutable.HashMap
  var jobCallback: () => Unit = null
  jobConfig.put("Id", "tmp")

  def CreateTask(taskType:Class[_ <: Task], id:String="I"+taskConfig.size):JobBuilder = {
    currentTask = new mutable.HashMap

    currentTask.put("Type", taskType)
    currentTask.put("Id", id)

    taskConfig.put(id, currentTask)

    this
  }

  def SetTaskProperty(id:String, value:Any):JobBuilder = {
    assert(currentTask != null)
    currentTask.put(id, value)

    this
  }

  def SetJobProperty(key:String, value:Any):JobBuilder = {
    jobConfig.put(key, value)

    this
  }

  def SetJobCallback(callback:()=>Unit):JobBuilder={
    jobCallback = callback

    this
  }

  def Build():Job = new Job(
    jobConfig.get("Id").get.asInstanceOf[String],
    /* Convert Mutable.Map[String, Mutable.Map] to Map[String, Map] */
    taskConfig.map({
      case (i:String, m:mutable.Map[String,Any]) => (i, m.toMap)
    }).toMap,
    jobCallback
  )

}
