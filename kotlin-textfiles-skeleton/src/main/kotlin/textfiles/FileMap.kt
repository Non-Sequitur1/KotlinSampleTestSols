package textfiles

private val INITIAL_BUCKET_COUNT: Int = 4
private val MAP_SIZE_THRESHOLD: Double = 0.75

class FileMap {
    private var buckets: MutableList<MutableList<Pair<String, TextFile>>> = MutableList(INITIAL_BUCKET_COUNT) { mutableListOf() }

    var size: Int = 0
        private set
    operator fun get(key: String): TextFile? {
        for (entry: Pair<String, TextFile> in key.bucket()) {
            if (entry.first == key) return entry.second
        }
        return null
    }
    operator fun set(key: String, tf: TextFile) {
        val it=key.bucket().iterator()
        while (it.hasNext()){
            val entry:Pair<String,TextFile> =it.next()
            if(entry.first==key) {
                it.remove()
            }
        }
        key.bucket().add(key to tf)
        size++
        if (size >= MAP_SIZE_THRESHOLD*buckets.size) resize()
    }

    public operator fun iterator(): Iterator<Pair<String, TextFile>> = object: Iterator<Pair<String, TextFile>> {
        private var bucketIterator: Iterator<MutableList<Pair<String, TextFile>>> = buckets.iterator()
        private var elIterator: Iterator<Pair<String, TextFile>> = bucketIterator.next().iterator()

        override fun hasNext(): Boolean {
            while (bucketIterator.hasNext() && !elIterator.hasNext()) elIterator = bucketIterator.next().iterator()
            return elIterator.hasNext()
        }

        override fun next(): Pair<String, TextFile> {
            if (!hasNext()) throw NoSuchElementException()
            return elIterator.next()
        }

    }

    private fun resize() {
        val entries: MutableList<Pair<String, TextFile>> = mutableListOf()
        for (entry: Pair<String, TextFile>in this) entries.add(entry)
        buckets = MutableList(buckets.size shl 1) { mutableListOf() }
        size = 0
        for (entry: Pair<String, TextFile> in entries) this[entry.first]=entry.second
    }


    private fun String.bucketIndex(): Int = this.hashCode() and (buckets.size-1)
    private fun String.bucket(): MutableList<Pair<String, TextFile>> = buckets[this.bucketIndex()]

}