package sortedlist

import io.github.mrasterisco.sortedlist.*
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SortedListTest {

    private fun <T> assertCollectionEquals(c1: Iterable<T>, c2: Iterable<T>) {
        for ((e1, e2) in c1 zip c2) {
            assertEquals(e1, e2, "Collection $c1 is not equal to $c2")
        }
    }

    @Test
    fun checkGet() {
        val l1 = sortedMutableListOf(1, 2, 5, 6)
        assertEquals(1, l1[0])
        assertEquals(2, l1[1])
        assertEquals(5, l1[2])
        assertEquals(6, l1[3])
    }

    @Test
    fun checkIteration() {
        assertCollectionEquals(listOf(1, 2, 5, 6), sortedMutableListOf(1, 2, 5, 6))
    }

    @Test
    fun checkSize() {
        assertEquals(4, sortedMutableListOf(1, 2, 5, 6).size)
    }

    @Test
    fun checkInitWithOrder() {
        assertCollectionEquals(listOf(1, 2, 5, 6), sortedMutableListOf(2, 6, 1, 5))
        assertCollectionEquals(listOf(1, 2, 5, 6), sortedMutableListOf(compareBy { it }, 2, 6, 1, 5))
        assertCollectionEquals(listOf(6, 5, 2, 1), sortedMutableListOf(compareBy { -it }, 2, 6, 1, 5))
    }

    @Test
    fun checkAddWithOrder() {
        val l1 = sortedMutableListOf(1, 2, 5, 6)
        l1.add(4)
        assertTrue(4 in l1)
        assertEquals(4, l1[2])
        assertCollectionEquals(sortedMutableListOf(1, 2, 4, 5, 6), l1)

        val l2: SortedMutableList<String> = sortedMutableListOf("A", "B", "D")
        l2.add("C")
        assertTrue("C" in l2)
        assertEquals("C", l2[2])
        assertCollectionEquals(sortedMutableListOf("A", "B", "C", "D"), l2)
    }

    @Test
    fun checkAdd() {
        val l: SortedMutableList<String> = sortedMutableListOf(compareBy { it.length }, "B", "AAA")
        l.add("DD")
        assertEquals(3, l.size)
        assertTrue("DD" in l)
        assertCollectionEquals(listOf("B", "DD", "AAA"), l)
    }

    @Test
    fun checkRemove() {
        assertCollectionEquals(listOf(1, 2, 5, 6), sortedMutableListOf(2, 6, 8, 1, 5).apply { remove(8) })
        assertCollectionEquals(listOf(1, 2, 5, 8), sortedMutableListOf(2, 6, 8, 1, 5).apply { remove(6) })
        assertCollectionEquals(listOf(1, 2, 5, 6, 8), sortedMutableListOf(2, 6, 8, 1, 5).apply { remove(3) })
    }

    @Test
    fun containsIsBasedOnRealEqualityNotBasedOnComparator() {
        val l: SortedMutableList<String> = sortedMutableListOf(compareBy { it.length }, "B", "CC", "AA", "AAA")
        assertTrue("CC" in l)
        assertTrue("DD" !in l)
        assertTrue("AA" in l)
        assertTrue("AAA" in l)
    }

    @Test
    fun containsShouldDoAroundLog2SizeChecks() {
        val numbers = (1..100000).map { Random.nextInt(10_000_000) }

        var comparatorCounter = 0
        val comparator = Comparator<Int> { t1, t2 ->
            comparatorCounter++
            t1.compareTo(t2)
        }
        val tree = sortedMutableListOf(comparator)

        for (num in numbers) {
            tree.add(num)
        }

        comparatorCounter = 0
        tree.add(5432143)

        assertTrue(comparatorCounter < 32)
    }

    @Test
    fun sortingIsCorrect() {
        val numbers = (1..1000).map { Random.nextInt(10_000_000) }
        val tree = sortedMutableListOf<Int>().apply {
            for(num in numbers) add(num)
        }

        assertCollectionEquals(tree, numbers.sorted())
    }

    @Test
    fun checkToSortedList() {
        val numbers = listOf(1, 6, 22, 5)
        val list = numbers.toSortedList()

        assertCollectionEquals(listOf(1, 5, 6, 22), list)
    }

    @Test
    fun checkToSortedMutableList() {
        val numbers = mutableListOf(34, 2, 1, 29)
        val mutableList = numbers.toSortedMutableList()

        assertCollectionEquals(listOf(1, 2, 29, 34), mutableList)
    }

}