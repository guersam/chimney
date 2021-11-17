package io.scalaland.chimney.dsl

import io.scalaland.chimney.internal.TransformerCfg._
import io.scalaland.chimney.internal._

/** Provides DSL for configuring [[io.scalaland.chimney.Transformer]]'s
  * generation and using the result to transform value at the same time
  *
  * @param  source object to transform
  * @param  td     transformer definition
  * @tparam From   type of input value
  * @tparam To     type of output value
  * @tparam C      type-level encoded config
  */
final class TransformerInto[From, To, C <: TransformerCfg, Flags <: TransformerFlags](
    val source: From,
    val td: TransformerDefinition[From, To, C, Flags]
) extends FlagsDsl[Lambda[`F1 <: TransformerFlags` => TransformerInto[From, To, C, F1]], Flags]
     with TransformerIntoVersionSpecific[From, To, C, Flags] {

  /** Lifts current transformation with provided type constructor `F`.
    *
    * It keeps all the configuration, provided missing values, renames,
    * coproduct instances etc.
    *
    * @tparam F    wrapper type constructor
    * @return [[io.scalaland.chimney.dsl.TransformerFInto]]
    */
  def lift[F[+_]]: TransformerFInto[F, From, To, WrapperType[F, C], Flags] =
    new TransformerFInto[F, From, To, WrapperType[F, C], Flags](source, td.lift[F])


  /** Used internally by macro. Please don't use in your code.
    */
  def __refineTransformerDefinition[C1 <: TransformerCfg](
      f: TransformerDefinition[From, To, C, Flags] => TransformerDefinition[From, To, C1, Flags]
  ): TransformerInto[From, To, C1, Flags] =
    new TransformerInto[From, To, C1, Flags](source, f(td))

}
