package collections

import junit.framework.TestCase.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

// These are examples of the kinds of imports that may be useful when writing tests.
// import kotlin.test.assertEquals
// import kotlin.test.assertFalse
// import kotlin.test.assertTrue

class SinglyLinkedListExtraTests {
    @Test
    fun `removeAt works for in-bound indices`() {
        val list = SinglyLinkedList<Int>()
        for (i in 1..10) {
            list.add(i - 1, i)
            assertEquals(i, list.size)
        }

        // 1 2 3 4 5 7 8 9 10

        assertEquals(list.removeAt(5), 6)
        assertEquals(list[5], 7)
        assertEquals(list.size, 9)
    }

    @Test
    fun `removeAt works for edge indices`() {
        val list = SinglyLinkedList<Int>()
        for (i in 1..10) {
            list.add(i - 1, i)
            assertEquals(i, list.size)
        }

        // 1 2 3 4 5 7 8 9 10

        assertEquals(list.removeAt(0), 1)
        assertEquals(list.removeAt(8), 10)
        assertEquals(list[0], 2)
        assertEquals(list.size, 8)
    }

    @Test
    fun `removeAt behaves properly for indices out of bounds`() {
        val list = SinglyLinkedList<Int>()
        for (i in 1..10) {
            list.add(i - 1, i)
            assertEquals(i, list.size)
        }
        try {
            list.removeAt(10)
            fail()
        } catch (ex: IndexOutOfBoundsException) {
        }
    }

    @Test
    fun `remove behaves properly for elements in list`() {
        val list = SinglyLinkedList<Int>()
        for (i in 1..10) {
            list.add(i - 1, i - 1)
            assertEquals(i, list.size)
        }

        assertTrue(list.remove(4))
        assertTrue(!list.contains(4))
        assertEquals(list.size, 9)
    }

    @Test
    fun `remove removes only the first instance of an element in a list`() {
        val list = SinglyLinkedList<Int>()
        for (i in 1..5) {
            list.add(i - 1, i - 1)
        }
        for (i in 4..8) {
            list.add(i + 1, i)
        }

        assertTrue(list.remove(4))
        assertEquals(list[4], 4)
        assertEquals(list.size, 9)
    }

    @Test
    fun `remove behaves properly for elements non-present in the list`() {
        val list = SinglyLinkedList<Int>()
        for (i in 1..10) {
            list.add(i - 1, i - 1)
            assertEquals(i, list.size)
        }
        assertTrue(!list.remove(11))
    }
}
