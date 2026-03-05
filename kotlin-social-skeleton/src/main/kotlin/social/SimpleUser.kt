package social

import java.util.concurrent.locks.ReentrantLock

class SimpleUser(
    override val userName: String,
    override val yearOfBirth: Int,
    override val bio: String,
    private val befriendingStrategy: (User, User) -> Boolean = ::standardStrategy,
) : User {

    override val lock: ReentrantLock = ReentrantLock()

    var currentFriends_: MutableList<User> = mutableListOf<User>()

    override val currentFriends: List<User>
        get() = currentFriends_.map { it }

    override fun hasFriend(u: User): Boolean = u in currentFriends_

    override fun removeFriend(u: User): Boolean = currentFriends_.remove(u)

    override fun considerFriendRequest(u: User): Boolean {
        if (!befriendingStrategy(this, u)) return false
        currentFriends_.add(u)
        return true
    }

    override fun removeLongestStandingFriend(): User? = currentFriends_.removeFirstOrNull()

    init {
        require(yearOfBirth >= 1900 && yearOfBirth <= 2100) { "Invalid year of birth!" }
    }
}
