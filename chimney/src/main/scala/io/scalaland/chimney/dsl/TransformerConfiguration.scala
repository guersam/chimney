package io.scalaland.chimney.dsl

import io.scalaland.chimney.internal.TransformerFlags

class TransformerConfiguration[Flags <: TransformerFlags]
    extends FlagsDsl[({ type λ[F1 <: TransformerFlags] = TransformerConfiguration[F1] })#λ, Flags]

object TransformerConfiguration {

  implicit val default: TransformerConfiguration[TransformerFlags.Default] =
    new TransformerConfiguration[TransformerFlags.Default]
}
