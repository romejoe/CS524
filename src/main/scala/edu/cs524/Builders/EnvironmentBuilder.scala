package edu.cs524.Builders

import edu.cs524.{Environment, Master, NetworkLayer, Worker}

class EnvironmentBuilder {
  val NetworkLayerID = "NetworkLayer"
  val MasterID = "Master"

  var envProperties: Map[String, Any] = Map()
  var masterProperties: Map[String, Any] = Map()
  var workerProperties: Map[String, Map[String, Any]] = Map()
  var currentWorkerID: String = ""

  def SetNetworkLayer(layer: Class[_ <: NetworkLayer]): EnvironmentBuilder = {
    envProperties += (NetworkLayerID -> layer)
    this
  }

  def SetMaster(master: Class[_ <: Master]): EnvironmentBuilder = {
    masterProperties += ("Type" -> master)
    this
  }

  def CreateWorker(worker: Class[_ <: Worker], id: String = "W:" + workerProperties.size): EnvironmentBuilder = {
    workerProperties += (id -> Map("Type" -> worker, "ID" -> id))
    currentWorkerID = id
    this
  }

  def SelectWorker(id: String) = {
    if (workerProperties.contains(id)) currentWorkerID = id

    this
  }

  def SetWorkerProperty(k: String, v: Any): EnvironmentBuilder = {
    workerProperties = workerProperties.updated(currentWorkerID,
      workerProperties.get(currentWorkerID).get + (k -> v)
    )
    this
  }

  def SetMasterProperty(k: String, v: Any): EnvironmentBuilder = {
    masterProperties += (k -> v)
    this
  }

  def Build() = {
    val NetLayer: NetworkLayer = envProperties.get(NetworkLayerID).get
      .asInstanceOf[Class[_ <: NetworkLayer]].newInstance()

    val master: Master = masterProperties.get(MasterID).get
      .asInstanceOf[Class[_ <: Master]].newInstance()
    val workers: Set[Worker] = Set() ++ workerProperties.map(
    { case (id, props) => {
      val worker: Worker = props.get("Type")
        .get.asInstanceOf[Class[_ <: Worker]].newInstance()
      worker.setID(id)
      worker.setProperties(props)
      worker
    }
    })

    NetLayer.RegisterNode(master)
    workers.foreach(NetLayer.RegisterNode(_))

    new Environment(NetLayer,master, workers)

  }

}
