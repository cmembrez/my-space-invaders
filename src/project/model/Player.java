package project.model;

import project.controller.SpaceInvaders;
import project.model.projectiles.Projectile;
import project.utils.MyLinkedList;
import project.utils.Point;
import project.utils.Vector;
import project.view.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Player is controlled by the user/keyboard,
 * can throw Shot, and can receive Booster.
 */
public class Player extends Character implements Movable, Damageable, Shotable<Projectile> {
    // VARIABLES
    private long timeOfLastShot = 0; // when player's last shot was fired
    private final int SHOT_RATE = 500;  // in ms
    private final int MAX_HEALTH = 100;

    private final Vector MOVE_RIGHT = new Vector(10, 0);  // nb of pixel per step to go right
    private final Vector MOVE_LEFT = new Vector(-10, 0);  // nb of pixel per step to go left
    private final Vector MOVE_RESET = new Vector(0, 0);  // used in KeyReleased() to avoid shuttering
    private Vector velocity = new Vector(0,0);

    // CONSTRUCTOR
    private Player(Image img, project.utils.Point coordinate) {
        super(img, coordinate);
        this.setCurrentHealth(MAX_HEALTH);
    }

    public static Player playerBeethoven(int x) throws IOException{
        String filename = "/player2_200.png";  // "/player2_small.png";
        int playerVerticalPosition = 475;
        project.utils.Point coordinate = new Point(x, playerVerticalPosition);
        Image imgLogo = ImageIO.read(Objects.requireNonNull(SpaceInvaders.class.getResource(filename)));
        return new Player(imgLogo, coordinate);
    }


    // Properties
    public Vector getMOVE_RIGHT() {
        return MOVE_RIGHT;
    }
    public Vector getMOVE_LEFT() {
        return MOVE_LEFT;
    }
    public Vector getMOVE_RESET() {
        return MOVE_RESET;
    }

    public Vector getVelocity() {
        return velocity;
    }
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    // Methods
    /**
     * Enemy can inflict damage to player by calling player.receiveDamage(someDamageAmount);
     * Player's currentHealth is reduced by this amount
     * @param damage an int representing the damage inflicted.
     */
    @Override
    public boolean receiveDamage(Sprite otherSprite, int damage) {
        // if ( this.isDead() ) throw new CharacterAlreadyDeadException(this.getName()+" is already dead. Take it easy!");
        if( this.isDead() ) return true;  // avoids unnecessary crash at the end of the game

        if (this.checkCollision(otherSprite)){
            this.setCurrentHealth(this.getCurrentHealth() - damage);
            return true;
        }
        return false;
    }

    @Override
    public void move(Vector delta) {
        this.getCoordinate().translate(delta);
    }

    @Override
    public MyLinkedList<Projectile> shot(MyLinkedList<Projectile> notes, Projectile note) {
        long elaspedTime = System.currentTimeMillis() - this.timeOfLastShot;
        if (elaspedTime > this.SHOT_RATE) {
            notes.add(note);
            this.timeOfLastShot = System.currentTimeMillis();
        }
        return notes;
    }
}
