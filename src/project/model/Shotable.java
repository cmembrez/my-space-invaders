package project.model;

import project.utils.MyLinkedList;

/**
 * Can shoot/throw/attack with something
 */
public interface Shotable<E> {
    MyLinkedList<E> shot(MyLinkedList<E> projectiles, E projectile);
}
