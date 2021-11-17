package io.scalaland.chimney

import io.scalaland.chimney.dsl.TransformerFDefinition
import io.scalaland.chimney.internal.{TransformerCfg, TransformerFlags}

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

object TransformerF extends TransformerFVersionSpecific {

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
  def define[F[+_], From, To]
      : TransformerFDefinition[F, From, To, TransformerCfg.WrapperType[F, TransformerCfg.Empty], TransformerFlags.Default] =
    new TransformerFDefinition(Map.empty, Map.empty)

}
