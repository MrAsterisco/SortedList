package io.github.mrasterisco.sortedlist

import kotlin.math.max

class BinaryTreeIterator<T>(private val tree: BinaryTree<T>) : MutableListIterator<T> {
    private val lifo = mutableListOf<BinaryTree.Node<T>>()
    private var nextElementIndex = 0

    init {
        addNodeAndItsLeftChildren(tree.root)
    }

    private fun addNodeAndItsLeftChildren(right: BinaryTree.Node<T>?) {
        var childNode = right
        while (childNode != null) {
            lifo.add(childNode)
            childNode = childNode.left
        }
    }

    override fun next(): T {
        val ret: T
        val node = lifo.last()
        ret = node.elements[nextElementIndex]
        nextElementIndex++

        if (nextElementIndex >= node.elements.size) {
            nextElementIndex = 0
            goToTheNextNode(node)
        }
        return ret
    }

    private fun goToTheNextNode(node: BinaryTree.Node<T>) {
        lifo.removeAt(lifo.lastIndex)
        addNodeAndItsLeftChildren(node.right)
    }

    override fun hasNext(): Boolean {
        return lifo.isNotEmpty()
    }

    override fun remove() {
        val node = lifo.last()
        tree.remove(node.elements[nextElementIndex])
        goToTheNextNode(node)
    }

    override fun hasPrevious(): Boolean {
        return previousIndex() > 0
    }

    override fun nextIndex(): Int {
        return nextElementIndex
    }

    override fun previous(): T {
        throw UnsupportedOperationException()
    }

    override fun previousIndex(): Int {
        throw UnsupportedOperationException()
    }

    override fun add(element: T) {
        tree.add(element)
    }

    override fun set(element: T) {
        throw UnsupportedOperationException("Changing an item in place may break sorting. Remove the item and add it again.")
    }

}
