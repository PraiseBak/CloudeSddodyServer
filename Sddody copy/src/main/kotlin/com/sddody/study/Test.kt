//package com.sddody.study
//
//
//@DslMarker
//annotation class PersonDsl
//
//@PersonDsl
//class Person{
//    var name: String?= null
//    var age: Int? = null
//    var address: Address? = null
//
//
//}
//
//fun Person.address(init: Address.() -> Unit){
//    val address = Address()
//    init(address)
//    this.address = address
//}
//
//fun person(init : Person.() -> Unit): Person{
//    val person = Person()
//    init(person)
//    return person
//}
//
//
//@PersonDsl
//class Address{
//    var mandatory: String? = null
//    var detail: String? = null
//    var postalCode: String? = null
//
//}
//
//fun main() {
//    val p : Person ?= Person()
//    val person : Person = person{
//        name = "김스타"
//        age = 29
//        address{
//            mandatory = "서울시 ㅇㅇ"
//            detail = "707"
//            postalCode = "060"
//        }
//    }
//}