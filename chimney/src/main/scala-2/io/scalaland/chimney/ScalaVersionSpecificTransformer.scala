package io.scalaland.chimney

import io.scalaland.chimney.dsl._
import io.scalaland.chimney.internal._
import io.scalaland.chimney.internal.macros.dsl.TransformerBlackboxMacros

import scala.language.experimental.macros

private[chimney] trait ScalaVersionSpecificTransformer {

  /** Provides [[io.scalaland.chimney.Transformer]] derived with the default settings.
    *
    * When transformation can't be derived, it results with compilation error.
    *
    * @tparam From
    *   type of input value
    * @tparam To
    *   type of output value
    * @return
    *   [[io.scalaland.chimney.Transformer]] type class definition
    */
  implicit def derive[From, To]: Transformer[From, To] =
    macro TransformerBlackboxMacros.deriveTransformerImpl[From, To]

  /** Creates an empty [[io.scalaland.chimney.dsl.TransformerDefinition]] that
    * you can customize to derive [[io.scalaland.chimney.Transformer]].
    *
    * @see [[io.scalaland.chimney.dsl.TransformerDefinition]] for available settings
    *
    * @tparam From type of input value
    * @tparam To type of output value
    * @return [[io.scalaland.chimney.dsl.TransformerDefinition]] with defaults
    */
  def define[From, To]: TransformerDefinition[From, To, TransformerCfg.Empty, TransformerFlags.Default] =
    new TransformerDefinition(Map.empty, Map.empty)

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
  def defineF[F[+_], From, To]
      : TransformerFDefinition[F, From, To, TransformerCfg.WrapperType[F, TransformerCfg.Empty], TransformerFlags.Default] =
    TransformerF.define[F, From, To]
}
