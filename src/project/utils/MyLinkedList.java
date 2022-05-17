package project.utils;

import java.util.ListIterator;

/**
 * Code modified with solutions from UNIFR course OOP.
 * @param <T>
 */
public class MyLinkedList<T> extends java.util.AbstractSequentialList<T> implements Iterable<T>  {
    // FIELDS
    private Node<T> head;
    private Node<T> tail;

    public static void main(String[] args){
        System.out.println("[MyLinkedList.main] Testing the implementation:");
        MyLinkedList<String> lst = new MyLinkedList<>();
        lst.add("Hello");
        lst.add("World");
        System.out.println("[MyLinkedList.main] list size: " + lst.size());

        for( String s : lst ){
            System.out.println(s);
        }

        System.out.println("[MyLinkedList.main] Iterate backward:");
        ListIterator<String> it = lst.listIterator(lst.size() + 1);
        while( it.hasPrevious() ){
            System.out.println(it.previous());
        }

    }

    // CONSTRUCTOR
    public MyLinkedList(){
        this.head = new Node<>(null);
        this.tail = new Node<>(null);
        this.head.previous = this.head;
        this.head.next = this.tail;
        this.tail.previous = this.head;
        this.tail.next = this.tail;
    }

    // PROPERTIES
    public int size() {
        int size = 0;
        Node<T> tempNode = this.head;
        while( tempNode.next != tempNode.next.next ){
            tempNode = tempNode.next;
            size++;
        }
        return size;
    }

    // METHODS

    @Override
    public boolean add(T e) {
        Node<T> newNode = new Node<>(e);
        newNode.next = this.tail;
        newNode.previous = this.tail.previous;
        newNode.previous.next = newNode;
        this.tail.previous = newNode;
        return true;  // no check of duplicate element, etc.
    }

    @Override
    public T get(int index) {
        Node<T> tempNode = this.head;
        while( index-- >= 0){
            tempNode = tempNode.next;
        }
        return tempNode.content;
    }

    public boolean remove(Object o) {
        Node<T> tempNode = this.head;
        while( tempNode.next != this.tail ){
            tempNode = tempNode.next;
            if( tempNode.content.equals(o) ){
                tempNode.previous.next = tempNode.next;
                tempNode.next.previous = tempNode.previous;
                return true;
            }
        }
        return false;
    }

    public ListIterator<T> listIterator(int index) {
        Node<T> tempNode = this.head;
        while( index-- > 0){
            tempNode = tempNode.next;
        }
        return new Iterator<>(tempNode);
    }
}
