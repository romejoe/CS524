package edu.cs524


trait Worker extends NetworkNode{
  var id = ""
  var props:Map[String, Any] = Map()

  def setID(s: String) = id = s

  def setProperties(map: Map[String, Any]) = props = map

}
