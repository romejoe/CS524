package edu.cs524.EventLogger

object EventType extends Enumeration{
  type EventType = Value

  val JOB = Value("JOB")
  val WORK = Value("WORK")
  val IDLE = Value("IDLE")
  val NETWORK = Value("NETWORK")
  val OVERHEAD = Value("OVERHEAD")

}
