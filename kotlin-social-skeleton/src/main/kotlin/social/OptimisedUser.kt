package social

import java.util.concurrent.locks.ReentrantLock

class OptimisedUser(
    override val userName: String,
    override val yearOfBirth: Int,
    override val bio: String,
    private val befriendingStrategy: (User, User) -> Boolean = ::standardStrategy,
) : User {

    override val lock: ReentrantLock = ReentrantLock()

    var currentFriends_: HashMapLinked<String, User> = HashMapLinked<String, User>()

    override val currentFriends: List<User>
        get() = currentFriends_.values

    override fun hasFriend(u: User): Boolean = currentFriends_.containsKey(u.userName)

    override fun removeFriend(u: User): Boolean = currentFriends_.remove(u.userName) != null

    override fun considerFriendRequest(u: User): Boolean {
        if (!befriendingStrategy(this, u)) return false
        currentFriends_[u.userName] = u
        return true
    }

    override fun removeLongestStandingFriend(): User? = currentFriends_.removeLongestStandingEntry()?.second

    init {
        require(yearOfBirth >= 1900 && yearOfBirth <= 2100) { "Invalid year of birth!" }
    }
}
