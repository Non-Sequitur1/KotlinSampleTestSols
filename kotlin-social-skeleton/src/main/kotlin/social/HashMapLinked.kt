package social

private const val INITIAL_BUCKETS = 8
private const val LOAD_FACTOR = 0.75

class HashMapLinked<K, V> : OrderedMap<K, V> {
    private class Node<K, V>(
        val key: K,
        var value: V,
        var prev: Node<K, V>?,
        var next: Node<K, V>? = null,
    )

    private var head: Node<K, V>? = null
    private var tail: Node<K, V>? = null

    private var buckets: MutableList<MutableList<Node<K, V>>>

    init {
        buckets = mutableListOf()
        for (i in 0..<INITIAL_BUCKETS) {
            buckets.add(mutableListOf())
        }
    }

    // The size property will yield the number of key-value pairs currently in the map.
    override var size = 0
        private set

    //  The values property will yield a List containing all of the values in the map, ordered
    // according to when they were added to the map.
    override val values: List<V>
        get() {
            val allValues: MutableList<V> = mutableListOf<V>()
            var curr: Node<K, V>? = head
            while (curr != null) {
                allValues.add(curr.value)
                curr = curr.next
            }
            return allValues
        }

    // The containsKey method takes a key and returns true if and only if the map contains
    // an entry that maps this key to some value.

    // containsKey: This involves locating a node in a hash bucket associated with the
    // given key, and returning true if and only if such a node exists.
    override fun containsKey(key: K): Boolean = getBucket(key).any { it.key == key }

    // The remove method takes a key. If an entry that maps this key to some value exists
    // in the map, this entry should be removed and the value with which the key was
    // associated should be returned. Otherwise, the map should be left unchanged and
    // null should be returned.

    //  remove: As in the case of a regular hashmap, this method should determine whether
    // an entry is present for the given key.
    //
    // If so, the associated Node should be removed from the hash bucket.
    // Additionally, the Node should be unlinked from the doubly-linked list:
    //
    // if the Node has a previous (prev) node and a next (node) node, these should be updated
    // so that the successor of prev becomes next and the predecessor of next becomes prev.

    // A challenge here is to think through edge cases, such as when
    // the node being removed is the head or the tail of the doubly-linked list.
    //
    // The size of the linked hashmap should be adjusted appropriately.
    //
    // The remove method should not perform a resize operation:
    // once the number of buckets in a linked hashmap has been increased, it never decreases.
    override fun remove(key: K): V? {
        val iter: MutableListIterator<Node<K, V>> = getBucket(key).listIterator()

        while (iter.hasNext()) {
            val curr : Node<K, V> = iter.next()
            if (curr.key == key) {
                iter.remove()
                return unlinkNode(curr)
            }
        }
        return null
    }

    private fun unlinkNode(curr: Node<K, V>) : V {
        val oldValue: V = curr.value
        curr.prev?.let {
            it.next = curr.next
            true
        } ?: run {
            // here then current would be the head of the node if its previous node is null
            head = curr.next
            false
        }

        curr.next?.let {
            it.prev = curr.prev
            true
        } ?: run {
            // here then the current would be tail of the node if its next node is null
            // as above, its previous node would have already had its next set to null
            tail = curr.prev
            false
        }

        // clear out the pointers

        curr.prev = null
        curr.next = null

        // decrement size
        size--

        return oldValue
    }

    // The set method takes a key and a value, and associates the key with the value. If
    // some value was previously associated with the key, the existing key-value pair should
    // be removed from the map, and the new key-value pair added instead. With respect
    // to the ordering, the new key-value pair should be the newest key-value pair, as it
    // has been most recently added to the map. The method should return the value
    // previously associated with the given key, or null if there was no such value.

    // set: Having implemented remove, set can be implemented relatively easily.

    // First, call remove to remove any existing entry associated with the given key, and obtain
    // the associated value if such an entry exists. The result of this call to remove (either
    // a value, or null), is what set should ultimately return.
    //
    // A new node representing the key-value pair associated with the call to set can then
    // be created and placed in a suitable hash bucket. This node should be added to the end
    // of the doubly-linked list, so that its predecessor is the previous tail of the list,
    // and it becomes the new tail of the list.
    //
    // A challenge here again is to make this work in edge case scenarios,
    // such as when the linked hashmap is empty.
    //
    // The size of the linked hashmap should be adjusted appropriately,
    // and resize should be called in case the hash buckets need to be resized.

    override operator fun set(
        key: K,
        value: V,
    ): V? {
        val removedVal: V? = remove(key)

        val newNode: Node<K, V> = Node<K, V>(key, value, tail)
        if (head == null) head = newNode
        tail?.next = newNode
        tail = newNode

        getBucket(key).add(newNode)

        size++

        resize()

        return removedVal
    }

    //  The removeLongestStandingEntry method takes no arguments and should return
    // null if the map is empty. Otherwise, it should remove and return the key-value pair
    // that has been present in the map for the longest.
    override fun removeLongestStandingEntry(): Pair<K, V>? = head?.let { remove(it.key)?.let { v -> it.key to v } }

    private fun getBucket(key: K) = buckets[key.hashCode().mod(buckets.size)]

    private fun resize() {
        if (size <= LOAD_FACTOR * buckets.size) {
            return
        }
        val allContent = mutableListOf<Node<K, V>>()
        for (bucket in buckets) {
            allContent.addAll(bucket)
        }

        val newNumBuckets = buckets.size * 2

        buckets = mutableListOf()
        for (i in 0..<newNumBuckets) {
            buckets.add(mutableListOf())
        }

        for (node in allContent) {
            getBucket(node.key).add(node)
        }
    }
}
