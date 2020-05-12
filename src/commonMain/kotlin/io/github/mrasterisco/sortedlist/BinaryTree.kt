@file:Suppress("RedundantVisibilityModifier")

package io.github.mrasterisco.sortedlist

public interface BinaryTree<T>: Iterable<T> {
    val size: Int
    val root: Node<T>?
    val comparator: Comparator<T>
    fun add(element: T): Boolean
    fun remove(element: T): Boolean
    fun clear()

    public interface Node<T> {
        val elements: List<T>
        val numberOfElementInSubtree: Int
        val left: Node<T>?
        val right: Node<T>?
    }
}
