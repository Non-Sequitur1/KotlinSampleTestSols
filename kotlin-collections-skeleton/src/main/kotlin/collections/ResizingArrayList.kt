package collections

data class ResizingArrayList<T>(
    private val initialCapacity: Int = 16,
) : ImperialMutableList<T> {
    private var backingArray: Array<T?> = arrayOfNulls<Any?>(initialCapacity) as Array<T?>

    override var size: Int = 0
        private set

    override operator fun get(index: Int): T =
        if (index in
            0 until size
        ) {
            backingArray[index]!!
        } else {
            throw IndexOutOfBoundsException()
        }

    private fun resize(numOfElsToAdd: Int) {
        if (size + numOfElsToAdd <= backingArray.size) return
        val newArray: Array<T?> = arrayOfNulls<Any?>((backingArray.size shl 1).coerceAtLeast(numOfElsToAdd)) as Array<T?>
        for (i in 0 until backingArray.size) newArray[i] = backingArray[i]
        backingArray = newArray
    }

    override fun clear() {
        backingArray = arrayOfNulls<Any?>(initialCapacity) as Array<T?>
        size = 0
    }

    override fun removeAt(index: Int): T {
        if (index !in 0 until size) throw IndexOutOfBoundsException()
        val oldValue: T = get(index)
        var i: Int = size
        while (i > index) {
            backingArray[i - 1] = backingArray[i]
            i--
        }
        size--
        return oldValue
    }

    override operator fun iterator(): Iterator<T> =
        object : Iterator<T> {
            private var index: Int = 0

            override fun hasNext(): Boolean = index < size

            override fun next(): T =
                if (hasNext()) {
                    backingArray[index++]!!
                } else {
                    throw NoSuchElementException()
                }
        }

    override fun addAll(
        index: Int,
        other: ImperialMutableList<T>,
    ) {
        if (index !in 0..size) throw IndexOutOfBoundsException()
        val numOfElsToAdd: Int = other.size
        resize(numOfElsToAdd)
        for (i in (index until size).reversed()) {
            backingArray[i + numOfElsToAdd] = backingArray[i]
        }
        for (i in (0 until numOfElsToAdd)) backingArray[index + i] = other[i]
        size += numOfElsToAdd
    }

    override operator fun set(
        index: Int,
        element: T,
    ): T {
        if (index !in 0 until size) throw IndexOutOfBoundsException()
        val oldValue: T = get(index)
        backingArray[index] = element
        return oldValue
    }

    override fun remove(element: T): Boolean {
        for (index in 0 until size) {
            if (backingArray[index]!! == element) {
                var i: Int = size
                while (i > index) {
                    backingArray[i - 1] = backingArray[i]
                    i--
                }
                size--
                return true
            }
        }
        return false
    }

    override fun contains(element: T): Boolean {
        for (index in 0 until size) {
            if (backingArray[index]!! == element) {
                return true
            }
        }
        return false
    }

    override fun add(
        index: Int,
        element: T,
    ) {
        if (index !in 0..size) throw IndexOutOfBoundsException()
        resize(1)
        for (i in (index until size).reversed()) {
            backingArray[i + 1] = backingArray[i]
        }
        backingArray[index] = element
        size++
    }

    init {
        require(initialCapacity >= 0) { "Initial capacity must be non-negative!" }
    }

    override fun toString(): String = backingArray.slice(0 until size).joinToString(", ", prefix = "[", postfix = "]")
}
