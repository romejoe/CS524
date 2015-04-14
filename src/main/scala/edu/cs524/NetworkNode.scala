package edu.cs524

import edu.cs524.EventLogger.EventLogger._
import edu.cs524.EventLogger.EventType



trait NetworkNode extends Runnable{
  var Halt = false
  var netLayer:NetworkLayer = null

  def getMainSleepInterval():Long = 100

  override def run() = {
    StartEvent(EventType.WORK, getID())
    while(!Halt) {
      Main()

      EndEvent(EventType.WORK, getID())
      StartEvent(EventType.IDLE, getID())
      Thread.sleep(getMainSleepInterval())
      EndEvent(EventType.IDLE, getID())
      StartEvent(EventType.WORK, getID())
    }
    EndEvent(EventType.WORK, getID())
  }

  def Exit() ={
    Halt = true
    //break thread sleep
  }

  def Main()

  def getID():String
}
