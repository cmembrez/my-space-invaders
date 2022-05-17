package project.model;

import oop.lib.Paintable;
import oop.lib.Painting;

import project.model.projectiles.Projectile;
import project.utils.MyLinkedList;
import project.utils.Vector;
import project.view.Sprite;

import java.util.Collections;

public class Group<E extends Enemy> extends MyLinkedList<E> implements Movable, Damageable, Shotable<Projectile>, Paintable {
    //////////////////////////////////////////////////////////////////////
    // Constructor
    //////////////////////////////////////////////////////////////////////
    public Group() {
        super();
    }


    //////////////////////////////////////////////////////////////////////
    // Methods
    //////////////////////////////////////////////////////////////////////
    /**
     * Return the maximum x coordinate of the elements in the group
     * @return the maximum x coordinate
     */
    public int getMax(){
        MyLinkedList<Integer> xCoordinates = new MyLinkedList<>();
        for( E value : this){
            xCoordinates.add( (int) value.getCoordinate().x() );
        }

        return Collections.max(xCoordinates);
    }
    /**
     * Return the minimum x coordinate of the elements in the group
     * @return the minimum x coordinate
     */
    public int getMin(){
        MyLinkedList<Integer> xCoordinates = new MyLinkedList<>();
        for( E value : this){
            xCoordinates.add( (int) value.getCoordinate().x() );
        }

        return Collections.min(xCoordinates);
    }

    /**
     * Return the width of the first element (index 0) if group not empty, else -1.
     * @return width of first element if group not empty, otherwise -1.
     */
    public int getWidth(){
        if( this.isEmpty() ) return 0;
        return this.get(0).get_width();
    }

    /**
     * Return the height of the first element (index 0) if group not empty, else -1.
     * @return height of first element if group not empty, otherwise -1.
     */
    public int getHeight(){
        if( this.isEmpty() ) return 0;
        return this.get(0).get_height();
    }

    /**
     * Detect if an enemy in the group is dead, and remove it.
     */
    public void detectAndRemoveDeadEnemies(){
        MyLinkedList<Enemy> enemiesToRemove = new MyLinkedList<>();
        for(E e : this){
            if( e.isDead() ){
                enemiesToRemove.add(e);
            }
        }
        removeAll(enemiesToRemove);
    }


    @Override
    public void paint(Painting painting) {
        for( E e : this ){
            e.paint(painting);
        }
    }

    @Override
    public boolean receiveDamage(Sprite otherSprite, int damage) throws Character.CharacterAlreadyDeadException {
        for(E e: this){
            if (e.receiveDamage(otherSprite, damage)){
                remove(e);
                return true;
            }
        }
        return false;
    }

    @Override
    public void move(Vector delta) {
        for(E e : this){
            e.move(delta);
        }
    }

    public void setVelocity(Vector velocity) {
        for(E e : this) {
            e.setVelocity(velocity);
        }
    }

    public Vector getVelocity(){
        return this.get(0).getVelocity();
    }

    @Override
    public MyLinkedList<Projectile> shot(MyLinkedList<Projectile> projectiles, Projectile projectile) {
        for( E e : this ){
            projectiles.addAll(e.shot(projectiles, projectile));
        }
        return projectiles;
    }
}
