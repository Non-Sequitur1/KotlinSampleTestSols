package tunes

class StandardTune : Tune {
    private val notesList: MutableList<Note> = mutableListOf()
    override val notes: List<Note> = notesList
    override val totalDuration: Double
        get() = notes.sumOf { it.duration }

    override fun addNote(note: Note) {
        notesList.add(note)
    }

    override fun iterator(): Iterator<Note> = notes.iterator()
}
