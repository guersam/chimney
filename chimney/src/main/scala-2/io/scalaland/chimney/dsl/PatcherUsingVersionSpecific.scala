package io.scalaland.chimney.dsl

import io.scalaland.chimney.internal.PatcherCfg
import io.scalaland.chimney.internal.macros.dsl.PatcherBlackboxMacros

import scala.language.experimental.macros

private[dsl] trait PatcherUsingVersionSpecific[T, P, C <: PatcherCfg] { this: PatcherUsing[T, P, C] =>

  /** Applies configured patching in-place
    *
    * @return patched value
    */
  def patch: T = macro PatcherBlackboxMacros.patchImpl[T, P, C]
}
