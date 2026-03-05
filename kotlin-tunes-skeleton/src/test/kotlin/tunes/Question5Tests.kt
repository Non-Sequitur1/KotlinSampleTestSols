package tunes

import kotlin.test.Test
import kotlin.test.assertEquals

class Question5Tests {
    @Test
    fun concurrencyTest() {
        for (repeat in 1..20) {
            println("Repeat run $repeat")
            val composerNotes: List<MutableList<Note>> = (0..<8).map { mutableListOf() }
            val expectedNotes: MutableSet<Note> = mutableSetOf()
            for (i in 0..<20000) {
                (0..<8).forEach {
                    val note = Note(i % 200, (i % 4 * it + 1).toDouble())
                    composerNotes[it].add(note)
                    expectedNotes.add(note)
                }
            }

            // TODO: create a ThreadSafeTune

            val tune: ThreadSafeTune = ThreadSafeTune(StandardTune())

            // TODO: create eight Composers using the composerNotes lists

            val eightComposers: List<Composer> = composerNotes.map { Composer(it, tune) }

            // TODO: create eight Threads, one per Composer

            val eightThreads: List<Thread> = eightComposers.map { Thread(it) }

            // TODO: start the threads

            eightThreads.forEach { it.start() }

            // TODO: join the threads

            eightThreads.forEach { it.join() }

            assertEquals(expectedNotes, tune.notes.toSet())
        }
    }
}
