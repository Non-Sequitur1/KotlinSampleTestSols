package collections;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class SinglyLinkedListJava<T> implements ImperialMutableList<T> {

    private int size = 0;
    private Node<T> head = null;

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("[");
        Node<T> current = head;
        boolean first = true;
        while (current != null) {
            if (!first) {
                result.append(", ");
            }
            first = false;
            result.append(current.element);
            current = current.next;
        }
        result.append("]");
        return result.toString();
    }

    @Override
    public T get(int index) {
        checkIndexInBounds(index);
        Node<T> current = this.head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.element;
    }

    @Override
    public void add(int index, T element) {
        checkIndexInBounds(index, true);
        size++;
        ImperialPair<Node<T>, Node<T>> prevCurr = traverseTo(index);
        Node<T> prev = prevCurr.first();
        Node<T> curr = prevCurr.second();
        Node<T> newNode = new Node<T>(element, curr);
        if (prev == null) {
            head = newNode;
        } else {
            prev.next = newNode;
        }
    }

    @Override
    public void clear() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public boolean contains(T element) {
        Node<T> current = head;
        while (current != null) {
            if (current.element == element) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public T removeAt(int index) {
        checkIndexInBounds(index);
        ImperialPair<Node<T>, Node<T>> prevCurr = traverseTo(index);
        Node<T> prev = prevCurr.first();
        Node<T> curr = prevCurr.second();
        T result = prev.element;
        unlink(prev, curr);
        return result;
    }

    @Override
    public boolean remove(T element) {
        Node<T> previous = null;
        Node<T> current = head;
        while (current != null) {
            if (current.element == element) {
                unlink(previous, current);
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public T set(int index, T element) {
        checkIndexInBounds(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        T result = current.element;
        current.element = element;
        return result;
    }

    @Override
    public void addAll(int index, @NotNull ImperialMutableList<T> other) {
        checkIndexInBounds(index, true);
        Iterator<T> iterator = other.iterator();
        Node<T> start = new Node<T>(iterator.next());
        Node<T> end = start;
        while (iterator.hasNext()) {
            end.next = new Node<T>(iterator.next());
            end = end.next;
        }
        if (index == 0) {
            end.next = head;
            head = start;
        } else {
            ImperialPair<Node<T>, Node<T>> prevCurr = traverseTo(index);
            Node<T> prev = prevCurr.first();
            Node<T> curr = prevCurr.second();
            assert (prev != null);
            prev.next = start;
            end.next = curr;
        }
        size += other.getSize();
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            // TODO: you need to populate this anonymous class, including implementing its methods.
            private Node<T> nextElement = head;

            @Override
            public boolean hasNext() {
                return nextElement != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T result = nextElement.element;
                nextElement = nextElement.next;
                return result;
            }

        };
    }

    private void checkIndexInBounds(int index, boolean inclusive) {
        if (index < 0 || index >= (inclusive ? size + 1 : size)) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void checkIndexInBounds(int index) {
        checkIndexInBounds(index, false);
    }

    private ImperialPair<Node<T>, Node<T>> traverseTo(int index) {
        Node<T> previous = null;
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            previous = current;
            current = current.next;
        }
        return new ImperialPair<>(previous, current);
    }

    private void unlink(Node<T> previous, Node<T> current) {
        if (previous == null) {
            head = current.next;
        } else {
            previous.next = current.next;
        }
        size--;
    }

    private static class Node<T> {
        T element;
        Node<T> next;

        Node(T element, Node<T> next) {
            this.element = element;
            this.next = next;
        }

        Node(T element) {
            this.element = element;
            this.next = null;
        }
    }
}
