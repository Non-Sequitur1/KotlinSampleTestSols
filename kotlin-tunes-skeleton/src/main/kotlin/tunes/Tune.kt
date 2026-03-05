package tunes

interface Tune {
    val notes: List<Note>
    val totalDuration: Double

    fun addNote(note: Note)

    operator fun iterator(): Iterator<Note>
}
