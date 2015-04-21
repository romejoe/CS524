package edu.cs524.MasterImpl

import edu.cs524.NetworkNode

/**
 * Created by jstanton on 4/20/15.
 */
class BulkRoundRobinSubsetMaster extends BulkRoundRobinMaster{
  override def GetWorkersToTask(): Set[_ <: NetworkNode] = {
    Neighbors.zipWithIndex.collect(
      {case (e,i) if ((i+1) %2)== 0 => e}
    )
  }
}
