package textfiles

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ThreadSafeTextFile(private val targetTextFile: TextFile): TextFile {

    private val tfLock: ReentrantLock = ReentrantLock()

    override val length: Int
        get() = tfLock.withLock {
             targetTextFile.length
        }

    override fun insertText(offset: Int, toInsert: String) = tfLock.withLock {
        targetTextFile.insertText(offset,toInsert)
    }

    override fun deleteText(offset: Int, size: Int) = tfLock.withLock {
        targetTextFile.deleteText(offset,size)
    }

    override fun toString(): String = tfLock.withLock {
        targetTextFile.toString()
    }

}