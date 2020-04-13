package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ai.BlinkyAi;
import nl.tudelft.jpacman.npc.ai.ClydeAi;
import nl.tudelft.jpacman.npc.ai.InkyAi;
import nl.tudelft.jpacman.npc.ai.PinkyAi;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.Map;

/**
 * Factory that creates ghosts.
 *
 * @author Jeroen Roosen 
 */
public class GhostFactory {

    /**
     * The sprite store containing the ghost sprites.
     */
    private final PacManSprites sprites;

    private final Map<Direction, Sprite> fearSprite;
    /**
     * Creates a new ghost factory.
     *
     * @param spriteStore The sprite provider.
     */
    public GhostFactory(PacManSprites spriteStore) {
        this.sprites = spriteStore;
        this.fearSprite = sprites.getFearedGhostSprite();
    }

    /**
     * Creates a new Blinky / Shadow, the red Ghost.
     *
     * @see Blinky
     * @return A new Blinky.
     */
    public Ghost createBlinky() {
        Blinky blinky = new Blinky(sprites.getGhostSprite(GhostColor.RED), fearSprite);
        blinky.addAi();
        return blinky;
    }

    /**
     * Creates a new Pinky / Speedy, the pink Ghost.
     *
     * @see Pinky
     * @return A new Pinky.
     */
    public Ghost createPinky() {
        Pinky pinky = new Pinky(sprites.getGhostSprite(GhostColor.PINK), fearSprite);
        pinky.addAi();
        return pinky;
    }

    /**
     * Creates a new Inky / Bashful, the cyan Ghost.
     *
     * @see Inky
     * @return A new Inky.
     */
    public Ghost createInky() {
        Inky inky = new Inky(sprites.getGhostSprite(GhostColor.CYAN), fearSprite);
        inky.addAi();
        return inky;
    }

    /**
     * Creates a new Clyde / Pokey, the orange Ghost.
     *
     * @see Clyde
     * @return A new Clyde.
     */
    public Ghost createClyde() {
        Clyde clyde = new Clyde(sprites.getGhostSprite(GhostColor.ORANGE), fearSprite);
        clyde.addAi();
        return clyde;
    }

    public PacManSprites getSprites() {
        return sprites;
    }
}
