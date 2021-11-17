package io.scalaland.chimney.dsl

import io.scalaland.chimney.internal._

/** Allows customization of [[io.scalaland.chimney.TransformerF]] derivation
  *
  * @tparam F    wrapper type constructor
  * @tparam From type of input value
  * @tparam To   type of output value
  * @tparam C    type-level encoded config
  */
final class TransformerFDefinition[F[+_], From, To, C <: TransformerCfg, Flags <: TransformerFlags](
    val overrides: Map[String, Any],
    val instances: Map[(String, String), Any]
) extends FlagsDsl[Lambda[`F1 <: TransformerFlags` => TransformerFDefinition[F, From, To, C, F1]], Flags]
    with TransformerFDefinitionVersionSpecific [F, From, To, C, Flags] {


  /** Used internally by macro. Please don't use in your code.
    */
  def __refineConfig[C1 <: TransformerCfg]: TransformerFDefinition[F, From, To, C1, Flags] =
    this.asInstanceOf[TransformerFDefinition[F, From, To, C1, Flags]]

  /** Used internally by macro. Please don't use in your code.
    */
  def __addOverride(key: String, value: Any): TransformerFDefinition[F, From, To, C, Flags] =
    new TransformerFDefinition(overrides.updated(key, value), instances)

  /** Used internally by macro. Please don't use in your code.
    */
  def __addInstance(from: String, to: String, value: Any): TransformerFDefinition[F, From, To, C, Flags] =
    new TransformerFDefinition(overrides, instances.updated((from, to), value))
}
