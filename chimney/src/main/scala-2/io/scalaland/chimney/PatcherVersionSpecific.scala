package io.scalaland.chimney

import io.scalaland.chimney.internal.macros.dsl.PatcherBlackboxMacros

import scala.language.experimental.macros

private[chimney] trait PatcherVersionSpecific { this: Patcher.type =>

  /** Provides implicit [[io.scalaland.chimney.Patcher]] instance
   * for arbitrary types.
   *
   * @tparam T type of object to apply patch to
   * @tparam Patch type of patch object
   * @return [[io.scalaland.chimney.Patcher]] type class instance
   */
  implicit def derive[T, Patch]: Patcher[T, Patch] = macro PatcherBlackboxMacros.derivePatcherImpl[T, Patch]
}
