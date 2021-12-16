package io.scalaland.chimney.internal.macros.dsl

import io.scalaland.chimney.Patcher

import scala.language.experimental.macros

object DSLMacros {

  def derivePatcher[T, Patch]: Patcher[T, Patch] =
    macro PatcherBlackboxMacros.derivePatcherImpl[T, Patch]

}
