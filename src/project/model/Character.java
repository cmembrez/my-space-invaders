package project.model;

import project.utils.Point;
import project.view.Sprite;

import java.awt.*;

public abstract class Character extends Sprite {
    ///////////////////////////////////////////////
    //                Fields                     //
    ///////////////////////////////////////////////
    private String name;
    private int currentHealth;


    ///////////////////////////////////////////////
    //              Constructor                  //
    ///////////////////////////////////////////////
    public Character(Image img, Point coordinate) {
        super(img, coordinate);
    }


    ///////////////////////////////////////////////
    //                Methods                    //
    ///////////////////////////////////////////////
    /**
     * Returns true if the current health of this Character is zero or less
     * @return boolean, true if character is dead
     */
    public boolean isDead(){
        return currentHealth <= 0;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    ///////////////////////////////////////////////
    //                Exception                  //
    ///////////////////////////////////////////////
    public static class CharacterAlreadyDeadException extends Exception{
        public CharacterAlreadyDeadException(String message){
            super(message);
        }
    }
}
