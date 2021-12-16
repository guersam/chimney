package io.scalaland.chimney.dsl

import io.scalaland.chimney.internal.{TransformerCfg, TransformerFlags}

final class TransformerFInto[F[+_], From, To, C <: TransformerCfg, Flags <: TransformerFlags](
    val source: From,
    val td: TransformerFDefinition[F, From, To, C, Flags]
) extends FlagsDsl[Lambda[`F1 <: TransformerFlags` => TransformerFInto[F, From, To, C, F1]], Flags]
     with ScalaVersionSpecificTransformerFInto[F, From, To, C, Flags] {

  /** Used internally by macro. Please don't use in your code.
    */
  def __refineTransformerDefinition[C1 <: TransformerCfg](
      f: TransformerFDefinition[F, From, To, C, Flags] => TransformerFDefinition[F, From, To, C1, Flags]
  ): TransformerFInto[F, From, To, C1, Flags] =
    new TransformerFInto[F, From, To, C1, Flags](source, f(td))

}
