package io.scalaland.chimney

private[chimney] trait ScalaVersionSpecificTransformerF {

  /** Provides [[io.scalaland.chimney.TransformerF]] derived with the default settings.
    *
    * When transformation can't be derived, it results with compilation error.
    *
    * @tparam F
    *   wrapper type constructor
    * @tparam From
    *   type of input value
    * @tparam To
    *   type of output value
    * @return
    *   [[io.scalaland.chimney.TransformerF]] type class definition
    */
  implicit def derive[F[+_], From, To](implicit tfs: TransformerFSupport[F]): TransformerF[F, From, To] =
    ??? // macro TransformerBlackboxMacros.deriveTransformerFImpl[F, From, To]

  /** Creates an empty [[io.scalaland.chimney.dsl.TransformerFDefinition]] that
    * you can customize to derive [[io.scalaland.chimney.TransformerF]].
    *
    * @see [[io.scalaland.chimney.dsl.TransformerFDefinition]] for available settings
    *
    * @tparam F    wrapper type constructor
    * @tparam From type of input value
    * @tparam To   type of output value
    * @return [[io.scalaland.chimney.dsl.TransformerFDefinition]] with defaults
    */
  def define[F[+_], From, To] = dsl.defaultDefinition[From, To].enableDefaultValues.lift[F]
}
