package project.model.projectiles;

import project.utils.Point;
import project.utils.Vector;
import project.view.Sprite;

import java.awt.*;

public abstract class Projectile extends Sprite {
    // Variable
    private int damage;
    private int speed;

    // Constructor
    public Projectile(Image img, Point coordinate, int damage, int speed) {
        super(img, coordinate);
        this.damage = damage;
        this.speed = speed;
    }

    // Getter Setter
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public abstract void move(Vector delta);

    public abstract Vector getMOVE_UP();

    public abstract Vector getMOVE_DOWN();
}
