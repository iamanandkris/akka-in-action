package aia.persistence.sharded

import aia.persistence.sharded
import aia.persistence.sharded.MyShardTest.Command
import akka.actor._
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings, ShardCoordinator}


object ShardedAccountant{
  def props(id:String)= Props(new ShardedAccountant(id))
  def name = "sharded-accontant"
}
class ShardedAccountant(id:String) extends Actor {

  ClusterSharding(context.system).start(
    typeName = MyShardTest.shardName,
    entityProps = MyShardTest.props(id),
    settings = ClusterShardingSettings(context.system),
    extractEntityId = MyShardTest.extractEntityId,
    extractShardId = MyShardTest.extractShardId
  )

  def shardedShopper = {
    ClusterSharding(context.system).shardRegion(MyShardTest.shardName)
  }

  def receive = {
    case cmd: Command =>
      shardedShopper forward cmd
  }
}
