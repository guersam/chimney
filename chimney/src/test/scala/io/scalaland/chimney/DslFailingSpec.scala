package io.scalaland.chimney

import io.scalaland.chimney.dsl._
import io.scalaland.chimney.examples._
import utest._

object DslFailingSpec extends TestSuite {
  import DslSpecTemp._

  val tests = Tests {

    "support different set of fields of source and target" - {
      import `support different set of fields of source and target`._

      "field is added to the target" - {

        "use implicit transformer for option when .enableUnsafeOption" - {
          import `use implicit transformer for option when .enableUnsafeOption`._

          implicit val stringToIntTransformer: Transformer[Int, String] = _.toString

          // TODO
          //          "use transformer when .enableUnsafeOption" - {
          //            Foobar(Some(1)).into[Foobar2].enableUnsafeOption.transform ==> Foobar2("1")
          //          }
          //
          //          "use transformer when .disableUnsafeOption adn then .enableUnsafeOption" - {
          //            Foobar(Some(1)).into[Foobar2].disableUnsafeOption.enableUnsafeOption.transform ==> Foobar2("1")
          //          }
        }
      }
    }

    "support value classes" - {

      import VCDomain1._

      "transforming value class to a value" - {

        // TODO
        //        UserName("Batman").transformInto[String] ==> "Batman"
        //        User("100", UserName("abc")).transformInto[UserDTO] ==>
        //          UserDTO("100", "abc")
      }

      "transforming value to a value class" - {

        // TODO
        //        "Batman".transformInto[UserName] ==> UserName("Batman")
        //        UserDTO("100", "abc").transformInto[User] ==>
        //          User("100", UserName("abc"))
      }
    }

    "support common data types" - {
      import `support common data types`._

      "support automatically filling of scala.Unit" - {
        import `support automatically filling of scala.Unit`._
        // TODO
        //        Buzz("a").transformInto[NewBuzz] ==> NewBuzz("a", ())
        //        Buzz("a").transformInto[FooBuzz] ==> FooBuzz(())
        //        NewBuzz("a", null.asInstanceOf[Unit]).transformInto[FooBuzz] ==> FooBuzz(null.asInstanceOf[Unit])

        compileError("""Buzz("a").transformInto[ConflictingFooBuzz]""")
          .check(
            "",
            "value: scala.Unit - can't derive transformation from value: java.lang.String in source type io.scalaland.chimney.DslSpec.Buzz"
          )
      }

      "support scala.util.Either" - {
        //        Left(Foo("a")).transformInto[Either[Bar, Bar]] ==> Left(Bar("a"))
        //        Right(Foo("a")).transformInto[Either[Bar, Bar]] ==> Right(Bar("a"))
        //        Left(Foo("a")).transformInto[Left[Bar, Bar]] ==> Left(Bar("a"))
        //        Right(Foo("a")).transformInto[Right[Bar, Bar]] ==> Right(Bar("a"))
      }
    }

    "support with .enableUnsafeOption" - {
      implicit val stringToIntTransformer: Transformer[Int, String] = _.toString

      "use implicit transformer" - {
        import `use implicit transformer`._

        //        Foobar(Some(1)).into[Foobar2].enableUnsafeOption.transform ==> Foobar2("1")
        //        NestedFoobar(Some(Foobar(Some(1)))).into[NestedFoobar2].enableUnsafeOption.transform ==> NestedFoobar2(
        //          Foobar2("1")
        //        )
      }

      "preserve option to option mapping" - {
        import `preserve option to option mapping`._
        // TODO
        //        Foobar(Some(1), Some("foobar")).into[Foobar2].enableUnsafeOption.transform ==> Foobar2("1", Some("foobar"))
        //        Foobar(Some(1), None).into[Foobar2].enableUnsafeOption.transform ==> Foobar2("1", None)
      }

      "transforming None leads to NoSuchElementException" - {
        import `transforming None leads to NoSuchElementException`._

        // TODO
        //        intercept[NoSuchElementException] {
        //          Foobar(None).into[Foobar2].enableUnsafeOption.transform
        //        }
      }
    }

    "support using method calls to fill values from target type" - {
      import `support using method calls to fill values from target type`._

      "val and lazy vals work" - {
        // TODO
        //        Foobar("param").into[Foobar2].transform ==> Foobar2("param", "valField", "lazyValField")
      }

      "works with rename" - {
        import `works with rename`._

        // TODO
        //        val res = Foobar("param")
        //          .into[FooBar4]
        //          .withFieldRenamed(_.param, _.p)
        //          .withFieldRenamed(_.valField, _.v)
        //          .withFieldRenamed(_.lazyValField, _.lv)
        //          .withFieldRenamed(_.method1, _.m)
        //          .enableMethodAccessors
        //          .transform
        //
        //        res ==> FooBar4(p = "param", v = "valField", lv = "lazyValField", m = "method1")
      }

      "works if transform is configured with .enableMethodAccessors" - {
        // TODO
        //        Foobar("param").into[Foobar3].enableMethodAccessors.transform ==> Foobar3(
        //          param = "param",
        //          valField = "valField",
        //          lazyValField = "lazyValField",
        //          method1 = "method1"
        //        )
      }
    }

    "support sealed hierarchies" - {
      "enum types encoded as sealed hierarchies of case objects" - {
        "transforming flat and deep enum" - {
          // TODO
          //          (colors2.Red: colors2.Color).transformInto[colors3.Color] ==> colors3.Red
          //          (colors2.Green: colors2.Color).transformInto[colors3.Color] ==> colors3.Green
          //          (colors2.Blue: colors2.Color).transformInto[colors3.Color] ==> colors3.Blue
          //          (colors2.Black: colors2.Color).transformInto[colors3.Color] ==> colors3.Black
          //
          //          (colors3.Red: colors3.Color).transformInto[colors2.Color] ==> colors2.Red
          //          (colors3.Green: colors3.Color).transformInto[colors2.Color] ==> colors2.Green
          //          (colors3.Blue: colors3.Color).transformInto[colors2.Color] ==> colors2.Blue
          //          (colors3.Black: colors3.Color).transformInto[colors2.Color] ==> colors2.Black
        }
      }

      "transforming non-isomorphic domains" - {

        def triangleToPolygon(t: shapes1.Triangle): shapes2.Shape =
          shapes2.Polygon(
            List(
              t.p1.transformInto[shapes2.Point],
              t.p2.transformInto[shapes2.Point],
              t.p3.transformInto[shapes2.Point]
            )
          )

        def rectangleToPolygon(r: shapes1.Rectangle): shapes2.Shape =
          shapes2.Polygon(
            List(
              r.p1.transformInto[shapes2.Point],
              shapes2.Point(r.p1.x, r.p2.y),
              r.p2.transformInto[shapes2.Point],
              shapes2.Point(r.p2.x, r.p1.y)
            )
          )

        val triangle: shapes1.Shape =
          shapes1.Triangle(shapes1.Point(0, 0), shapes1.Point(2, 2), shapes1.Point(2, 0))

        val rectangle: shapes1.Shape =
          shapes1.Rectangle(shapes1.Point(0, 0), shapes1.Point(6, 4))

        // TODO
        //        rectangle
        //          .into[shapes2.Shape]
        //          .withCoproductInstance[shapes1.Shape] {
        //            case r: shapes1.Rectangle => rectangleToPolygon(r)
        //            case t: shapes1.Triangle  => triangleToPolygon(t)
        //          }
        //          .transform ==> shapes2.Polygon(
        //          List(shapes2.Point(0, 0), shapes2.Point(0, 4), shapes2.Point(6, 4), shapes2.Point(6, 0))
        //        )
      }

      "transforming flat and deep domains" - {
        // TODO
        //        (shapes3.Triangle(shapes3.Point(2.0, 0.0), shapes3.Point(2.0, 2.0), shapes3.Point(0.0, 0.0)): shapes3.Shape)
        //          .transformInto[shapes4.Shape] ==>
        //          shapes4.Triangle(shapes4.Point(2.0, 0.0), shapes4.Point(2.0, 2.0), shapes4.Point(0.0, 0.0))
        //
        //        (shapes3.Rectangle(shapes3.Point(2.0, 0.0), shapes3.Point(2.0, 2.0)): shapes3.Shape)
        //          .transformInto[shapes4.Shape] ==>
        //          shapes4.Rectangle(shapes4.Point(2.0, 0.0), shapes4.Point(2.0, 2.0))
        //
        //        (shapes4.Triangle(shapes4.Point(2.0, 0.0), shapes4.Point(2.0, 2.0), shapes4.Point(0.0, 0.0)): shapes4.Shape)
        //          .transformInto[shapes3.Shape] ==>
        //          shapes3.Triangle(shapes3.Point(2.0, 0.0), shapes3.Point(2.0, 2.0), shapes3.Point(0.0, 0.0))
        //
        //        (shapes4.Rectangle(shapes4.Point(2.0, 0.0), shapes4.Point(2.0, 2.0)): shapes4.Shape)
        //          .transformInto[shapes3.Shape] ==>
        //          shapes3.Rectangle(shapes3.Point(2.0, 0.0), shapes3.Point(2.0, 2.0))
      }
    }

    "support polymorphic source/target objects and modifiers" - {
      import Poly._

      "automatically fill Unit parameters" - {
        import `automatically fill Unit parameters`._

        type UnitBar = Bar[Unit]
        // TODO
        //        Foo("test").transformInto[UnitBar] ==> Bar("test", ())
        //        Foo("test").transformInto[Bar[Unit]] ==> Bar("test", ())
      }
    }

    "transform from non-case class to case class" - {
      import NonCaseDomain._

      // TODO
      //      "support non-case classes inputs" - {
      //        val source = new ClassSource("test-id", "test-name")
      //        val target = source.transformInto[CaseClassNoFlag]
      //
      //        target.id ==> source.id
      //        target.name ==> source.name
      //      }
      //
      //      "support trait inputs" - {
      //        val source: TraitSource = new TraitSourceImpl("test-id", "test-name")
      //        val target = source.transformInto[CaseClassNoFlag]
      //
      //        target.id ==> source.id
      //        target.name ==> source.name
      //      }
    }

    "transform between case classes and tuples" - {
      import `transform between case classes and tuples`._

      val expected = (0, 3.14, "pi")

      // TODO
      //      Foo(0, 3.14, "pi")
      //        .transformInto[(Int, Double, String)] ==> expected
      //
      //      (0, 3.14, "pi").transformInto[Foo]

      "even recursively" - {

        case class Bar(foo: Foo, baz: Boolean)

        val expected = ((100, 2.71, "e"), false)
        // TODO
        //        Bar(Foo(100, 2.71, "e"), baz = false)
        //          .transformInto[((Int, Double, String), Boolean)] ==> expected
        //
        //        ((100, 2.71, "e"), true).transformInto[Bar] ==>
        //          Bar(Foo(100, 2.71, "e"), baz = true)
      }
    }

    "support macro dependent transformers" - {
      "Option[List[A]] -> List[B]" - {
        import `Option[List[A]] -> List[B]`._

        implicit def optListT[A, B](implicit underlying: Transformer[A, B]): Transformer[Option[List[A]], List[B]] =
          _.toList.flatten.map(underlying.transform)

        // TODO
        //        ClassA(None).transformInto[ClassB] ==> ClassB(Nil)
        //
        //        ClassA(Some(List.empty)).transformInto[ClassB] ==> ClassB(Nil)
        //
        //        ClassA(Some(List(ClassAA("l")))).transformInto[ClassB] ==> ClassB(List(ClassBB("l")))
        //
        //        ClassA(Some(List(ClassAA("l")))).into[ClassC].withFieldConst(_.other, "other").transform ==> ClassC(
        //          List(ClassBB("l")),
        //          "other"
        //        )
        //
        //        implicit val defined: Transformer[ClassA, ClassD] =
        //          Transformer.define[ClassA, ClassD].withFieldConst(_.other, "another").buildTransformer
        //
        //        ClassA(Some(List(ClassAA("l")))).transformInto[ClassD] ==> ClassD(List(ClassBB("l")), "another")
      }
    }

    "support scoped transformer configuration passed implicitly" - {
      import `support scoped transformer configuration passed implicitly`._

      implicit val transformerConfiguration = {
        TransformerConfiguration.default.enableOptionDefaultsToNone.enableMethodAccessors.disableDefaultValues
      }

      "scoped config only" - {
        // TODO
        //        (new Source).transformInto[Target] ==> Target(100, None)
        //        (new Source).into[Target].transform ==> Target(100, None)
      }

      "scoped config overridden by instance flag" - {
        // TODO
        //        (new Source)
        //          .into[Target]
        //          .disableMethodAccessors
        //          .enableDefaultValues
        //          .transform ==> Target(200, Some("foo"))
        //
        //        (new Source)
        //          .into[Target]
        //          .enableDefaultValues
        //          .transform ==> Target(100, Some("foo"))
        //
        //        (new Source)
        //          .into[Target]
        //          .disableOptionDefaultsToNone
        //          .withFieldConst(_.field2, Some("abc"))
        //          .transform ==> Target(100, Some("abc"))
      }
    }
  }
}
