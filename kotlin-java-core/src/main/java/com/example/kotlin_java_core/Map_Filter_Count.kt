package com.example.kotlin_java_core

fun filter() {
    var list = listOf(1,2,3,4,5,6,7,8)

    println(list.filter{ it%2 == 0})

    println(list.filterNot { it%2==0 })

    println(list.filterIndexed { k,v -> k%2==0})

}


fun map() {
    var list = listOf(1,2,3,4,5)
    val doubled: List<Int> = list.map { it * 2 }
    println(doubled)
    println(list)

    val strList = listOf("Manisha", "sasha", "rasha")
    val strLen = strList.map { it.length }
    println(strLen)

    val indexedStr = strList.mapIndexed { index, value ->
        "$index --> $value"
    }
    println(indexedStr)

    val numbers = listOf(1,2,3,4,5)
    var res = numbers
        .filter { it%2 == 0 }
        .map { it*2 }
    println(res)

    val salaries = mapOf("manisha" to 70, "sasha" to 80)
    val newSal = salaries.mapValues { it.value + 20 }
    val names = salaries.mapKeys { "Miss. " + it.key  }
    println(names + newSal)

    val data: List<String?> = listOf("a", null, "b", "c", null)
    println(data.mapNotNull { it?.uppercase()})

    //combine 2 lists
    val list1 = listOf(1,2,3,4,5)
    val list2 = listOf(11,22,33,44,55)

    println(list1 + list2)

    val zipped = list1.zip(list2)
    println(zipped)

    println(listOf(list1, list2).flatten())

    println(list1.zip(list2) {l1,l2 -> l1 + l2})
}

fun count() {
    val list1 = listOf(1,2,3,4,5,6)
    println(list1.count())

    println(list1.count { it%2 == 0})

    println(list1.sortedDescending())

    val list2 = listOf("manisha", "sasha", "rish")
    println(list2.sortedBy { it.length })
}

fun main() {
   /* filter()
    map()*/
    count()


}