package io.scalaland.chimney.dsl

import io.scalaland.chimney.internal.TransformerFlag._
import io.scalaland.chimney.internal.dsl._
import io.scalaland.chimney.internal._
import io.scalaland.chimney.internal.derived.TransformerDerive
import io.scalaland.chimney.Transformer
import io.scalaland.chimney.internal.utils.MacroUtils

import scala.compiletime.error

/** Allows customization of [[io.scalaland.chimney.Transformer]] derivation
  *
  * @tparam From type of input value
  * @tparam To   type of output value
  * @tparam C    type-level encoded config
  */
final class TransformerDefinition[From, To, Config <: Tuple, Flags <: Tuple](
    val overrides: Map[String, Any],
    val instances: Map[(String, String), Any]
) extends FlagsDsl[[FS <: Tuple] =>> TransformerDefinition[From, To, Config, FS], Flags] {

  /** Lifts current transformer definition with provided type constructor `F`.
    *
    * It keeps all the configuration, provided missing values, renames,
    * coproduct instances etc.
    *
    * @tparam F    wrapper type constructor
    * @return [[io.scalaland.chimney.dsl.TransformerFDefinition]]
    */
  inline def lift[F[_]]: TransformerFDefinition[F, From, To, EnableConfig[Config, TransformerCfg.WrapperType[F]], Flags] =
    TransformerFDefinition[F, From, To, EnableConfig[Config, TransformerCfg.WrapperType[F]], Flags](overrides, instances)

  /** Use `value` provided here for field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
    * @param selector target field in `To`, defined like `_.name`
    * @param value    constant value to use for the target field
    * @return [[io.scalaland.chimney.dsl.TransformerDefinition]]
    */
  transparent inline def withFieldConst[T](inline selector: To => T, value: T) = 
    TransformerDefinitionBuilder.withFieldConst[From, To, Config, Flags, T](this)(selector, value)

  /** Use wrapped `value` provided here for field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
    * @param selector target field in `To`, defined like `_.name`
    * @param value    constant value to use for the target field
    * @return [[io.scalaland.chimney.dsl.TransformerFDefinition]]
    */
  transparent inline def withFieldConstF[F[_], T](inline selector: To => T, value: F[T]) = 
    lift[F].withFieldConstF[T](selector, value)

  /** Use `map` provided here to compute value of field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
    * @param selector target field in `To`, defined like `_.name`
    * @param map      function used to compute value of the target field
    * @return [[io.scalaland.chimney.dsl.TransformerDefinition]]
    */
  transparent inline def withFieldComputed[T](inline selector: To => T, map: From => T) = 
    TransformerDefinitionBuilder.withFieldComputed(this)(selector, map)
  
  /** Use `map` provided here to compute value of field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
    * @param selector target field in `To`, defined like `_.name`
    * @param map      function used to compute value of the target field
    * @return [[io.scalaland.chimney.dsl.TransformerFDefinition]]
    */
  transparent inline def withFieldComputedF[F[_], T](inline selector: To => T, map: From => F[T]) = 
    lift[F].withFieldComputedF(selector, map)

  /** Use `selectorFrom` field in `From` to obtain the value of `selectorTo` field in `To`
    *
    * By default if `From` is missing field picked by `selectorTo` compilation fails.
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#fields-renaming]] for more details
    * @param selectorFrom source field in `From`, defined like `_.originalName`
    * @param selectorTo   target field in `To`, defined like `_.newName`
    * @return [[io.scalaland.chimney.dsl.TransformerDefinition]]
    */
  transparent inline def withFieldRenamed[T](inline selectorFrom: From => T, inline selectorTo: To => T) = 
    TransformerDefinitionBuilder.withFieldRelabelled(this)(selectorFrom, selectorTo)

  /** Use `f` to calculate the (missing) coproduct instance when mapping one coproduct into another.
    *
    * By default if mapping one coproduct in `From` into another coproduct in `To` derivation
    * expects that coproducts to have matching names of its components, and for every component
    * in `To` field's type there is matching component in `From` type. If some component is missing
    * it fails compilation unless provided replacement with this operation.
    * This method might require passing specific types explictly like:
    * ```scala
    * definition.withCoproductInstance[CaseA, CaseB](_ => ???)
    * ```
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#transforming-coproducts]] for more details
    * @param f function to calculate values of components that cannot be mapped automatically
    * @return [[io.scalaland.chimney.dsl.TransformerDefinition]]
    */
  inline def withCoproductInstance[FF <: From, TT <: To](f: FF => TT): TransformerDefinition[From, To, EnableConfig[Config, TransformerCfg.CoproductInstance[FF, TT]], Flags] = 
    TransformerDefinitionBuilder.withInstanceComputed(this)(f)

  /** Use `f` to calculate the (missing) coproduct instance when mapping one coproduct into another.
    *
    * By default if mapping one coproduct in `From` into another coproduct in `To` derivation
    * expects that coproducts to have matching names of its components, and for every component
    * in `To` field's type there is matching component in `From` type. If some component is missing
    * it fails compilation unless provided replacement with this operation.
    * This method might require passing specific types explictly like:
    * ```scala
    * definition.withCoproductInstance[CaseA, CaseB](_ => ???)
    * ```
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#transforming-coproducts]] for more details
    * @param f function to calculate values of components that cannot be mapped automatically
    * @return [[io.scalaland.chimney.dsl.TransformerFDefinition]]
    */
  transparent inline def withCoproductInstanceF[F[_], FF <: From, TT <: To](f: FF => F[TT]) = 
    lift[F].withCoproductInstanceF[FF, TT](f)

  /** Build Transformer using current configuration.
    *
    * It runs macro that tries to derive instance of `Transformer[From, To]`.
    * When transformation can't be derived, it results with compilation error.
    *
    * @return [[io.scalaland.chimney.Transformer]] type class instance
    */
  inline def buildTransformer: Transformer[From, To] = 
    TransformerDerive.derived[From, To, Config, Flags](this)

}

def defaultDefinition[From, To]: TransformerDefinition[From, To, EmptyTuple, EmptyTuple] = TransformerDefinition(Map.empty, Map.empty)

def defaultDefinitionWithFlags[From, To, Flags <: Tuple]: TransformerDefinition[From, To, EmptyTuple, Flags] = TransformerDefinition(Map.empty, Map.empty)
