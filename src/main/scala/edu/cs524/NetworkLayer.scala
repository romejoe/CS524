package edu.cs524

import edu.cs524.EventLogger.EventType

import scala.util.Random

trait NetworkLayer {
  var MaxLatency = 100
  //def RegisterNode(node:NetworkNode)
  def PerformRPC(srcID:String,node:NetworkNode, rpc:(NetworkNode)=>Any):Any = {

    EventLogger.EventLogger.StartEvent(EventType.NETWORK,srcID)

    val latency = Random.nextInt(MaxLatency)
    Thread.sleep(latency)
    val result = rpc(node)
    Thread.sleep(latency)

    EventLogger.EventLogger.EndEvent(EventType.NETWORK,srcID)

    result
  }
}
