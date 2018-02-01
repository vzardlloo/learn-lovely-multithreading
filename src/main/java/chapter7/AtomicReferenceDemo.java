package chapter7;


import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDemo {

    public static AtomicReference<User> atomicReference = new AtomicReference<>();
    private static AtomicIntegerFieldUpdater<User> a = AtomicIntegerFieldUpdater.newUpdater(User.class, "old");

    public static void main(String[] args) {
        User user = new User("vzard", 21);
        atomicReference.set(user);
        User newUser = new User("newguy", 18);
        atomicReference.compareAndSet(user, newUser);
        System.out.println(atomicReference.get().getName());
        System.out.println(atomicReference.get().getOld());

        a.getAndIncrement(atomicReference.get());
        System.out.println(atomicReference.get().getOld());
        a.compareAndSet(newUser, 19, 45);
        System.out.println(atomicReference.get().getOld());

    }


    static class User {
        private String name;
        public volatile int old;

        public User(String name, int old) {
            this.name = name;
            this.old = old;
        }

        public String getName() {
            return name;
        }

        public int getOld() {
            return old;
        }
    }
}
