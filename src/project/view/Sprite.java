package project.view;

import oop.lib.Paintable;
import oop.lib.Painting;
import project.utils.Point;

import java.awt.*;

/**
 * Stores and paints the representation of an entity, e.g. an image.
 */
public class Sprite implements Paintable, Representation {

    //////////////////////////////////////////////////////////////////////
    // VARIABLE
    //////////////////////////////////////////////////////////////////////
    private project.utils.Point coordinate;  // x,y coordinates
    private Image img;

    //////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    //////////////////////////////////////////////////////////////////////
    public Sprite (Image img, project.utils.Point coordinate){
        this.img = img;
        this.coordinate = coordinate;
    }

    //////////////////////////////////////////////////////////////////////
    // PUBLIC
    //////////////////////////////////////////////////////////////////////
    @Override
    public void paint(Painting painting) {
        painting.drawImage(this.img, this.coordinate.asArray());
    }

    protected boolean checkCollision(Sprite otherSprite){
        return this.getBoundingBox().intersects(otherSprite.getBoundingBox());
    }

    //////////////////////////////////////////////////////////////////////
    // GETTERS & SETTERS
    //////////////////////////////////////////////////////////////////////
    public project.utils.Point getCoordinate(){
        return this.coordinate;
    }
    public void setCoordinate(Point coordinate){
        this.coordinate = coordinate;
    }

    public int get_width(){
        return img.getWidth(null);
    }
    public int get_height() {
        return img.getWidth(null);
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle((int) this.coordinate.x(), (int) this.coordinate.y(),
                this.get_width(), this.get_height());
    }
}
