package io.scalaland.chimney

import internal.derived.TransformerDerive
import internal.TransformerFlag
import dsl.TransformerFDefinition
import scala.compiletime.error

private[chimney] trait ScalaVersionSpecificTransformerF {

  /** Provides [[io.scalaland.chimney.TransformerF]] derived with the default settings.
    *
    * When transformation can't be derived, it results with compilation error.
    *
    * @tparam F
    *   wrapper type constructor
    * @tparam From
    *   type of input value
    * @tparam To
    *   type of output value
    * @return
    *   [[io.scalaland.chimney.TransformerF]] type class definition
    */
  inline given derive[F[+_], From, To](using tfs: TransformerFSupport[F]): TransformerF[F, From, To] =
    TransformerDerive.derived[F, From, To, EmptyTuple, TransformerFlag.DefaultValues *: EmptyTuple](
      TransformerFDefinition(Map.empty, Map.empty)
    )

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
  inline def define[F[+_], From, To] = dsl.defaultDefinition[From, To].enableDefaultValues.lift[F]
}
