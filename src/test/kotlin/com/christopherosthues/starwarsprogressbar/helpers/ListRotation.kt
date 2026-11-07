package com.christopherosthues.starwarsprogressbar.helpers

fun <E> rotateByOne(list: MutableList<E>, startIndex: Int = 0) {
    list[startIndex] = list[startIndex + 1].also {
        list[startIndex + 1] = list[startIndex + 2].also {
            list[startIndex + 2] = list[startIndex]
        }
    }
}

fun <E> rotateByTwo(list: MutableList<E>, startIndex: Int = 0) {
    list[startIndex] = list[startIndex + 2].also {
        list[startIndex + 1] = list[startIndex].also {
            list[startIndex + 2] = list[startIndex + 1]
        }
    }
}
