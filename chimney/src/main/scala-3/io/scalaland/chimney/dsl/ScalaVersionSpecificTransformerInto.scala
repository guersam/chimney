package io.scalaland.chimney.dsl

import io.scalaland.chimney.internal.{TransformerCfg, TransformerFlags}

import scala.language.experimental.macros

private[dsl] trait ScalaVersionSpecificTransformerInto[From, To, C <: TransformerCfg, Flags <: TransformerFlags] {

  /** Use `value` provided here for field picked using `selector`.
   *
   * By default if `From` is missing field picked by `selector` compilation fails.
   *
   * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
   * @return [[io.scalaland.chimney.dsl.TransformerInto]]
   */
  def withFieldConst[T, U](selector: To => T, value: U): TransformerInto[From, To, _, Flags] =
    ??? // macro TransformerIntoWhiteboxMacros.withFieldConstImpl

  /** Use wrapped `value` provided here for field picked using `selector`.
   *
   * By default if `From` is missing field picked by `selector` compilation fails.
   *
   * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
   * @param selector target field in `To`, defined like `_.name`
   * @param value    constant value to use for the target field
   * @return [[io.scalaland.chimney.dsl.TransformerFInto]]
   */
  def withFieldConstF[F[+_], T, U](
                                    selector: To => T,
                                    value: F[U]
                                  ): TransformerFInto[F, From, To, _, Flags] =
    ??? // macro TransformerIntoWhiteboxMacros.withFieldConstFImpl[F]

  /** Use `map` provided here to compute value of field picked using `selector`.
   *
   * By default if `From` is missing field picked by `selector` compilation fails.
   *
   * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
   * @param selector target field in `To`, defined like `_.name`
   * @param map      function used to compute value of the target field
   * @return [[io.scalaland.chimney.dsl.TransformerInto]]
   * */
  def withFieldComputed[T, U](
                               selector: To => T,
                               map: From => U
                             ): TransformerInto[From, To, _, Flags] =
    ??? // macro TransformerIntoWhiteboxMacros.withFieldComputedImpl

  /** Use `map` provided here to compute wrapped value of field picked using `selector`.
   *
   * By default if `From` is missing field picked by `selector` compilation fails.
   *
   * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
   * @param selector target field in `To`, defined like `_.name`
   * @param map      function used to compute value of the target field
   * @return [[io.scalaland.chimney.dsl.TransformerFInto]]
   */
  def withFieldComputedF[F[+_], T, U](
                                       selector: To => T,
                                       map: From => F[U]
                                     ): TransformerFInto[F, From, To, _, Flags] =
    ??? // macro TransformerIntoWhiteboxMacros.withFieldComputedFImpl[F]

  /** Use `selectorFrom` field in `From` to obtain the value of `selectorTo` field in `To`
   *
   * By default if `From` is missing field picked by `selectorTo` compilation fails.
   *
   * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#fields-renaming]] for more details
   * @param selectorFrom source field in `From`, defined like `_.originalName`
   * @param selectorTo   target field in `To`, defined like `_.newName`
   * @return [[io.scalaland.chimney.dsl.TransformerInto]]
   * */
  def withFieldRenamed[T, U](
                              selectorFrom: From => T,
                              selectorTo: To => U
                            ): TransformerInto[From, To, _, Flags] =
    ??? // macro TransformerIntoWhiteboxMacros.withFieldRenamedImpl

  /** Use `f` to calculate the (missing) coproduct instance when mapping one coproduct into another
   *
   * By default if mapping one coproduct in `From` into another coproduct in `To` derivation
   * expects that coproducts will have matching names of its components, and for every component
   * in `To` field's type there is matching component in `From` type. If some component is missing
   * it will fail.
   *
   * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#transforming-coproducts]] for more details
   * @param f function to calculate values of components that cannot be mapped automatically
   * @return [[io.scalaland.chimney.dsl.TransformerInto]]
   */
  def withCoproductInstance[Inst](f: Inst => To): TransformerInto[From, To, _, Flags] =
    ??? // macro TransformerIntoWhiteboxMacros.withCoproductInstanceImpl

  /** Use `f` to calculate the (missing) wrapped coproduct instance when mapping one coproduct into another
   *
   * By default if mapping one coproduct in `From` into another coproduct in `To` derivation
   * expects that coproducts to have matching names of its components, and for every component
   * in `To` field's type there is matching component in `From` type. If some component is missing
   * it fails compilation unless provided replacement with this operation.
   *
   * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#transforming-coproducts]] for more details
   * @param f function to calculate values of components that cannot be mapped automatically
   * @return [[io.scalaland.chimney.dsl.TransformerFInto]]
   */
  def withCoproductInstanceF[F[+_], Inst](f: Inst => F[To]): TransformerFInto[F, From, To, _, Flags] =
    ??? // macro TransformerIntoWhiteboxMacros.withCoproductInstanceFImpl[F]

  /** Apply configured transformation in-place.
   *
   * It runs macro that tries to derive instance of `Transformer[From, To]`
   * and immediately apply it to captured `source` value.
   * When transformation can't be derived, it results with compilation error.
   *
   * @return transformed value of type `To`
   */
  def transform[ScopeFlags <: TransformerFlags](
                                                 implicit tc: io.scalaland.chimney.dsl.TransformerConfiguration[ScopeFlags]
                                               ): To =
    ??? // macro TransformerBlackboxMacros.transformImpl[From, To, C, Flags, ScopeFlags]
}
