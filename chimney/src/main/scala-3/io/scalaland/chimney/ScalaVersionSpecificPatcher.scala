package io.scalaland.chimney

import io.scalaland.chimney.internal.derived.PatcherDerive

import scala.language.experimental.macros

private[chimney] trait ScalaVersionSpecificPatcher {

  /** Provides implicit [[io.scalaland.chimney.Patcher]] instance
   * for arbitrary types.
   *
   * @tparam T type of object to apply patch to
   * @tparam Patch type of patch object
   * @return [[io.scalaland.chimney.Patcher]] type class instance
   */
  inline def derive[T, Patch]: Patcher[T, Patch] =
    PatcherDerive.derived[T, Patch, EmptyTuple, ""]
}
