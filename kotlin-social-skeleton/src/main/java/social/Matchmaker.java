package social;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

public final class Matchmaker {
    // If you decide to implement Matchmaker in Kotlin (for reduced marks), you will need to delete
    // this file, and add a Matchmarker.kt file with your Kotlin files.

    private BiFunction<User, User, Boolean> usersAreCompatible;

    public Matchmaker(BiFunction<User, User, Boolean> usersAreCompatible) {
        this.usersAreCompatible = usersAreCompatible;
    }

    public void tryMatching(User a_, User b_) {

        User a = a_;
        User b = b_;

        if (a.hashCode() > b.hashCode()) {
            a = b_; b = a_;
        }

        a.getLock().lock();
        b.getLock().lock();

        try {

            if (usersAreCompatible.apply(a, b)) {
                a.considerFriendRequest(b);
                b.considerFriendRequest(a);
            }

        }
        finally {
            b.getLock().unlock();
            a.getLock().unlock();
        }

    }

}
