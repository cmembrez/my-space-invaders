package project.model.projectiles;

import project.controller.SpaceInvaders;
import project.model.Movable;
import project.utils.Point;
import project.utils.Vector;


import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Note is the projectile that can be thrown by the player at the enemy
 * (as in, a music note)
 */
public class Note extends Projectile implements Movable {
    // VARIABLE
    private final static int DAMAGE = 50;
    private final static int SPEED = 5;  // nb of pixel per step


    private final Vector MOVE_UP = new Vector(0, SPEED * -1);  // nb of pixel per step to move up

    // CONSTRUCTOR
    private Note(Image img, Point coordinate, int damage, int speed) {
        super(img, coordinate, damage, speed);
    }

    public static Note shotQuaver(Point coordinate) throws IOException {
        String filename = "/semiQuaver2_small.png";
        Image imgLogo = ImageIO.read(Objects.requireNonNull(SpaceInvaders.class.getResource(filename)));
        return new Note(imgLogo, coordinate, DAMAGE, SPEED);
    }

    // METHODS

    /**
     * Returns the amount of damage inflicted by this object
     * @return int
     */
    public int getDamage(){
        return DAMAGE;
    }

    public int getSpeed(){
        return SPEED;
    }

    public Vector getMOVE_UP() {
        return MOVE_UP;
    }

    @Override
    public Vector getMOVE_DOWN() {
        return null;
    }

    @Override
    public void move(Vector delta) {
        this.getCoordinate().translate(delta);
    }
}
