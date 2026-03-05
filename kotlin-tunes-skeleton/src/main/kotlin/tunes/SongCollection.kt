package tunes

class SongCollection {
    private class Song(
        val name: String,
        val tune: Tune,
    )

    private class TreeNode(
        var song: Song,
        var left: TreeNode? = null,
        var right: TreeNode? = null,
    )

    private var root: TreeNode? = null

    fun addSong(
        name: String,
        tune: Tune,
    ) {
        root?.let{ rt ->
            fun addSongHelper(
                curr: TreeNode,
                song: Song,
            ) {
                val cmp: Int = song.name.compareTo(curr.song.name)
                when {
                    (cmp < 0) -> {
                        curr.left?.let { addSongHelper(it, song) } ?: run {
                            curr.left = TreeNode(song)
                        }
                    }
                    (cmp > 0) -> {
                        curr.right?.let { addSongHelper(it, song) } ?: run {
                            curr.right = TreeNode(song)
                        }
                    }
                    (cmp == 0) -> {
                        throw UnsupportedOperationException("Song name already present")
                    }
                }
            }
            addSongHelper(rt, Song(name, tune))
            true
        } ?: run {
            root = TreeNode(Song(name, tune))
            false
        }
    }

    fun getTune(name: String): Tune {
        fun getTuneHelper(
            curr: TreeNode,
            name: String,
        ): Tune {
            val cmp: Int = name.compareTo(curr.song.name)
            when {
                (cmp < 0) -> {
                    return curr.left?.let { getTuneHelper(it, name) } ?: throw NoSuchElementException()
                }
                (cmp > 0) -> {
                    return curr.right?.let { getTuneHelper(it, name) } ?: throw NoSuchElementException()
                }
                (cmp == 0) -> {
                    return curr.song.tune
                }
                else -> {
                    throw IllegalStateException()
                }
            }
        }
        return root?.let { getTuneHelper(it, name) } ?: throw NoSuchElementException()
    }

    fun getSongNames(): List<String> {
        fun getSongNamesHelper(curr: TreeNode): List<String> =
            (
                curr.left?.let {
                    getSongNamesHelper(it)
                } ?: emptyList()
            ).plus(curr.song.name).plus(
                curr.right?.let {
                    getSongNamesHelper(it)
                } ?: emptyList(),
            )
        return root?.let { getSongNamesHelper(it) } ?: emptyList()
    }
}
