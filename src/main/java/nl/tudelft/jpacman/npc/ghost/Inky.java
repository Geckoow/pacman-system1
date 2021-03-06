package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ai.InkyAi;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.Map;

/**
 * <p>
 * An implementation of the classic Pac-Man ghost Inky.
 * </p>
 * <b>AI:</b> Inky has the most complicated AI of all. Inky considers two things: Blinky's
 * location, and the location two grid spaces ahead of Pac-Man. Inky draws a
 * line from Blinky to the spot that is two squares in front of Pac-Man and
 * extends that line twice as far. Therefore, if Inky is alongside Blinky
 * when they are behind Pac-Man, Inky will usually follow Blinky the whole
 * time. But if Inky is in front of Pac-Man when Blinky is far behind him,
 * Inky tends to want to move away from Pac-Man (in reality, to a point very
 * far ahead of Pac-Man). Inky is affected by a similar targeting bug that
 * affects Speedy. When Pac-Man is moving or facing up, the spot Inky uses to
 * draw the line is two squares above and left of Pac-Man.
 * <p>
 * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
 * </p>
 *
 * @author Jeroen Roosen
 */
public class Inky extends Ghost {

    private static final int SQUARES_AHEAD = 2;

    /**
     * The variation in intervals, this makes the ghosts look more dynamic and
     * less predictable.
     */
    private static final int INTERVAL_VARIATION = 50;

    /**
     * The base movement interval.
     */
    private static final int MOVE_INTERVAL = 250;

    public static int getSquaresAhead() {
        return SQUARES_AHEAD;
    }

    private final String name;
    /**
     * Creates a new "Inky".
     *
     * @param spriteMap The sprites for this ghost.
     */
    public Inky(Map<Direction, Sprite> spriteMap, Map<Direction, Sprite> spriteMap2) {
        super(spriteMap, spriteMap2, MOVE_INTERVAL, INTERVAL_VARIATION);
        this.name = "Inky";
    }

    @Override
    public void addAi() {
        this.ai = new InkyAi(this);
    }



}
