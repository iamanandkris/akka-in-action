package aia.persistence.sharded

import aia.persistence._
import aia.persistence.sharded.MyShardTest.Command
import akka.actor._
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}

object ShardedShoppers {
  def props(actor:ActorRef)= Props(new ShardedShoppers(actor))
  def name = "sharded-shoppers"
}

class ShardedShoppers(actor:ActorRef) extends Actor {

  ClusterSharding(context.system).start(
    ShardedShopper.shardName,
    ShardedShopper.props(actor),
    ClusterShardingSettings(context.system),
    ShardedShopper.extractEntityId,
    ShardedShopper.extractShardId
  )

  def shardedShopper = {
    ClusterSharding(context.system).shardRegion(ShardedShopper.shardName)
  }

  def receive = {
    case cmd: Shopper.Command =>
      shardedShopper forward cmd
  }
}
