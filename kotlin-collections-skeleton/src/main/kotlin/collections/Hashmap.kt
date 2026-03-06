package collections

class Hashmap<K, V>(
    private val bucketFactory: () -> Bucket<K, V>,
) : ImperialMutableMap<K, V> {

    // Problem:  TODO - briefly describe the problem you have identified.
    // Solution: TODO - briefly explain how you have solved this problem.
    //
    // Problem:  TODO - briefly describe the problem you have identified.
    // Solution: TODO - briefly explain how you have solved this problem.
    //
    // TODO - add further Problem/Solution entries for other problems that you identify.

    var buckets: Array<Bucket<K, V>> = Array(16) { bucketFactory() }

    override var size: Int = 0

    // The iterator implementation works by putting all entries of the map into a list, and then returns an iterator to
    // this list. This is simpler than the "on demand" iterator that you implemented during a lab exercise on hashmaps.
    // This simpler approach is intentional here; it is not one of the problems you are supposed to identify.
    override fun iterator(): Iterator<ImperialMutableMap.Entry<K, V>> = helperMethod().iterator()

    override fun put(key: K, value: V): V? {
        if (size > buckets.size * MAX_LOAD_FACTOR) {
            resize()
        }
        val bucket = key.bucket()
        for (entry : ImperialMutableMap.Entry<K, V> in bucket) { // iterator-based access
            if (entry.key == key) {
                val result = entry.value
                entry.value = value
                // The size doesn't even change here!
                return result
            }
        }
        bucket.add(0, ImperialMutableMap.Entry(key, value))
        size++
        // Here you did not check for the size AFTER doing the add!

        return null
    }

    override fun get(key: K): V? {
        for (entry in key.bucket()) {
            if (entry.key == key) {
                return entry.value
            }
        }
        return null
    }

    override fun remove(key: K): V? {
        val bucket = key.bucket()
        for ((index, entry) in bucket.withIndex()) {
            if (entry.key == key) {
                val result = entry.value
                bucket.removeAt(index)
                size-- // you need to decrement the size
                return result
            }
        }
        return null
    }

    fun K.bucketIndex(): Int = hashCode() and (buckets.size  - 1) // Math.floorMod(hashCode(), buckets.size)

    fun K.bucket(): Bucket<K, V> = buckets[bucketIndex()]

    fun resize() {
        val entries = helperMethod()
        buckets = Array(buckets.size shl 1) { bucketFactory() }
        size = 0 // You forgot to reinitialise the size here!
        entries.forEach {
            put(it.key, it.value)
        }
    }

    fun helperMethod(): SinglyLinkedList<ImperialMutableMap.Entry<K, V>> {
        val entries = SinglyLinkedList<ImperialMutableMap.Entry<K, V>>()
        for (bucket in buckets) {
            for (entry in bucket) {
                entries.add(0, entry)
            }
        }
        return entries
    }
}
