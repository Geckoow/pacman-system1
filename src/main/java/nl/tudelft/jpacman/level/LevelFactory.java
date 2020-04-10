package nl.tudelft.jpacman.level;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ai.BlinkyAi;
import nl.tudelft.jpacman.npc.ai.RandomAi;
import nl.tudelft.jpacman.npc.ghost.GhostColor;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.sprite.Sprite;

/**
 * Factory that creates levels and units.
 *
 * @author Jeroen Roosen 
 */
public class LevelFactory {

    private static final int GHOSTS = 4;
    private static final int BLINKY = 0;
    private static final int INKY = 1;
    private static final int PINKY = 2;
    private static final int CLYDE = 3;

    /**
     * The default value of a pellet.
     */
    private static final int PELLET_VALUE = 10;

    /**
     * The default value of a pellet.
     */
    private static final int FRUIT_VALUE = 100;

    /**
     * The default value of a powerpill.
     */
    private static final int POWERPILL_VALUE = 50;

    /**
     * The sprite store that provides sprites for units.
     */
    private final PacManSprites sprites;

    /**
     * Used to cycle through the various ghost types.
     */
    private int ghostIndex;

    /**
     * The factory providing ghosts.
     */
    private final GhostFactory ghostFact;

    /**
     * Creates a new level factory.
     *
     * @param spriteStore
     *            The sprite store providing the sprites for units.
     * @param ghostFactory
     *            The factory providing ghosts.
     */
    public LevelFactory(PacManSprites spriteStore, GhostFactory ghostFactory) {
        this.sprites = spriteStore;
        this.ghostIndex = -1;
        this.ghostFact = ghostFactory;
    }

    /**
     * Creates a new level from the provided data.
     *
     * @param board
     *            The board with all ghosts and pellets occupying their squares.
     * @param ghosts
     *            A list of all ghosts on the board.
     * @param startPositions
     *            A list of squares from which players may start the game.
     * @return A new level for the board.
     */
    public Level createLevel(Board board, List<Ghost> ghosts,
                             List<Square> startPositions) {

        // We'll adopt the simple collision map for now.
        CollisionMap collisionMap = new PlayerCollisions(ghosts, board);

        return new Level(board, ghosts, startPositions, collisionMap);
    }

    /**
     * Creates a new ghost.
     *
     * @return The new ghost.
     */
    Ghost createGhost() {
        ghostIndex++;
        ghostIndex %= GHOSTS;
        switch (ghostIndex) {
            case BLINKY:
                return ghostFact.createBlinky();
            case INKY:
                return ghostFact.createInky();
            case PINKY:
                return ghostFact.createPinky();
            case CLYDE:
                return ghostFact.createClyde();
            default:
                return new RandomGhost(sprites.getGhostSprite(GhostColor.RED), sprites.getFearedGhostSprite());
        }
    }

    /**
     * Creates a new pellet.
     *
     * @return The new pellet.
     */
    public Pellet createPellet() {
        return new Pellet(PELLET_VALUE, sprites.getPelletSprite());
    }

    /**
     * Creates a new fruit.
     *
     * @return The new fruit.
     */
    public Pellet createApple() {
        return new Pellet(FRUIT_VALUE, sprites.getAppleSprite());
    }

    /**
     * Creates a new fruit.
     *
     * @return The new fruit.
     */
    public Pellet createCherry() {
        return new Pellet(FRUIT_VALUE, sprites.getCherrySprite());
    }

    /**
     * Creates a new fruit.
     *
     * @return The new fruit.
     */
    public Pellet createMelon() {
        return new Pellet(FRUIT_VALUE, sprites.getMelonSprite());
    }

    /**
     * Creates a new fruit.
     *
     * @return The new fruit.
     */
    public Pellet createOrange() {
        return new Pellet(FRUIT_VALUE, sprites.getOrangeSprite());
    }

    /**
     * Creates a new fruit.
     *
     * @return The new fruit.
     */
    public Pellet createStrawberry() {
        return new Pellet(FRUIT_VALUE, sprites.getStrawberrySprite());
    }
    /**
     * Creates a new powerpill.
     *
     * @return The new powerpill.
     */

    public Pellet createFruit(){
        int rand = (int) (Math.random()*5);
        System.out.println(rand);
        switch (rand){
            case 0 :
                return createApple();
            case 1 :
                return createCherry();
            case 2 :
                return createMelon();
            case 3:
                return createOrange();
            case 4:
                return createStrawberry();
        }
        return createPellet();
    }
    public PowerPill createPowerPill() {
        return new PowerPill(POWERPILL_VALUE, sprites.getPowerPillSprite());
    }

    /**
     * Implementation of an NPC that wanders around randomly.
     *
     * @author Jeroen Roosen
     */
    private static final class RandomGhost extends Ghost {

        /**
         * The suggested delay between moves.
         */
        private static final long DELAY = 175L;

        /**
         * Creates a new random ghost.
         *
         * @param ghostSprite
         *            The sprite for the ghost.
         */
        RandomGhost(Map<Direction, Sprite> ghostSprite, Map<Direction, Sprite> ghostSprite2) {
            super(ghostSprite, ghostSprite2, (int) DELAY, 0);
            addAi(new RandomAi(this));
        }
        public void reverseScared(){
            setScared(false);
            addAi(new RandomAi(this));
            setSprites(getBasicSprites());
        }
    }
}
