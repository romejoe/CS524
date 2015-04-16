package edu.cs524


import edu.cs524.EventLogger.EventLogger._
import edu.cs524.EventLogger.EventType


trait NetworkNode extends Runnable{


  var Halt = false
  var netLayer:NetworkLayer = null
  var ID:String = null
  var Neighbors:Set[_ <: NetworkNode] = null

  def getMainSleepInterval():Long = 100

  def ShouldDoWork(): Boolean = true

  override def run() = {
    var i = 0
    StartEvent(EventType.OVERHEAD, getID())
    Setup()
    while(!Halt) {
      //StartEvent(EventType.OVERHEAD, getID())
      val shouldWork = ShouldDoWork()
      EndEvent(EventType.OVERHEAD, getID())
      if(shouldWork) {

        StartEvent(EventType.WORK, getID())
        try {
          Main()
        } catch {
          case e: Exception =>
        }
        EndEvent(EventType.WORK, getID())
      }

      StartEvent(EventType.IDLE, getID())
      Thread.sleep(getMainSleepInterval())
      EndEvent(EventType.IDLE, getID())
      StartEvent(EventType.OVERHEAD, getID())
      i += 1
    }
    Cleanup()
    EndEvent(EventType.OVERHEAD, getID())
  }

  def Exit() ={
    Halt = true
    //break thread sleep
  }


  def SetNeighborNodes(nodes:Set[_ <: NetworkNode])={
    Neighbors = nodes
  }

  def Setup() = {}
  def Main()
  def Cleanup() = {}

  def setID(newID:String) = ID = newID
  def getID():String = ID
}
