package io.scalaland.chimney.internal.derived

import scala.deriving._
import scala.compiletime._
import io.scalaland.chimney._
import io.scalaland.chimney.internal.utils.MacroUtils

object PatcherDerive:
  import DeriveUtils.Concat

  inline def derived[T, P, Config <: Tuple, Path <: String]: Patcher[T, P] =
    summonFrom {
      case tm: Mirror.ProductOf[T] =>
        summonFrom {
          case pm: Mirror.ProductOf[P] =>
            PatcherDeriveProduct.deriveProduct[T, P, Config, Path](using tm, pm)
          case _ =>
            error(constValue["Requested patcher combination not supported, at: " Concat Path])
        }
      case tm: Mirror.SumOf[T] =>
        summonFrom {
          case pm: Mirror.SumOf[P] =>
            PatcherDeriveCoproduct.deriveCoproduct[T, P, Config, Path](using tm, pm)
          case _ =>
            error(constValue["Requested patcher combination not supported, at: " Concat Path])
        }
      case _ =>
        error(constValue["Requested patcher combination not supported, at: " Concat Path])
    }

  inline def derivedN[T, P <: Tuple, Config <: Tuple]: Patcher[T, P] =
    summonFrom {
      case tm: Mirror.ProductOf[T] =>
        PatcherDeriveProduct.deriveProductN[T, P, Config](using tm)
      case tm: Mirror.SumOf[T] =>
        error(constValue["Requested patcherN combination not supported"])
      case _ =>
        error(constValue["Requested patcherN combination not supported"])
    }

end PatcherDerive
