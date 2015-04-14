package edu.cs524

import scala.util.Random
import java.lang.System.currentTimeMillis
import edu.cs524.Node
import edu.cs524.EventLogger.{EventType, EventLogger}

object Network {
  var MaxLatency = 500

  def receiveDelayTransmit(source: Node, dest: Node) : Node = {
    var latency = Random.nextInt(MaxLatency)
    var timeToAllowThrough = currentTimeMillis() + latency
    var Id = latency.toString + timeToAllowThroughput.toString

    EventLogger.StartEvent(EventType.NETWORK, Id)

    do{
    }while(currentTimeMillis() < timeToAllowThroughput)

    EventLogger.EndEvent(EventType.NETWORK, Id)

    dest
  }
}