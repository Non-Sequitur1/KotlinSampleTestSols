package proglang

import java.util.HashMap
import java.util.concurrent.locks.Lock
import kotlin.concurrent.withLock

class ProgramExecutor(private val body: Stmt, private val lock: Lock, private val pause: Long, private val store: MutableMap<String, Int>): Runnable {
    override fun run() {
        var program: Stmt? = body.clone()
        while (program != null) {
            Thread.sleep(pause)
            lock.withLock {
                program = program?.step(store)
            }
        }
    }
}