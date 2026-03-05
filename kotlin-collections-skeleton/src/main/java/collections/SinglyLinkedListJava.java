package collections;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class SinglyLinkedListJava<T> implements ImperialMutableList<T> {

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
        throw new RuntimeException("TODO: you need to implement this method.");
    }

    @Override
    public void add(int index, T element) {
        throw new RuntimeException("TODO: you need to implement this method.");
    }

    @Override
    public void clear() {
        throw new RuntimeException("TODO: you need to implement this method.");
    }

    @Override
    public boolean contains(T element) {
        throw new RuntimeException("TODO: you need to implement this method.");
    }

    @Override
    public T removeAt(int index) {
        throw new RuntimeException("TODO: you need to implement this method.");
    }

    @Override
    public boolean remove(T element) {
        throw new RuntimeException("TODO: you need to implement this method.");
    }

    @Override
    public T set(int index, T element) {
        throw new RuntimeException("TODO: you need to implement this method.");
    }

    @Override
    public void addAll(int index, @NotNull ImperialMutableList<T> other) {
        throw new RuntimeException("TODO: you need to implement this method.");
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            // TODO: you need to populate this anonymous class, including implementing its methods.

            @Override
            public boolean hasNext() {
                throw new RuntimeException("TODO: you need to implement this method.");
            }

            @Override
            public T next() {
                throw new RuntimeException("TODO: you need to implement this method.");
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
}
