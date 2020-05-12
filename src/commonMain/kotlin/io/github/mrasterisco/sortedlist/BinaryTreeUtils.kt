package io.github.mrasterisco.sortedlist

operator fun <T> BinaryTree<T>.get(index: Int): T {
    if (index !in 0 until this.size) {
        throw IndexOutOfBoundsException()
    }
    return getFromSubTree(this.root!!, index, 0)
}

private fun <T> getFromSubTree(
    rootNode: BinaryTree.Node<T>,
    index: Int,
    numberOfMinorItemsInParent: Int
): T {
    val minors = numberOfMinorItemsInParent + (rootNode.left?.numberOfElementInSubtree ?: 0)
    val size = rootNode.elements.size
    return when {
        index < minors -> getFromSubTree(rootNode.left!!, index, numberOfMinorItemsInParent)
        minors + size < index + 1 -> getFromSubTree(rootNode.right!!, index, minors + size)
        else -> rootNode.elements[index - minors]
    }
}

fun <T> BinaryTree<T>.contains(element: T): Boolean {
    return isSubtreeContains(this.root, element, this.comparator)
}

private fun <T> isSubtreeContains(rootNode: BinaryTree.Node<T>?, element: T, comparator: Comparator<T>): Boolean {
    return if (rootNode == null) {
        false
    } else {
        val comp = comparator.compare(rootNode.elements.first(), element)
        when {
            comp < 0 -> isSubtreeContains(rootNode.right, element, comparator)
            comp > 0 -> isSubtreeContains(rootNode.left, element, comparator)
            else -> rootNode.elements.contains(element)
        }
    }
}
