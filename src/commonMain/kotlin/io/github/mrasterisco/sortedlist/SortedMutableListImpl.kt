package io.github.mrasterisco.sortedlist

fun <T : Comparable<T>> sortedMutableListOf(vararg elements: T): SortedMutableList<T> {
    val comparator = Comparator<T> { p0, p1 -> p0.compareTo(p1) }
    return SortedMutableListImpl(comparator, *elements)
}

fun <T> sortedMutableListOf(comparator: Comparator<T>, vararg elements: T): SortedMutableList<T> {
    return SortedMutableListImpl(comparator, *elements)
}

class SortedMutableListImpl<T>(comparator: Comparator<T>, vararg elements: T) : SortedMutableList<T> {

    private val tree: BinaryTree<T>

    init {
        tree = AvlTree(comparator, *elements)
    }

    override val size: Int
        get() = tree.size

    override fun add(element: T) = tree.add(element)
    override fun remove(element: T) = tree.remove(element)

    override operator fun get(index: Int): T = tree[index]

    override fun contains(element: T): Boolean = tree.contains(element)
    override fun containsAll(elements: Collection<T>): Boolean = elements.all { contains(it) }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator(tree)

    override fun indexOf(element: T): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean = tree.root == null

    override fun lastIndexOf(element: T): Int {
        TODO("Not yet implemented")
    }

    override fun add(index: Int, element: T) =
        throw UnsupportedOperationException("Adding an item to a specific index may break sorting. Use `add(element:)`.")

    override fun addAll(index: Int, elements: Collection<T>) =
        throw UnsupportedOperationException("Adding an item to a specific index may break sorting. Use `add(element:)`.")

    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach { add(it) }
        return true
    }

    override fun clear() {
        tree.clear()
    }

    override fun listIterator(): MutableListIterator<T> = BinaryTreeIterator(tree)

    override fun listIterator(index: Int): MutableListIterator<T> {
        throw UnsupportedOperationException()
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var removed = false
        elements.forEach { removed = removed && remove(it) }
        return removed
    }

    override fun removeAt(index: Int): T {
        val item = tree[index]
        remove(item)
        return item
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var modified = false
        tree.forEach {
            if (!elements.contains(it)) {
                tree.remove(it)
                modified = true
            }
        }

        return modified
    }

    override fun set(index: Int, element: T): T {
        throw UnsupportedOperationException("Changing an item in place may break sorting. Remove the item and add it again.")
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        throw UnsupportedOperationException()
    }

    override fun toString(): String {
        return "[${tree.joinToString(", ") { it.toString() }}]"
    }

}
