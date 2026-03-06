package collections

fun <T> ImperialMutableList<T>.removeAll(targList: ImperialMutableList<out T>) {
    for (toRemove: T in targList) {
        this.remove(toRemove)
    }
}