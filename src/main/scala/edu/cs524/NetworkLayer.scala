package edu.cs524

import edu.cs524.EventLogger.EventType

import scala.util.Random

trait NetworkLayer {
  val MaxLatency = 10
  def PerformRPC(srcID:String,node:NetworkNode, rpc:(NetworkNode)=>Any):Any = {

    EventLogger.EventLogger.StartEvent(EventType.NETWORK,srcID)

    Thread.sleep(getLatency())
    val result = rpc(node)
    Thread.sleep(getLatency())

    EventLogger.EventLogger.EndEvent(EventType.NETWORK,srcID)

    result
  }

  def getLatency() : Int = {
    var numberLiveNetworkProcesses = 
      EventLogger.EventLogger.getInProgressNetworkEvents()

    var currentMaxLatency = numberLiveNetworkProcesses * MaxLatency

    return Random.nextInt(currentMaxLatency)
  }
}
