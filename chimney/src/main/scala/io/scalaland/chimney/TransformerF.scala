package io.scalaland.chimney

/** Type class expressing partial transformation between
  * source type `From` and target type `To`, wrapping
  * transformation result in type constructor `F`.
  *
  * Useful for validated transformations, where result
  * type is wrapped in Option, Either, Validated, etc...
  *
  * @see [[io.scalaland.chimney.TransformerFSupport]]
  *
  * @tparam F    wrapper type constructor
  * @tparam From type of input value
  * @tparam To   type of output value
  */
trait TransformerF[F[+_], From, To] {
  def transform(src: From): F[To]
}

object TransformerF extends ScalaVersionSpecificTransformerF