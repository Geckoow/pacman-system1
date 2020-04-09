package nl.tudelft.jpacman.npc;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.ai.Ai;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;

import java.util.*;

/**
 * A non-player unit.
 *
 * @author Jeroen Roosen
 */
public abstract class Ghost extends Unit {
    /**
     * The sprite map, one sprite for each direction.
     */
    private final Map<Direction, Sprite> basicSprites;

    /**
     * The sprite map, one sprite for each direction.
     */
    private final Map<Direction, Sprite> fearSprites;

    private Map<Direction, Sprite> sprites;

    /**
     * The base move interval of the ghost.
     */
    private final int moveInterval;

    /**
     * The random variation added to the {@link #moveInterval}.
     */
    private final int intervalVariation;

    /**
     * The amount of cells feared ghost wants to stay away from Pac Man.
     */
    private static final int FEARNESS = 12;

    /**
     * A map of opposite directions.
     */
    private static final Map<Direction, Direction> OPPOSITES = new EnumMap<>(Direction.class);

    static {
        OPPOSITES.put(Direction.NORTH, Direction.SOUTH);
        OPPOSITES.put(Direction.SOUTH, Direction.NORTH);
        OPPOSITES.put(Direction.WEST, Direction.EAST);
        OPPOSITES.put(Direction.EAST, Direction.WEST);
    }

    private boolean scared;

    protected Ai ai;

    private String name;
    /**
     * Calculates the next move for this unit and returns the direction to move
     * in.
     * <p>
     * Precondition: The NPC occupies a square (hasSquare() holds).
     *
     * @return The direction to move in, or <code>null</code> if no move could
     * be devised.
     */
    public Direction nextMove() {
        return ai.nextAiMove().orElseGet(this::randomMove);
    }

    /**
     * Creates a new ghost.
     *
     * @param spriteMap         The sprites for every direction.
     * @param moveInterval      The base interval of movement.
     * @param intervalVariation The variation of the interval.
     */
    protected Ghost(Map<Direction, Sprite> spriteMap, Map<Direction, Sprite> spriteMap2, int moveInterval, int intervalVariation) {
        this.basicSprites = spriteMap;
        this.fearSprites = spriteMap2;
        this.sprites = spriteMap;
        this.intervalVariation = intervalVariation;
        this.moveInterval = moveInterval;
        this.scared = false;
        this.name = "ghost";
    }
    public void addAi(Ai ai){
        this.ai = ai;
    }

    public void switchSprite(){
        if(this.scared == true)
            this.sprites = fearSprites;
        else
            this.sprites = basicSprites;
    }

    public boolean isScared() {
        return scared;
    }

    public void setScared(boolean scared) {
        this.scared = scared;
    }

    @Override
    public Sprite getSprite() {
        return sprites.get(getDirection());
    }

    /**
     * The time that should be taken between moves.
     *
     * @return The suggested delay between moves in milliseconds.
     */
    public long getInterval() {
        return this.moveInterval + new Random().nextInt(this.intervalVariation);
    }

    public static int getFEARNESS() {
        return FEARNESS;
    }

    public static Map<Direction, Direction> getOPPOSITES() {
        return OPPOSITES;
    }

    public String getName() {
        return name;
    }

    /**
     * Determines a possible move in a random direction.
     *
     * @return A direction in which the ghost can move, or <code>null</code> if
     * the ghost is shut in by inaccessible squares.
     */

    protected Direction randomMove() {
        Square square = getSquare();
        List<Direction> directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (square.getSquareAt(direction).isAccessibleTo(this)) {
                directions.add(direction);
            }
        }
        if (directions.isEmpty()) {
            return null;
        }
        int i = new Random().nextInt(directions.size());
        return directions.get(i);
    }
}
