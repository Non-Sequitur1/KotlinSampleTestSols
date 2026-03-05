package textfiles

import java.util.Random

class Author(private val stringList: List<String>, private val targTextFile: TextFile, private val rng: Random): Runnable {
    override fun run() {
        for (string: String in stringList) {
            targTextFile.insertText(rng.nextInt(targTextFile.length), string)
        }
    }
}