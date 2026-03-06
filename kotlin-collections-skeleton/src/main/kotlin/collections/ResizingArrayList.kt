package collections

class ResizingArrayList<T>(
    private val initialCapacity: Int = 16,
) : ImperialMutableList<T> {
    private var array: Array<T?> = arrayOfNulls<Any?>(initialCapacity) as Array<T?>

    override var size: Int = 0
        private set

    private fun resize() {
        if (size < array.size) return
        var newArray: Array<T?> = arrayOfNulls<Any?>((array.size shl 1).coerceAtLeast(1)) as Array<T?>
        for (i in 0..array.size - 1) newArray[i] = array[i]
        array = newArray
    }

    override operator fun get(index: Int): T {
        if (!(index >= 0 && index < size)) throw IndexOutOfBoundsException()
        return array[index]!!
    }

    override fun add(
        index: Int,
        element: T,
    ) {
        if (!(index >= 0 && index <= size)) throw IndexOutOfBoundsException()
        resize()
        for (i in (index until size).reversed()) {
            array[i + 1] = array[i]
        }
        array[index] = element
        size++
    }

    override fun clear() {
        array = arrayOfNulls<Any?>(initialCapacity) as Array<T?>
        size = 0
    }

    override fun contains(element: T): Boolean = array.contains(element)

    override fun removeAt(index: Int): T {
        if (!(index >= 0 && index < size)) throw IndexOutOfBoundsException()
        // find index to remove, then overwrite values.
        val oldValue: T = array[index]!!
        for (i in index until size) {
            array[i] = array[i + 1]
        }
        size--
        return oldValue
    }

    override fun remove(element: T): Boolean {
        val elIndex: Int = array.indexOf(element)
        if (elIndex == -1) return false
        removeAt(elIndex)
        return true
    }

    override operator fun set(
        index: Int,
        element: T,
    ): T {
        if (!(index >= 0 && index < size)) throw IndexOutOfBoundsException()
        val oldValue: T = array[index]!!
        array[index] = element
        return oldValue
    }

    override fun addAll(
        index: Int,
        other: ImperialMutableList<T>,
    ) {
        for (item: T in other.reversed()) {
            add(index, item)
        }
    }

    override fun iterator(): Iterator<T> =
        object : Iterator<T> {
            private var index: Int = 0

            override fun hasNext(): Boolean = index < size

            override fun next(): T =
                if (hasNext()) {
                    array[index++]!!
                } else {
                    throw NoSuchElementException()
                }
        }

    override fun toString(): String {
        val result = StringBuilder()
        result.append("[")
        var current: Int = 0
        var first = true
        while (current < size) {
            if (!first) {
                result.append(", ")
            }
            first = false
            result.append(array[current++])
        }
        result.append("]")
        return result.toString()
    }

    init {
        require(initialCapacity >= 0) { "Array initial capacity cannot be negative." }
    }
}
