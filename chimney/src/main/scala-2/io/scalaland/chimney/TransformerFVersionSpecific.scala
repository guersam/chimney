package io.scalaland.chimney

import io.scalaland.chimney.internal.macros.dsl.TransformerBlackboxMacros

import scala.language.experimental.macros

private[chimney] trait TransformerFVersionSpecific {

  /** Provides [[io.scalaland.chimney.TransformerF]] derived with the default settings.
    *
    * When transformation can't be derived, it results with compilation error.
    *
    * @tparam F    wrapper type constructor
    * @tparam From type of input value
    * @tparam To   type of output value
    * @return [[io.scalaland.chimney.TransformerF]] type class definition
    */
  implicit def derive[F[+_], From, To](implicit tfs: TransformerFSupport[F]): TransformerF[F, From, To] =
    macro TransformerBlackboxMacros.deriveTransformerFImpl[F, From, To]
}
