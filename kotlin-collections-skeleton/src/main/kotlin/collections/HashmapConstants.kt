package collections

typealias Bucket<K, V> = ImperialMutableList<ImperialMutableMap.Entry<K, V>>

const val MAX_LOAD_FACTOR = 0.75
