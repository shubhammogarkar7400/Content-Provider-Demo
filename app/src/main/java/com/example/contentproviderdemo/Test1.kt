package com.example.contentproviderdemo

data class Student(val name: String, val age: Int)



fun main() {

    val list = mutableListOf<Student>(
        Student("shubham", 44),
        Student("kunal", 22),
        Student("navin", 66),
        Student("abc", 66),
        Student("vinay", 21),
        Student("piyush", 33),
        Student("ayush", 19),
    )

    list.sortedBy { it.name }.sortedBy { it.age }.forEach { println(it) }
    list.sort



}