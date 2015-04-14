package edu.cs524

trait Task extends Runnable with Serializable{
  var Id:String = null

  def setID(s: String) = Id = s
  def getID():String = Id

  def Init(properties:Map[String, Any])
}
