package textfiles

private const val BLOCK_SIZE: Int = 8

class MultiStringTextFile(
    initialContents: String,
) : TextFile {
    private var blocks: MutableList<StringBuilder> = mutableListOf(StringBuilder(initialContents))

    /**
     * Yields the number of blocks that are currently used to represent the file.
     *
     * @return The number of blocks used to represent this file.
     */
    val numBlocks: Int
        get() = blocks.size

    override val length: Int
        get() = blocks.sumOf { it.length }

    private val concatenatedBlocks: StringBuilder
        get() = blocks.fold(StringBuilder(), {acc, el -> acc.append(el)})

    init {
        rebalance()
    }

    override fun toString(): String = concatenatedBlocks.toString()

    /**
     * Reorganises the internal representation of the text file so that the content is partitioned
     * into blocks of equal size, except that the final block may be shorter.
     */
    fun rebalance() {
        val concatenatedBlock: StringBuilder = concatenatedBlocks
        blocks=mutableListOf()
        while (concatenatedBlock.isNotEmpty()) {
            val thisBlockSize: Int = BLOCK_SIZE.coerceAtMost(concatenatedBlock.length)
            val block: StringBuilder = StringBuilder(concatenatedBlock.substring(0, thisBlockSize))
            concatenatedBlock.delete(0,thisBlockSize)
            blocks.add(block)
        }
    }

    override fun insertText(
        offset: Int,
        toInsert: String,
    ) {
        if (!(offset in 0..length)) throw FileIndexOutOfBoundsException()
        if (offset == length) blocks.add(StringBuilder())
        var start = 0
        for (block: StringBuilder in blocks) {
            if (!(offset >= start && offset <= start+block.length)) {
                // it is outside the block; we move on.
                start += block.length
                continue
            }
            // now, we have it inside the block. we roll.
            val blockOffset: Int = offset - start
            block.insert(blockOffset, toInsert)
            break
        }
    }

    override fun deleteText(
        offset: Int,
        size: Int,
    ) {
        if (offset < 0 || size < 0) {
            throw FileIndexOutOfBoundsException()
        }
        val newBlocks: MutableList<StringBuilder> = mutableListOf()
        var index = 0
        var block = 0
        while (block < blocks.size) {
            if (index < offset + size && offset < index + blocks[block].length) {
                if (size == 0) {
                    return
                }
                // Add the prefix
                val prefixSize: Int = offset - index
                if (prefixSize > 0) {
                    newBlocks.add(StringBuilder(blocks[block].substring(0, prefixSize)))
                }
                index += prefixSize
                // If the suffix is in the same block, add it. Otherwise, skip over blocks.
                if (prefixSize + size < blocks[block].length) {
                    newBlocks.add(StringBuilder(blocks[block].substring(prefixSize + size)))
                } else {
                    var deleted: Int = blocks[block].length - prefixSize
                    block++
                    while (block < blocks.size && size - deleted >= blocks[block].length) {
                        deleted += blocks[block].length
                        block++
                    }
                    if (deleted < size) {
                        if (block >= blocks.size) {
                            throw FileIndexOutOfBoundsException()
                        }
                        assert(blocks[block].length > size - deleted)
                        newBlocks.add(StringBuilder(blocks[block].substring(size - deleted)))
                    }
                }
                index += size
            } else {
                newBlocks.add(blocks[block])
                index += blocks[block].length
            }
            block++
        }
        if (offset >= index) {
            if (offset > index || size > 0) {
                throw FileIndexOutOfBoundsException()
            }
        }
        blocks = newBlocks
    }


}
