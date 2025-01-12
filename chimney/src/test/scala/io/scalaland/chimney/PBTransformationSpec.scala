//package io.scalaland.chimney
//
//import utest._
//import io.scalaland.chimney.examples.addressbook
//import io.scalaland.chimney.examples.order
//import io.scalaland.chimney.examples.pb
//
//object PBTransformationSpec extends TestSuite {
//
//  import dsl._
//
//  val tests = Tests {
//
//    "transform value classes between their primitive representations" - {
//
//      addressbook.PersonName("John").transformInto[String] ==> "John"
//      addressbook.PersonId(5).transformInto[Int] ==> 5
//      addressbook.Email("john@example.com").transformInto[String] ==> "john@example.com"
//    }
//
//    "not compile if target type is wrong for value class" - {
//
//      compileError(""" addressbook.PersonName("John").transformInto[Int] """)
//        .check(
//          "",
//          "Chimney can't derive transformation from io.scalaland.chimney.examples.addressbook.PersonName to Int"
//        )
//
//      compileError(""" addressbook.PersonId(5).transformInto[String] """)
//        .check(
//          "",
//          "Chimney can't derive transformation from io.scalaland.chimney.examples.addressbook.PersonId to String"
//        )
//
//      compileError(""" addressbook.Email("john@example.com").transformInto[Float] """)
//        .check("", "Chimney can't derive transformation from io.scalaland.chimney.examples.addressbook.Email to Float")
//    }
//
//    "transform enum represented as sealed trait hierarchy" - {
//
//      (addressbook.MOBILE: addressbook.PhoneType)
//        .transformInto[pb.addressbook.PhoneType] ==>
//        pb.addressbook.PhoneType.MOBILE
//
//      (addressbook.HOME: addressbook.PhoneType)
//        .transformInto[pb.addressbook.PhoneType] ==>
//        pb.addressbook.PhoneType.HOME
//
//      (addressbook.WORK: addressbook.PhoneType)
//        .transformInto[pb.addressbook.PhoneType] ==>
//        pb.addressbook.PhoneType.WORK
//    }
//
//    "transform bigger case classes" - {
//
//      "PhoneNumber" - {
//
//        addressbook
//          .PhoneNumber("1234567", addressbook.HOME)
//          .transformInto[pb.addressbook.PhoneNumber] ==>
//          pb.addressbook.PhoneNumber("1234567", pb.addressbook.PhoneType.HOME)
//      }
//
//      "Person" - {
//
//        addressbook
//          .Person(
//            addressbook.PersonName("John"),
//            addressbook.PersonId(123),
//            addressbook.Email("john@example.com"),
//            List(
//              addressbook.PhoneNumber("1234567", addressbook.HOME),
//              addressbook.PhoneNumber("77332233", addressbook.WORK),
//              addressbook.PhoneNumber("88776655", addressbook.MOBILE)
//            )
//          )
//          .transformInto[pb.addressbook.Person] ==>
//          pb.addressbook.Person(
//            "John",
//            123,
//            "john@example.com",
//            Seq(
//              pb.addressbook.PhoneNumber("1234567", pb.addressbook.PhoneType.HOME),
//              pb.addressbook.PhoneNumber("77332233", pb.addressbook.PhoneType.WORK),
//              pb.addressbook.PhoneNumber("88776655", pb.addressbook.PhoneType.MOBILE)
//            )
//          )
//      }
//
//      "AddressBook" - {
//
//        addressbook
//          .AddressBook(
//            List(
//              addressbook.Person(
//                addressbook.PersonName("John"),
//                addressbook.PersonId(123),
//                addressbook.Email("john@example.com"),
//                List(
//                  addressbook.PhoneNumber("1234567", addressbook.HOME),
//                  addressbook.PhoneNumber("77332233", addressbook.WORK),
//                  addressbook.PhoneNumber("88776655", addressbook.MOBILE)
//                )
//              ),
//              addressbook.Person(
//                addressbook.PersonName("Susan"),
//                addressbook.PersonId(321),
//                addressbook.Email("susan@example.com"),
//                List(addressbook.PhoneNumber("200300400", addressbook.MOBILE))
//              )
//            )
//          )
//          .transformInto[pb.addressbook.AddressBook] ==>
//          pb.addressbook.AddressBook(
//            Seq(
//              pb.addressbook.Person(
//                "John",
//                123,
//                "john@example.com",
//                Seq(
//                  pb.addressbook.PhoneNumber("1234567", pb.addressbook.PhoneType.HOME),
//                  pb.addressbook.PhoneNumber("77332233", pb.addressbook.PhoneType.WORK),
//                  pb.addressbook.PhoneNumber("88776655", pb.addressbook.PhoneType.MOBILE)
//                )
//              ),
//              pb.addressbook.Person(
//                "Susan",
//                321,
//                "susan@example.com",
//                Seq(pb.addressbook.PhoneNumber("200300400", pb.addressbook.PhoneType.MOBILE))
//              )
//            )
//          )
//      }
//
//      "Order" - {
//        val domainOrder =
//          order.Order(
//            List(order.OrderLine(order.Item(123, "foo"), 3), order.OrderLine(order.Item(321, "bar"), 1)),
//            order.Customer(123, "John", "Beer", order.Address("street", 1137, "city"))
//          )
//        val pbOrder = pb.order.Order(
//          Seq(
//            pb.order.OrderLine(Option(pb.order.Item(123, "foo")), 3),
//            pb.order.OrderLine(Option(pb.order.Item(321, "bar")), 1)
//          ),
//          Option(pb.order.Customer(123, "John", "Beer", Option(pb.order.Address("street", 1137, "city"))))
//        )
//        domainOrder.into[pb.order.Order].enableUnsafeOption.transform ==> pbOrder
//        pbOrder.into[order.Order].enableUnsafeOption.transform ==> domainOrder
//      }
//    }
//  }
//
//}
