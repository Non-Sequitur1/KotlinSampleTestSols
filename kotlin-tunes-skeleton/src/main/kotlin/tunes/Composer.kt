package tunes

class Composer(
    private val notesList: List<Note>,
    private val targTune: Tune,
) : Runnable {
    override fun run() {
        for (note: Note in notesList) {
            targTune.addNote(note)
        }
    }
}
