package social

import java.util.concurrent.locks.Lock

interface User {
    val userName: String
    val yearOfBirth: Int
    val bio: String
    val lock: Lock
    val currentFriends: List<User>

    fun hasFriend(u: User): Boolean

    fun removeFriend(u: User): Boolean

    fun considerFriendRequest(u: User): Boolean

    fun removeLongestStandingFriend(): User?
}
