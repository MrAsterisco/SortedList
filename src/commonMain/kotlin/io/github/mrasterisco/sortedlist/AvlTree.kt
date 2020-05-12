package io.github.mrasterisco.sortedlist

import kotlin.math.max

class AvlTree<T>(
    override val comparator: Comparator<T>,
    vararg elements: T
) : BinaryTree<T> {

    private var avlRoot: AvlTreeNode<T>? = null
    override val root: BinaryTree.Node<T>?
        get() = avlRoot

    override val size: Int
        get() = avlRoot?.numberOfElementInSubtree ?: 0

    init {
        for (act in elements.asList()) {
            add(act)
        }
    }

    override fun add(element: T): Boolean {
        avlRoot = insertToSubtree(avlRoot, element)
        return true
    }

    private fun insertToSubtree(rootNode: AvlTreeNode<T>?, element: T): AvlTreeNode<T> {
        val ret: AvlTreeNode<T>
        if (rootNode == null) {
            ret = AvlTreeNode(element)
        } else {
            val comp = comparator.compare(rootNode.listOfElement.first(), element)
            when {
                comp < 0 -> rootNode.rightChild = insertToSubtree(rootNode.rightChild, element)
                comp > 0 -> rootNode.leftChild = insertToSubtree(rootNode.leftChild, element)
                else -> rootNode.listOfElement.add(element)
            }
            ret = balancingSubtree(rootNode)
        }
        return ret
    }

    override fun remove(element: T): Boolean {
        val previousRoot = avlRoot
        avlRoot = deleteFromSubtree(avlRoot, element)
        return previousRoot != avlRoot
    }

    override fun clear() {
        avlRoot = null
    }

    private fun deleteFromSubtree(rootNode: AvlTreeNode<T>?, element: T): AvlTreeNode<T>? {
        var ret: AvlTreeNode<T>? = null
        if (rootNode != null) {
            val comp = comparator.compare(rootNode.listOfElement.first(), element)
            when {
                comp < 0 -> {
                    rootNode.rightChild = deleteFromSubtree(rootNode.rightChild, element)
                    ret = balancingSubtree(rootNode)
                }
                comp > 0 -> {
                    rootNode.leftChild = deleteFromSubtree(rootNode.leftChild, element)
                    ret = balancingSubtree(rootNode)
                }
                else -> {
                    rootNode.listOfElement.remove(element)
                    ret = if (rootNode.listOfElement.isEmpty()) {
                        deleteNode(rootNode)
                    } else {
                        rootNode.computeSubtreeSize()
                        rootNode
                    }
                }
            }
        }
        return ret
    }

    private fun deleteNode(node: AvlTreeNode<T>): AvlTreeNode<T>? {
        val ret: AvlTreeNode<T>?
        if (node.rightChild == null && node.leftChild == null) {
            ret = null
        } else if (node.rightChild == null) {
            ret = node.leftChild
        } else if (node.leftChild == null) {
            ret = node.rightChild
        } else {
            var leftNode = node.leftChild
            while (leftNode!!.right != null) {
                leftNode = leftNode.rightChild
            }

            leftNode.leftChild = deleteRight(node.leftChild!!)
            leftNode.rightChild = node.rightChild

            ret = balancingSubtree(leftNode)
        }
        return ret
    }

    private fun deleteRight(node: AvlTreeNode<T>): AvlTreeNode<T>? {
        val right = node.rightChild
        return if (right == null) {
            node.leftChild
        } else {
            node.rightChild = deleteRight(right)
            balancingSubtree(node)
        }
    }

    private fun balancingSubtree(node: AvlTreeNode<T>): AvlTreeNode<T> {
        var ret: AvlTreeNode<T> = node
        node.computeSubtreeSize()
        val balance = node.balance
        if (balance > 1) {
            if (node.rightChild?.balance ?: 0 < 0) {
                node.rightChild = rotateRight(node.rightChild!!)
            }
            ret = rotateLeft(node)
        } else if (balance < -1) {
            if (node.leftChild?.balance ?: 0 > 0) {
                node.leftChild = rotateLeft(node.leftChild!!)
            }
            ret = rotateRight(node)
        }
        return ret
    }

    private fun rotateRight(node: AvlTreeNode<T>): AvlTreeNode<T> {
        val leftNode = node.leftChild
        node.leftChild = leftNode!!.rightChild
        node.computeSubtreeSize()
        leftNode.rightChild = node
        leftNode.computeSubtreeSize()
        return leftNode
    }

    private fun rotateLeft(node: AvlTreeNode<T>): AvlTreeNode<T> {
        val rightNode = node.rightChild
        node.rightChild = rightNode!!.leftChild
        node.computeSubtreeSize()
        rightNode.leftChild = node
        rightNode.computeSubtreeSize()
        return rightNode
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is AvlTree<*>) {
            return false
        }
        return areTreesEqual(this.root, other.root)
    }

    private fun areTreesEqual(
        root1: BinaryTree.Node<*>?,
        root2: BinaryTree.Node<*>?
    ): Boolean {
        if (root1 === root2) {
            return true
        }
        return if (root1 == null || root2 == null) {
            false
        } else
            root1.elements == root2.elements &&
                    areTreesEqual(root1.left, root2.left) &&
                    areTreesEqual(root1.right, root2.right)
    }

    override fun hashCode(): Int {
        if (root == null) {
            return 0
        }
        val nodes = mutableListOf<BinaryTree.Node<*>>()
        avlRoot?.let { nodes.add(it) }
        var res = 17
        while (nodes.isNotEmpty()) {
            val head: BinaryTree.Node<*> = nodes.removeAt(0)
            res = 31 * res + head.hashCode()
            head.left?.let { nodes.add(it) }
            head.right?.let { nodes.add(it) }
        }
        return res
    }

    private class AvlTreeNode<T>(value: T) : BinaryTree.Node<T> {
        var listOfElement: MutableList<T> = mutableListOf(value)
        var leftChild: AvlTreeNode<T>? = null
        var rightChild: AvlTreeNode<T>? = null
        override var numberOfElementInSubtree: Int = 1
        private var height: Int = 1

        override val elements: List<T>
            get() = listOfElement

        override val left: BinaryTree.Node<T>?
            get() = leftChild

        override val right: BinaryTree.Node<T>?
            get() = rightChild

        fun computeSubtreeSize() {
            height = 1 + max((rightChild?.height ?: 0), (leftChild?.height ?: 0))
            val numInRight = (rightChild?.numberOfElementInSubtree ?: 0)
            val numInLeft = (leftChild?.numberOfElementInSubtree ?: 0)
            numberOfElementInSubtree = listOfElement.size + numInRight + numInLeft
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as AvlTreeNode<*>

            if (listOfElement != other.listOfElement) return false
            if (leftChild != other.leftChild) return false
            if (rightChild != other.rightChild) return false

            return true
        }

        override fun hashCode(): Int {
            var result = listOfElement.hashCode()
            result = 31 * result + (leftChild?.hashCode() ?: 0)
            result = 31 * result + (rightChild?.hashCode() ?: 0)
            return result
        }

        val balance: Int
            get() { return (rightChild?.height ?: 0) - (leftChild?.height ?: 0) }

    }

    override fun iterator(): Iterator<T> = BinaryTreeIterator(this)
}
