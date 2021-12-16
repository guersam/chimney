package io.scalaland.chimney

/** Type class definition that wraps patching behavior.
  *
  * @tparam T type of object to apply patch to
  * @tparam Patch type of patch object
  */
trait Patcher[T, Patch] {
  def patch(obj: T, patch: Patch): T
}

object Patcher extends ScalaVersionSpecificPatcher