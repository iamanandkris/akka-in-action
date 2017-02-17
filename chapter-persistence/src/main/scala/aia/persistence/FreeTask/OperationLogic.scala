package aia.persistence.FreeTask

import aia.persistence.FreeTask.OperationActor._
import aia.persistence.FreeTask.Operations.ArithOperations.ArithOpFac
import aia.persistence.FreeTask.Operations.ExprFreeState
import aia.persistence.FreeTask.Operations.OutputOperations.OutputOpFac
import cats.free.Free

/**
  * Created by anand on 15/12/16.
  */
object OperationLogic {

  def addAndPrint(x:Int, y:Int)(implicit arith:ArithOpFac[ExprFreeState],outp:OutputOpFac[ExprFreeState]):Free[ExprFreeState, ExecutionOutput]={
    import arith._,outp._
    val k:Free[ExprFreeState, ExecutionOutput] = for {
      sum <- add(x,y)
      op  <- outputConsole(sum)
    }yield Succeeded
    k
  }
  def subtractedAndPrint(x:Int, y:Int)(implicit arith:ArithOpFac[ExprFreeState],outp:OutputOpFac[ExprFreeState]):Free[ExprFreeState, ExecutionOutput]={
    import arith._,outp._
    val k:Free[ExprFreeState, ExecutionOutput] = for {
      sum <- sub(x,y)
      op  <- outputConsole(sum)
    }yield Succeeded
    k
  }
}
