package project.utils;

import java.util.ListIterator;

/**
 * Code modified with solutions from UNIFR course OOP.
 */
public class Iterator<T> implements ListIterator<T> {

    // FIELDS
    Node<T> currentNode;

    // CONSTRUCTOR
    public Iterator(Node<T> initialCursor){
        this.currentNode = initialCursor;
    }

    // METHODS
    @Override
    public boolean hasNext() {
        return this.currentNode.next != this.currentNode.next.next;
    }

    @Override
    public T next() {
        if( !hasNext() ){
            throw new Error("Out of the list");
        }
        this.currentNode = this.currentNode.next;
        return this.currentNode.content;
    }

    @Override
    public boolean hasPrevious() {
        return this.currentNode.previous != this.currentNode.previous.previous;
    }

    @Override
    public T previous() {
        if( !hasPrevious() ){
            throw new Error("Out of the list");
        }
        this.currentNode = this.currentNode.previous;
        return this.currentNode.content;
    }

    @Override
    public int nextIndex() {
        int index = 1;
        Node<T> tempNode = currentNode;
        while( tempNode.previous != tempNode.previous.previous ){
            tempNode = tempNode.previous;
            index++;
        }
        return index;
    }

    @Override
    public int previousIndex() {
        return nextIndex() - 2;
    }

    /**
     * removes the current node. Set its previous as the new current node if not null, else its next.
     */
    @Override
    public void remove() {
        this.currentNode.previous.next = this.currentNode.next;
        this.currentNode.next.previous = this.currentNode.previous;
        this.currentNode = this.currentNode.previous;
    }

    @Override
    public void set(T t) {
        this.currentNode.content = t;
    }

    @Override
    public void add(T t) {
        Node<T> tempNode = new Node<>(t);
        tempNode.previous = this.currentNode.previous;
        tempNode.next = this.currentNode;
        this.currentNode.previous = tempNode;
        tempNode.previous.next = tempNode;
    }
}
