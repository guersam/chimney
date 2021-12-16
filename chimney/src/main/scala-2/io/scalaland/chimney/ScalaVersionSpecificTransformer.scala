package io.scalaland.chimney

import io.scalaland.chimney.internal.macros.dsl.TransformerBlackboxMacros

import scala.language.experimental.macros

private[chimney] trait ScalaVersionSpecificTransformer {

  /** Provides [[io.scalaland.chimney.Transformer]] derived with the default settings.
    *
    * When transformation can't be derived, it results with compilation error.
    *
    * @tparam From
    *   type of input value
    * @tparam To
    *   type of output value
    * @return
    *   [[io.scalaland.chimney.Transformer]] type class definition
    */
  implicit def derive[From, To]: Transformer[From, To] =
    macro TransformerBlackboxMacros.deriveTransformerImpl[From, To]

}
