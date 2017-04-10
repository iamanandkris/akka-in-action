package aia.persistence
//<start id="persistence-shopper"/>

import aia.persistence.sharded.MyShardTest
import aia.persistence.sharded.MyShardTest.Account
import akka.actor._

object Shopper {
  def props(shopperId: Long,actor:ActorRef = Actor.noSender) = Props(new Shopper(actor))
  def name(shopperId: Long) = shopperId.toString

  trait Command {
    def shopperId: Long //<co id="shopper_command"/>
  }

  case class PayBasket(shopperId: Long) extends Command
  // for simplicity every shopper got 40k to spend.
  val cash = 40000
}

class Shopper(actor:ActorRef) extends Actor {
  import Shopper._

  def shopperId = self.path.name.toLong

  val basket = context.actorOf(Basket.props,
    Basket.name(shopperId))


  val wallet = context.actorOf(Wallet.props(shopperId, cash),
    Wallet.name(shopperId))

  def receive = {
    case cmd: Basket.Command => {
      cmd match {
        case Basket.GetItems(id) => actor ! MyShardTest.Create(Account("CHS", "TestOwner", "Root", List("Nathan", "Anand")),"test")
        case _ =>
      }
      basket forward cmd
    }
    case cmd: Wallet.Command => wallet forward cmd

    case PayBasket(shopperId) => basket ! Basket.GetItems(shopperId)
    case Items(list) => wallet ! Wallet.Pay(list, shopperId)
    case Wallet.Paid(_, shopperId) => basket ! Basket.Clear(shopperId)
  }
}
//<end id="persistence-shopper"/>


// alternative PayBasket handling:
// issue: ask timeout
// benefit: can report back to sender of final result.
//
// case PayBasket(shopperId) =>
//   import scala.concurrent.duration._
//   import context.dispatcher
//   import akka.pattern.ask
//   implicit val timeout = akka.util.Timeout(10 seconds)
//   for {
//     items <- basket.ask(Basket.GetItems(shopperId)).mapTo[Items]
//     paid <- wallet.ask(Wallet.Pay(items.list, shopperId)).mapTo[Wallet.Paid]
//   } yield {
//     basket ! Basket.Clear(shopperId)
//   }
