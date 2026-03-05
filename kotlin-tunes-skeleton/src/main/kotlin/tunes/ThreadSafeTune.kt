package tunes

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ThreadSafeTune(
    private val targetTune: Tune,
) : Tune {
    private val tuneLock: ReentrantLock = ReentrantLock()
    override val notes: List<Note>
        get() =
            tuneLock.withLock {
                targetTune.notes
            }
    override val totalDuration: Double
        get() =
            tuneLock.withLock {
                targetTune.totalDuration
            }

    override fun addNote(note: Note) =
        tuneLock.withLock {
            targetTune.addNote(note)
        }

    override fun iterator(): Iterator<Note> =
        tuneLock.withLock {
            targetTune.iterator()
        }
}
