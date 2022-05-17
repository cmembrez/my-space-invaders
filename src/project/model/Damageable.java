package project.model;

import project.view.Sprite;

/**
 * Can receive damage/be damaged.
 */
public interface Damageable {
    boolean receiveDamage(Sprite otherSprite, int damage) throws Character.CharacterAlreadyDeadException;
}
