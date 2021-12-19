package io.scalaland.chimney

import internal.derived.TransformerDerive
import internal.TransformerFlag
import dsl.*

import scala.compiletime.error

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
  inline given derive[From, To]: Transformer[From, To] =
    TransformerDerive.derived[From, To, EmptyTuple, TransformerFlag.DefaultValues *: EmptyTuple](
      TransformerDefinition(Map.empty, Map.empty)
    )

  /** Creates an empty [[io.scalaland.chimney.dsl.TransformerDefinition]] that
    * you can customize to derive [[io.scalaland.chimney.Transformer]].
    *
    * @see [[io.scalaland.chimney.dsl.TransformerDefinition]] for available settings
    *
    * @tparam From type of input value
    * @tparam To type of output value
    * @return [[io.scalaland.chimney.dsl.TransformerDefinition]] with defaults
    */
  inline def define[From, To] = dsl.defaultDefinition[From, To].enableDefaultValues

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
  inline def defineF[F[+_], From, To] = dsl.defaultDefinition[From, To].enableDefaultValues.lift[F]
}
