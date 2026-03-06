package collections

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

class StripedHashmap<K, V>(
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
    private val locks: Array<ReentrantLock> = Array(16) { ReentrantLock() }

    private val size_: AtomicInteger = AtomicInteger(0)
    override val size: Int
        get() = size_.get()

    // The iterator implementation works by putting all entries of the map into a list, and then returns an iterator to
    // this list. This is simpler than the "on demand" iterator that you implemented during a lab exercise on hashmaps.
    // This simpler approach is intentional here; it is not one of the problems you are supposed to identify.
    override fun iterator(): Iterator<ImperialMutableMap.Entry<K, V>> = helperMethod().iterator()

    override fun put(
        key: K,
        value: V,
    ): V? {
        if (size > buckets.size * MAX_LOAD_FACTOR) {
            resize()
        }
        key.bucketLock().lock()
        try {
            val bucket = key.bucket()
            for (entry: ImperialMutableMap.Entry<K, V> in bucket) { // iterator-based access
                if (entry.key == key) {
                    val result = entry.value
                    entry.value = value
                    // The size doesn't even change here!
                    return result
                }
            }
            bucket.add(0, ImperialMutableMap.Entry(key, value))
            size_.incrementAndGet()
            // Here you did not check for the size AFTER doing the add!
            return null
        } finally {
            key.bucketLock().unlock()
        }
    }

    override fun get(key: K): V? {
        key.bucketLock().lock()
        try {
            for (entry in key.bucket()) {
                if (entry.key == key) {
                    return entry.value
                }
            }
            return null
        } finally {
            key.bucketLock().unlock()
        }
    }

    override fun remove(key: K): V? {
        key.bucketLock().lock()
        try {
            val bucket = key.bucket()
            for ((index, entry) in bucket.withIndex()) {
                if (entry.key == key) {
                    val result = entry.value
                    bucket.removeAt(index)
                    size_.decrementAndGet() // you need to decrement the size
                    return result
                }
            }
            return null
        } finally {
            key.bucketLock().unlock()
        }
    }

    fun K.bucketIndex(): Int = hashCode() and (buckets.size - 1) // Math.floorMod(hashCode(), buckets.size)

    fun K.bucket(): Bucket<K, V> = buckets[bucketIndex()]

    fun K.bucketLockIndex() = bucketIndex() and (locks.size - 1)

    fun K.bucketLock() = locks[bucketLockIndex()]

    fun resize() {
        locks.forEach { lock -> lock.lock() }
        try {
            if (!(size > buckets.size * MAX_LOAD_FACTOR)) return
            val entries = helperMethod()
            buckets = Array(buckets.size shl 1) { bucketFactory() }
            entries.forEach {
                it.key.bucket().add(0, ImperialMutableMap.Entry(it.key, it.value))
            }
        } finally {
            locks.reversed().forEach { lock -> lock.unlock() }
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
