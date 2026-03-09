package collections

// Useful imports that you will probably need:
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class StripedHashmap<K, V>(
    private val bucketFactory: () -> Bucket<K, V>,
) : ImperialMutableMap<K, V> {
    private var buckets: Array<Bucket<K, V>> = Array(16) { bucketFactory() }
    // Users should not be allowed to view the bucket set.

    private var locks: Array<ReentrantLock> = Array(16) { ReentrantLock() }
    // Users should not be allowed to view the lock set.

    private val size_: AtomicInteger = AtomicInteger(0)

    override val size: Int
        get() = size_.get()
    // Users should not be allowed to modify the size on demand.

    // The iterator implementation works by putting all entries of the map into a list, and then returns an iterator to
    // this list. This is simpler than the "on demand" iterator that you implemented during a lab exercise on hashmaps.
    // This simpler approach is intentional here; it is not one of the problems you are supposed to identify.
    override fun iterator(): Iterator<ImperialMutableMap.Entry<K, V>> = helperMethod().iterator()

    override fun put(
        key: K,
        value: V,
    ): V? {
        // Problem:
        // Size checking was not properly performed.
        // Solution:
        // I had moved the block of code that checks for resizing to the start of the function.
        if (size > buckets.size * MAX_LOAD_FACTOR) {
            resize()
        }
        key.bucketLock().withLock {
            val bucket = key.bucket()

            // Problem:
            // Here, index-based iteration is used; this is highly inefficient for linked list based ImperialMutableList implementations.
            // Solution:
            // This has been modified to use iterators.

            for (entry: ImperialMutableMap.Entry<K, V> in bucket) {
                if (entry.key == key) {
                    val result = entry.value
                    entry.value = value
                    return result
                }
            }
            bucket.add(0, ImperialMutableMap.Entry(key, value))
            size_.incrementAndGet()
            return null
        }
    }

    override fun get(key: K): V? =
        key.bucketLock().withLock {
            for (entry in key.bucket()) {
                if (entry.key == key) {
                    return entry.value
                }
            }
            return null
        }

    override fun remove(key: K): V? =
        key.bucketLock().withLock {
            val bucket = key.bucket()
            for ((index, entry) in bucket.withIndex()) {
                if (entry.key == key) {
                    val result = entry.value
                    bucket.removeAt(index)
                    // Problem:
                    // `size` was not being properly decremented.
                    // Solution:
                    // `size--` has been added.
                    size_.decrementAndGet()
                    return result
                }
            }
            return null
        }

    // Users should not be able to call these functions on demand.

    private fun K.bucketIndex(): Int = Math.floorMod(hashCode(), buckets.size)

    private fun K.bucket(): Bucket<K, V> = buckets[bucketIndex()]

    private fun K.bucketLockIndex(): Int = bucketIndex() % locks.size

    private fun K.bucketLock(): ReentrantLock = locks[bucketLockIndex()]

    // Users should not be able to call this function on demand.
    private fun resize() {
        locks.forEach { lock -> lock.lock() }
        try {
            // Does it still need resizing?

            if (size <= buckets.size * MAX_LOAD_FACTOR) return

            val entries = helperMethod()
            buckets = Array(buckets.size shl 1) { bucketFactory() }
            // Problem:
            // `size` was not properly reinitialised upon resize.
            // Solution:
            // `size = 0` to reinitialise size.

            for (entry: ImperialMutableMap.Entry<K, V> in entries) {
                entry.key.bucket().add(0, ImperialMutableMap.Entry(entry.key, entry.value))
            }
        } finally {
            locks.reversed().forEach { lock -> lock.unlock() }
        }
    }

    private fun helperMethod(): SinglyLinkedList<ImperialMutableMap.Entry<K, V>> {
        val entries = SinglyLinkedList<ImperialMutableMap.Entry<K, V>>()
        for (bucket in buckets) {
            for (entry in bucket) {
                entries.add(0, entry)
            }
        }
        return entries
    }
}
