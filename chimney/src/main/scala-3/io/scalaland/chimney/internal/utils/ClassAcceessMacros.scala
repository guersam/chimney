package io.scalaland.chimney.internal.utils

import scala.quoted.{given, _}
import deriving._, compiletime._

object ClassAcceessMacros:

  transparent inline def selectByName[C](cValue: C, inline name: Any) = ${selectByNameAny('cValue, 'name) }

  private def selectByNameAny[C: Type](cValue: Expr[C], name: Expr[Any])(using q: Quotes): Expr[Any] = 
    import q.reflect.report
    name match
      case '{$n: String} =>
        selectByNameImpl(cValue, n)
      case _ =>
        report.throwError("Failed to extract name it is not a string for some reason")
  end selectByNameAny

  private def selectByNameImpl[C: Type](cValue: Expr[C], name: Expr[String])(using q: Quotes): Expr[Any] =
    import q.reflect._
    name.value match
      case Some(name) =>
        findMethod[C](name) match
          case Some(tpe) =>
            tpe match
              case '[tpe] =>
                val extracted = Select.unique(cValue.asTerm, name).asExpr.asExprOf[tpe]
                '{Some($extracted)}
          case None =>
            '{None}
      case None =>
        report.throwError("Failed to extract name, a bug library")
  end selectByNameImpl

  def selectByNameMacro[C: Type, Field: Type](cValue: Expr[C])(using Quotes): Option[Expr[Any]] =
    import quotes.reflect._

    Type.valueOfConstant[Field] match
      case Some(name: String) =>
        findMethod[C](name) match
          case Some(tpe) =>
            tpe match
              case '[tpe] =>
                val extracted = Select.unique(cValue.asTerm, name).asExpr.asExprOf[tpe]
                Some(extracted)
              case _ =>
                None
          case _ =>
            None
      case _ =>
        report.throwError("Failed to extract name it is not a string for some reason")
  end selectByNameMacro

  private def findMethod[C: Type](name: String)(using q: Quotes): Option[Type[?]] =
    import q.reflect._
    val sym = TypeTree.of[C].symbol
    if sym.isClassDef then
      val methods: List[Symbol] = sym.declaredMethods
      val rawMethod = methods.collectFirst { case s if s.name == name => s }
      rawMethod.map(_.tree) match
        case Some(DefDef(_, _, typeTree, _)) => Some(typeTree.tpe.asType)
        case _ => None
    else None
  end findMethod

end ClassAcceessMacros
