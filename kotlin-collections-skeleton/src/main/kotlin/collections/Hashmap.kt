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
        val bucket = key.bucket()
        for (i in 0..<bucket.size) {
            val entry = bucket[i]
            if (entry.key == key) {
                val result = entry.value
                entry.value = value
                if (size > buckets.size * MAX_LOAD_FACTOR) {
                    resize()
                }
                return result
            }
        }
        bucket.add(0, ImperialMutableMap.Entry(key, value))
        size++
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
                return result
            }
        }
        return null
    }

    fun K.bucketIndex(): Int = Math.floorMod(hashCode(), buckets.size)

    fun K.bucket(): Bucket<K, V> = buckets[bucketIndex()]

    fun resize() {
        val entries = helperMethod()
        buckets = Array(buckets.size * 2) { bucketFactory() }
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
