package io.scalaland.chimney.dsl

import io.scalaland.chimney.internal.TransformerCfg._
import io.scalaland.chimney.internal._

import scala.language.experimental.macros

/** Allows customization of [[io.scalaland.chimney.Transformer]] derivation
  *
  * @tparam From type of input value
  * @tparam To   type of output value
  * @tparam C    type-level encoded config
  */
final class TransformerDefinition[From, To, C <: TransformerCfg, Flags <: TransformerFlags](
    val overrides: Map[String, Any],
    val instances: Map[(String, String), Any]
) extends FlagsDsl[Lambda[`F1 <: TransformerFlags` => TransformerDefinition[From, To, C, F1]], Flags]
  with ScalaVersionSpecificTransformerDefinition[From, To, C, Flags] {

  /** Lifts current transformer definition with provided type constructor `F`.
    *
    * It keeps all the configuration, provided missing values, renames,
    * coproduct instances etc.
    *
    * @tparam F    wrapper type constructor
    * @return [[io.scalaland.chimney.dsl.TransformerFDefinition]]
    */
  def lift[F[+_]]: TransformerFDefinition[F, From, To, WrapperType[F, C], Flags] =
    new TransformerFDefinition[F, From, To, WrapperType[F, C], Flags](overrides, instances)

  /** Used internally by macro. Please don't use in your code.
   */
  def __refineConfig[C1 <: TransformerCfg]: TransformerDefinition[From, To, C1, Flags] =
    this.asInstanceOf[TransformerDefinition[From, To, C1, Flags]]

  /** Used internally by macro. Please don't use in your code.
    */
  def __addOverride(key: String, value: Any): TransformerDefinition[From, To, C, Flags] =
    new TransformerDefinition(overrides.updated(key, value), instances)

  /** Used internally by macro. Please don't use in your code.
    */
  def __addInstance(from: String, to: String, value: Any): TransformerDefinition[From, To, C, Flags] =
    new TransformerDefinition(overrides, instances.updated((from, to), value))

}
