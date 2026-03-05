package textfiles

class SingleStringTextFile(
    private val initialContents: String,
) : TextFile {
    private val builder: StringBuilder = StringBuilder()

    override val length: Int
        get() = builder.length

    override fun insertText(
        offset: Int,
        toInsert: String,
    ) {
        if (!(offset in 0..length)) throw FileIndexOutOfBoundsException()
        builder.insert(offset, toInsert)
    }

    override fun deleteText(
        offset: Int,
        size: Int,
    ) {
        if (!(size >= 0 && offset >= 0 && offset + size <= length)) throw FileIndexOutOfBoundsException()
        builder.delete(offset, offset + size)
    }

    override fun toString(): String = builder.toString()

    init {
        builder.append(initialContents)
    }
}
