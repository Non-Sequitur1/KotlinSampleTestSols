package collections

fun <T> ImperialMutableList<T>.removeAll(other: ImperialMutableList<out T>) {
    for (elToRemove: T in other) this.remove(elToRemove)
}
