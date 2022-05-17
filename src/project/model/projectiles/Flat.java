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
 * Flat is the projectile that can be thrown by the enemy at the player
 * (as in, a bemol, the "opposite" of a sharp/dièse)
 */
public class Flat extends Projectile implements Movable {
    // VARIABLE
    private final static int DAMAGE = 10;
    private final static int SPEED = 3;

    private final Vector MOVE_DOWN = new Vector(0, SPEED);  // nb of pixel per step to move up// nb of pixel per step

    // CONSTRUCTOR
    private Flat(Image img, Point coordinate, int damage, int speed) {
        super(img, coordinate, damage, speed);
    }

    public static Flat bemol(Point coordinate) throws IOException {
        String filename = "/flat_small2.png";
        Image imgLogo = ImageIO.read(Objects.requireNonNull(SpaceInvaders.class.getResource(filename)));
        return new Flat(imgLogo, coordinate, DAMAGE, SPEED);
    }

    // METHODS
    public int getDamage(){
        return DAMAGE;
    }

    public int getSpeed(){
        return SPEED;
    }

    public Vector getMOVE_DOWN() {
        return MOVE_DOWN;
    }

    @Override
    public void move(Vector delta) {
        this.getCoordinate().translate(delta);
    }

    @Override
    public Vector getMOVE_UP() {
        return null;
    }
}
