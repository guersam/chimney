package io.scalaland.chimney.dsl

import io.scalaland.chimney.Transformer
import io.scalaland.chimney.internal.macros.dsl.{TransformerBlackboxMacros, TransformerDefinitionWhiteboxMacros}
import io.scalaland.chimney.internal.{TransformerCfg, TransformerFlags}

import scala.language.experimental.macros

private[dsl] trait ScalaVersionSpecificTransformerDefinition[From, To, C <: TransformerCfg, Flags <: TransformerFlags] {

  /** Use `value` provided here for field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see
    *   [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]]
    *   for more details
    * @param selector
    *   target field in `To`, defined like `_.name`
    * @param value
    *   constant value to use for the target field
    * @return
    *   [[io.scalaland.chimney.dsl.TransformerDefinition]]
    */
  def withFieldConst[T, U](selector: To => T, value: U): TransformerDefinition[From, To, _ <: TransformerCfg, Flags] =
    macro TransformerDefinitionWhiteboxMacros.withFieldConstImpl[From, To, T, U, C]

  /** Use wrapped `value` provided here for field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see
    *   [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]]
    *   for more details
    * @param selector
    *   target field in `To`, defined like `_.name`
    * @param value
    *   constant value to use for the target field
    * @return
    *   [[io.scalaland.chimney.dsl.TransformerFDefinition]]
    */
  def withFieldConstF[F[+_], T, U](
      selector: To => T,
      value: F[U]
  ): TransformerFDefinition[F, From, To, _ <: TransformerCfg, Flags] =
    macro TransformerDefinitionWhiteboxMacros.withFieldConstFImpl[F]

  /** Use `map` provided here to compute value of field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see
    *   [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]]
    *   for more details
    * @param selector
    *   target field in `To`, defined like `_.name`
    * @param map
    *   function used to compute value of the target field
    * @return
    *   [[io.scalaland.chimney.dsl.TransformerDefinition]]
    */
  def withFieldComputed[T, U](
      selector: To => T,
      map: From => U
  ): TransformerDefinition[From, To, _ <: TransformerCfg, Flags] =
    macro TransformerDefinitionWhiteboxMacros.withFieldComputedImpl[From, To, T, U, C]

  /** Use `map` provided here to compute wrapped value of field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see
    *   [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]]
    *   for more details
    * @param selector
    *   target field in `To`, defined like `_.name`
    * @param map
    *   function used to compute value of the target field
    * @return
    *   [[io.scalaland.chimney.dsl.TransformerFDefinition]]
    */
  def withFieldComputedF[F[+_], T, U](
      selector: To => T,
      map: From => F[U]
  ): TransformerFDefinition[F, From, To, _ <: TransformerCfg, Flags] =
    macro TransformerDefinitionWhiteboxMacros.withFieldComputedFImpl[F]

  /** Use `selectorFrom` field in `From` to obtain the value of `selectorTo` field in `To`
    *
    * By default if `From` is missing field picked by `selectorTo` compilation fails.
    *
    * @see
    *   [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#fields-renaming]] for more
    *   details
    * @param selectorFrom
    *   source field in `From`, defined like `_.originalName`
    * @param selectorTo
    *   target field in `To`, defined like `_.newName`
    * @return
    *   [[io.scalaland.chimney.dsl.TransformerDefinition]]
    */
  def withFieldRenamed[T, U](
      selectorFrom: From => T,
      selectorTo: To => U
  ): TransformerDefinition[From, To, _ <: TransformerCfg, Flags] =
    macro TransformerDefinitionWhiteboxMacros.withFieldRenamedImpl[From, To, T, U, C]

  /** Use `f` to calculate the (missing) coproduct instance when mapping one coproduct into another.
    *
    * By default if mapping one coproduct in `From` into another coproduct in `To` derivation expects that coproducts to
    * have matching names of its components, and for every component in `To` field's type there is matching component in
    * `From` type. If some component is missing it fails compilation unless provided replacement with this operation.
    *
    * @see
    *   [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#transforming-coproducts]] for
    *   more details
    * @param f
    *   function to calculate values of components that cannot be mapped automatically
    * @return
    *   [[io.scalaland.chimney.dsl.TransformerDefinition]]
    */
  def withCoproductInstance[Inst](f: Inst => To): TransformerDefinition[From, To, _ <: TransformerCfg, Flags] =
    macro TransformerDefinitionWhiteboxMacros.withCoproductInstanceImpl[From, To, Inst, C]

  /** Use `f` to calculate the (missing) wrapped coproduct instance when mapping one coproduct into another
    *
    * By default if mapping one coproduct in `From` into another coproduct in `To` derivation expects that coproducts to
    * have matching names of its components, and for every component in `To` field's type there is matching component in
    * `From` type. If some component is missing it fails compilation unless provided replacement with this operation.
    *
    * @see
    *   [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#transforming-coproducts]] for
    *   more details
    * @param f
    *   function to calculate values of components that cannot be mapped automatically
    * @return
    *   [[io.scalaland.chimney.dsl.TransformerFDefinition]]
    */
  def withCoproductInstanceF[F[+_], Inst](
      f: Inst => F[To]
  ): TransformerFDefinition[F, From, To, _ <: TransformerCfg, Flags] =
    macro TransformerDefinitionWhiteboxMacros.withCoproductInstanceFImpl[F, From, To, Inst, C]

  /** Build Transformer using current configuration.
    *
    * It runs macro that tries to derive instance of `Transformer[From, To]`. When transformation can't be derived, it
    * results with compilation error.
    *
    * @return
    *   [[io.scalaland.chimney.Transformer]] type class instance
    */
  def buildTransformer[ScopeFlags <: TransformerFlags](implicit
      tc: io.scalaland.chimney.dsl.TransformerConfiguration[ScopeFlags]
  ): Transformer[From, To] =
    macro TransformerBlackboxMacros.buildTransformerImpl[From, To, C, Flags, ScopeFlags]
}
