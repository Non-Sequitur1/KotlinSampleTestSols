package tunes

data class Note(
    val pitch: Int,
    val duration: Double,
) {
    // Returns true if and only if the pitch of the note is not the maximum allowed pitch— i.e., there exists a note with a pitch above the pitch of this note.
    fun hasNoteAbove(): Boolean = (pitch < 200)

    // Returns true if and only if the pitch of the note is not the minimum allowed pitch—i.e., there exists a note with a pitch below the pitch of this note.
    fun hasNoteBelow(): Boolean = (pitch > 0)

    // Returns a note with the same duration as this note, but with a pitch value one higher. This method can assume that hasNoteAbove() holds, without needing to check it.
    fun noteAbove(): Note = Note(pitch + 1, duration)

    // Returns a note with the same duration as this note, but with a pitch value one lower. This method can assume that hasNoteBelow() holds, without needing to check it.
    fun noteBelow(): Note = Note(pitch - 1, duration)

    init {
        require(pitch >= 0 && pitch <= 200) { "Pitch must be non-negative and no larger than 200" }
        require(duration > 0 && duration <= 64.0) { "Duration must be positive and less than or equal to 64.0" }
    }

    override fun toString(): String {
        val octave: Int = pitch / 12
        val noteName: String = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")[pitch % 12]
        return "${noteName}$octave($duration)"
    }
}
