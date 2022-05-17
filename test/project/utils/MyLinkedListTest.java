package project.utils;

import org.junit.Test;

import java.util.ListIterator;
import static org.junit.Assert.*;

public class MyLinkedListTest {

    /**
     * Tests the basic add and get features of the linked list.
     */
    @Test
    public void addGet() {
        MyLinkedList<String> l = new MyLinkedList<>();
        l.add("First");
        assertEquals( "First", l.get(0));
        l.add("Second");
        assertEquals("Second", l.get(1));
        assertEquals( "First", l.get(0));
    }

    /**
     * Tests the clear functionality
     */
    @Test
    public void clear() {
        MyLinkedList<String> l = new MyLinkedList<>();
        for (int i = 0; i < 50; i++)
            l.add("el" + i);

        l.clear();
        assertTrue(l.isEmpty());
    }

    /**
     * Tests the iterator provided by the class
     */
    @Test
    public void iterator() {
        MyLinkedList<Integer> l = new MyLinkedList<>();
        for (int i=0; i < 300; i++)
            l.add(i);

        int i = 0;
        for (int n : l) {
            assertEquals(n, i);
            i++;
        }

        i = 298;
        ListIterator<Integer> it = l.listIterator(l.size());
        while (it.hasPrevious()) {
            // compiler doesn't like assertEquals here because that is callable both with Object or long as params
            assertEquals(i, (int) it.previous());
            i--;
        }
    }

    /**
     * Tests whether size() returns the correct number at all times
     */
    @Test
    public void size() {
        MyLinkedList<String> l = new MyLinkedList<>();
        assertEquals(l.size(), 0);
        l.add("First");
        assertEquals(l.size(), 1);
        l.remove(0);
        assertEquals(l.size(), 0);

        for (int i = 0; i < 50; i++)
            l.add("el" + i);

        assertEquals(l.size(), 50);
    }

    /**
     * Tests that calling next() and previous()
     */
    @Test
    public void nextPrevious() {
        MyLinkedList<String> l = new MyLinkedList<>();
        l.add("First");
        l.add("Second");

        ListIterator<String> it = l.listIterator(0);
        assertEquals("First", it.next());
        assertEquals("Second", it.next());
        assertEquals("First", it.previous());

    }

    /**
     * Tests that set / remove do the expected thing (cf javadoc of ListIterator) when iterating 'forward'
     */
    @Test
    public void setRemoveFW() {
        MyLinkedList<String> l = new MyLinkedList<>();
        l.add("First");
        l.add("Second");
        l.add("Third");

        ListIterator<String> it = l.listIterator(0);
        for (int i = 0; it.hasNext(); i++) {
            System.out.println(it.next());
            if (i == 1) {
                it.set("NewSecond");
            }
            if (i == 2) {
                it.remove();
            }
        }

        assertEquals(2, l.size());
        assertEquals("First", l.get(0));
        assertEquals("NewSecond", l.get(1));
    }

    /**
     * Tests that set / remove do the expected thing (cf javadoc of ListIterator) when iterating 'backward'
     *
     * As we defined the empty list (size 0) consisting of two elements we need to get the iterator form
     * size + 1 to be able to iterate backwards
     */
    @Test
    public void setRemoveBW() {
        MyLinkedList<String> l = new MyLinkedList<>();
        l.add("Uno");
        l.add("Dos");
        l.add("Tres");

        ListIterator<String> it = l.listIterator(l.size() + 1); // Because 0 is head; size + 1 is tail
        for (int i = l.size() - 1; it.hasPrevious(); i--) {
            System.out.println(it.previous());
            if (i == 1) {
                it.set("2");
            }
            if (i == 0) {
                it.remove();
            }
        }

        assertEquals(2, l.size());
        assertEquals("2", l.get(0));
        assertEquals("Tres", l.get(1));
    }
}