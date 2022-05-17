package project.model;

import project.controller.SpaceInvaders;
import project.model.projectiles.Projectile;
import project.utils.MyLinkedList;
import project.model.projectiles.Flat;
import project.utils.Point;
import project.utils.Vector;
import project.view.Sprite;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

/**
 * Enemy is the object that the Player needs to eliminate
 */
public class Enemy extends Character implements Movable, Damageable, Shotable<Projectile> {
    // VARIABLES
    private final int SHOT_PROBABILITY_ONE_OVER = 500; // a 0.5% prob is => 0.5% == 0.005  == 5 / 1000 == 1/200 ... pfiou
    private final int MAX_HEALTH = 100;
    private final int BASE_SPEED = 1;
    private final int BASE_STEP_DOWN = 2;

    private int moveSpeed = BASE_SPEED;

    private int stepDown = BASE_STEP_DOWN;

    private Vector moveRight = new Vector(moveSpeed, 0);  // nb of pixel per step to go right
    private Vector moveLeft = new Vector(-moveSpeed, 0);  // nb of pixel per step to go left
    private final Vector MOVE_RESET = new Vector(0, 0);  // used in KeyReleased() to avoid shuttering

    private Vector moveDown = new Vector(0, stepDown);  // nb of pixel per step to go down
    private Vector velocity = new Vector(0,0);

    // CONSTRUCTOR
    private Enemy(Image img, project.utils.Point coordinate, int deltaChange) {
        super(img, coordinate);
        this.setCurrentHealth(MAX_HEALTH);
        // speed - horizontal move
        this.setMoveSpeed(BASE_SPEED + deltaChange);
        this.setMoveLeft(this.moveSpeed);
        this.setMoveRight(this.moveSpeed);
        // step - vertical move
        this.setStepDown(BASE_STEP_DOWN * deltaChange);
        this.setMoveDown(this.stepDown);
    }

    public static Enemy enemyAlienMultiEyes(project.utils.Point coordinate, int difficultyLevel, int gameLevel) throws IOException {
        String filename = "/alienShip1.png";
        Image imgLogo = ImageIO.read(Objects.requireNonNull(SpaceInvaders.class.getResource(filename)));
        return new Enemy(imgLogo, coordinate, difficultyLevel + gameLevel);
    }

    // Properties
    public void setMoveRight(int newSpeed) {
        this.moveRight = new Vector(newSpeed, 0);
    }

    public void setMoveLeft(int newSpeed) {
        this.moveLeft = new Vector(-newSpeed, 0);;
    }
    public void setMoveDown(int newSpeed) {
        this.moveDown = new Vector(0, newSpeed);;
    }
    public int getStepDown() {
        return stepDown;
    }

    public void setStepDown(int stepDown) {
        this.stepDown = stepDown;
    }
    public int getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
    public Vector getMoveRight() {
        return moveRight;
    }

    public Vector getMoveLeft() {
        return moveLeft;
    }

    public Vector getMOVE_RESET() {
        return MOVE_RESET;
    }

    public Vector getMoveDown() {return moveDown;}

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }


    // METHODS
    @Override
    public boolean receiveDamage(Sprite otherSprite, int damage) throws CharacterAlreadyDeadException {
        if ( this.isDead() ) throw new CharacterAlreadyDeadException(this.getName()+" is already dead. Take it easy!");

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
    public MyLinkedList<Projectile> shot(MyLinkedList<Projectile> flats, Projectile flat) {
        boolean probNextShot = new Random().nextInt(SHOT_PROBABILITY_ONE_OVER)==0;   // 0.5% == 0.005  == 5 / 1000 == 1/200 ... pfiou
        if ( probNextShot ){
            int newX = (int) this.getCoordinate().x() + this.get_width()/2;
            try {
                Projectile newEnemyShot = Flat.bemol(new Point(newX, this.getCoordinate().y()-5));
                flats.add(newEnemyShot);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // System.out.println("boum boum boum");
        }
        return flats;
    }
}