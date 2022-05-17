package project.utils;

/**
 * Code modified with solutions from UNIFR course OOP.
 * @param <T>
 */
public class Node<T> {
    // Fields
    T content;
    Node<T> next;
    Node<T> previous;

    // Constructor
    public Node(T content) {
        this.content = content;
    }
}
