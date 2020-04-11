package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * A pellet, one of the little dots Pac-Man has to collect.
 *
 * @author Jeroen Roosen 
 */
public class Pellet extends Unit {

    /**
     * The sprite of this unit.
     */
    private final Sprite image;

    /**
     * The point value of this pellet.
     */
    private final int value;

    /**
     * Creates a new pellet.
     * @param points The point value of this pellet.
     * @param sprite The sprite of this pellet.
     */
    public Pellet(int points, Sprite sprite) {
        this.image = sprite;
        this.value = points;
    }

    /**
     * Returns the point value of this pellet.
     * @return The point value of this pellet.
     */
    public int getValue() {
        return value;
    }

    @Override
    public Sprite getSprite() {
        return image;
    }

    public void collide(Unit collidedOn, PlayerCollisions playerCollisions) {
        pelletColliding(collidedOn);
    }

    /**
     * Actual case of player consuming a pellet.
     *  @param player The player involved in the collision.
     *
     */
    public void playerVersusPellet(Player player) {
        leaveSquare();
        player.addPoints(getValue());
    }

    public void pelletColliding(Unit collidedOn) {
        if (collidedOn instanceof Player) {
            playerVersusPellet((Player) collidedOn);
        }
    }
}
