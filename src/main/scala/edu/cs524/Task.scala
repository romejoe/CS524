package edu.cs524

/**
 * Created by Joey on 4/7/15.
 */
trait Task extends Runnable with Serializable{
  def Init(properties:Map[String, Any])
}
