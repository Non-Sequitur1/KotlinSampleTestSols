package tunes

class TransposedTune(
    private val targetTune: Tune,
    private val offset: Int,
) : Tune {
    override val notes: List<Note>
        get() = targetTune.notes.map { Note((it.pitch + offset).coerceIn(0, 200), it.duration) }
    override val totalDuration: Double
        get() = targetTune.totalDuration

    override fun addNote(note: Note) {
        targetTune.addNote(Note((note.pitch - offset).coerceIn(0, 200), note.duration))
    }

    override fun iterator(): Iterator<Note> = notes.iterator()
}
