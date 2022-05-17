package project.view;

import java.awt.*;

public interface Representation {
    /**
     * Returns a bounding box which,
     * represents the border enclosing the Representation object.
     * @return a rectangle
     */
    Rectangle getBoundingBox();
}
