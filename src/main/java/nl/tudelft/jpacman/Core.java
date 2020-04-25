package nl.tudelft.jpacman;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.ui.PacManUI;

public class Core {
    private final CoreProduct coreProduct = new CoreProduct();

    public static final String DEFAULT_MAP = "/board.txt";
    private String levelMap = DEFAULT_MAP;

    private PacManUI pacManUI;
    private Game game;

    /**
     * @return The game object this launcher will start when
     * is called.
     */
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * The map file used to populate the level.
     *
     * @return The name of the map file.
     */
    protected String getLevelMap() {
        return levelMap;
    }

    /**
     * Set the name of the file containing this level's map.
     *
     * @param fileName Map to be used.
     * @return Level corresponding to the given map.
     */
    public Core withMapFile(String fileName) {
        levelMap = fileName;
        return this;
    }

    /**
     * @return A new map parser object using the factories from
     * {@link #getLevelFactory()} and {@link #getBoardFactory()}.
     */
    protected MapParser getMapParser() {
        return coreProduct.getMapParser();
    }

    /**
     * @return A new board factory using the sprite store from
     * {@link #getSpriteStore()}.
     */
    protected BoardFactory getBoardFactory() {
        return coreProduct.getBoardFactory();
    }

    /**
     * @return The default {@link PacManSprites}.
     */
    protected PacManSprites getSpriteStore() {
        return CoreProduct.getSPRITE_STORE();
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}
     * and the ghosts from {@link #getGhostFactory()}.
     */
    protected LevelFactory getLevelFactory() {
        return coreProduct.getLevelFactory();
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}.
     */
    protected GhostFactory getGhostFactory() {
        return coreProduct.getGhostFactory();
    }

    /**
     * @return A new factory using the players from {@link #getPlayerFactory()}.
     */
    protected GameFactory getGameFactory() {
        return coreProduct.getGameFactory();
    }

    /**
     * @return A new factory using the sprites from {@link #getSpriteStore()}.
     */
    protected PlayerFactory getPlayerFactory() {
        return coreProduct.getPlayerFactory();
    }

    public PacManUI getPacManUI() {
        return pacManUI;
    }

    public void setPacManUI(PacManUI pacManUI) {
        this.pacManUI = pacManUI;
    }
}
