package social

fun standardStrategy(
    f: User,
    c: User,
): Boolean = f.currentFriends.all { it.userName != c.userName }

fun unfriendlyStrategy(
    f: User,
    c: User,
): Boolean = false

fun limitOfFiveStrategy(
    f: User,
    c: User,
): Boolean =
    standardStrategy(f, c) &&
        run {
            while (f.currentFriends.size >= 5) f.removeLongestStandingFriend()
            true
        }

fun interestedInDogsStrategy(
    f: User,
    c: User,
): Boolean =
    standardStrategy(f, c) &&
        c.bio
            .lowercase()
            .split(" ")
            .contains("dog")
