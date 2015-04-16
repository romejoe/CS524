package edu.cs524.Tasks

import edu.cs524.Task


class SleeperTask extends Task{
  var SleepTimeout:Long = 0

  override def Init(properties: Map[String, Any]): Unit =
  {
    SleepTimeout = properties.get("Timeout").get.asInstanceOf[Long]
  }

  override def run(): Unit = {
    //do work
    Thread.sleep(SleepTimeout)
  }
}
