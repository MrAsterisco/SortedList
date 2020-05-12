@file:Suppress("unused")

package io.github.mrasterisco.sortedlist

fun <T : Comparable<T>> sortedMutableListOf(vararg elements: T): SortedMutableList<T> {
    val comparator = Comparator<T> { p0, p1 -> p0.compareTo(p1) }
    return BinaryTreeMutableList(comparator, *elements)
}

fun <T : Comparable<T>> sortedListOf(vararg elements: T): SortedList<T> {
    val comparator = Comparator<T> { p0, p1 -> p0.compareTo(p1) }
    return BinaryTreeMutableList(comparator, *elements)
}

fun <T> sortedMutableListOf(comparator: Comparator<T>, vararg elements: T): SortedMutableList<T> {
    return BinaryTreeMutableList(comparator, *elements)
}

fun <T> sortedListOf(comparator: Comparator<T>, vararg elements: T): SortedList<T> {
    return BinaryTreeMutableList(comparator, *elements)
}

inline fun <reified T : Comparable<T>> List<T>.toSortedList(): SortedList<T> {
    return sortedListOf(*this.toTypedArray())
}

inline fun <reified T: Comparable<T>> List<T>.toSortedMutableList(): SortedMutableList<T> {
    return sortedMutableListOf(*this.toTypedArray())
}