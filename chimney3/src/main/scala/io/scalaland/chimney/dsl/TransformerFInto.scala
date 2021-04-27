package io.scalaland.chimney.dsl

import io.scalaland.chimney.TransformerFSupport

import scala.compiletime.error

final class TransformerFInto[F[_], From, To, Config <: Tuple, Flags <: Tuple](
  val source: From,
  val definition: TransformerFDefinition[F, From, To, Config, Flags]
) extends FlagsDsl[[FS <: Tuple] =>> TransformerFInto[F, From, To, Config, FS], Flags]:

  /** Use `value` provided here for field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
    * @return [[io.scalaland.chimney.dsl.TransformerFInto]]
    */
  transparent inline def withFieldConst[T](inline selector: To => T, value: T) = 
    withDefinitionF(definition.withFieldConst(selector, value))

  /** Use wrapped `value` provided here for field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
    * @param selector target field in `To`, defined like `_.name`
    * @param value    constant value to use for the target field
    * @return [[io.scalaland.chimney.dsl.TransformerFInto]]
    */
  transparent inline def withFieldConstF[T](inline selector: To => T, value: F[T]) = 
    withDefinitionF(definition.withFieldConstF(selector, value))

  /** Use `map` provided here to compute value of field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
    * @param selector target field in `To`, defined like `_.name`
    * @param map      function used to compute value of the target field
    * @return [[io.scalaland.chimney.dsl.TransformerFInto]]
    */
  transparent inline def withFieldComputed[T](inline selector: To => T, map: From => T) = 
    withDefinitionF(definition.withFieldComputed(selector, map))
  
  /** Use `map` provided here to compute value of field picked using `selector`.
    *
    * By default if `From` is missing field picked by `selector` compilation fails.
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#providing-missing-values]] for more details
    * @param selector target field in `To`, defined like `_.name`
    * @param map      function used to compute value of the target field
    * @return [[io.scalaland.chimney.dsl.TransformerFInto]]
    */
  transparent inline def withFieldComputedF[T](inline selector: To => T, map: From => F[T]) = 
    withDefinitionF(definition.withFieldComputedF(selector, map))

  /** Use `selectorFrom` field in `From` to obtain the value of `selectorTo` field in `To`
    *
    * By default if `From` is missing field picked by `selectorTo` compilation fails.
    *
    * @see [[https://scalalandio.github.io/chimney/transformers/customizing-transformers.html#fields-renaming]] for more details
    * @param selectorFrom source field in `From`, defined like `_.originalName`
    * @param selectorTo   target field in `To`, defined like `_.newName`
    * @return [[io.scalaland.chimney.dsl.TransformerFInto]]
    */
  transparent inline def withFieldRenamed[T](inline selectorFrom: From => T, inline selectorTo: To => T) = 
    withDefinitionF(definition.withFieldRenamed(selectorFrom, selectorTo))

  inline def transform(using TransformerFSupport[F]): F[To] =
    definition.buildTransformer.transform(source)

  transparent inline def withDefinitionF(inline newDefinition: Any) =
    inline newDefinition match
      case definition: TransformerFDefinition[F, From, To, config, flags] =>
        TransformerFInto(source, definition)
      case _ =>
        error("Changing definition failed, should not happen, a bug in library")

end TransformerFInto