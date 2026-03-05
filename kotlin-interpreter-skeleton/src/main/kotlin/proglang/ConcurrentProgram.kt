package proglang

import java.util.HashMap
import java.util.concurrent.locks.ReentrantLock

class ConcurrentProgram(private val threadBodies: List<Stmt>, private val pauses: List<Long>) {

    private val lock: ReentrantLock = ReentrantLock()

    fun execute(store: Map<String, Int>): Map<String, Int> {
        val workingStore: MutableMap<String, Int> = HashMap()
        for (storeEntryKey: String in store.keys) {
            workingStore[storeEntryKey] = store[storeEntryKey]!!
        }
        val threadList: List<Thread> = List<Thread>(threadBodies.size) { Thread(ProgramExecutor(
            body = threadBodies[it],
            lock = lock,
            pause = pauses[it],
            store = workingStore
        )) }
        threadList.forEach { thread -> thread.start() }
        threadList.forEach { thread -> thread.join() }
        return workingStore
    }

    init {
        require (threadBodies.size == pauses.size) { "List sizes are incompatible!" }
    }
}