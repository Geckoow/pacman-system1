package nl.tudelft.jpacman;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.npc.ghost.GhostFactory;
import nl.tudelft.jpacman.sprite.PacManSprites;

public class CoreProduct {
    private static final PacManSprites SPRITE_STORE = new PacManSprites();

    public static PacManSprites getSPRITE_STORE() {
        return SPRITE_STORE;
    }

    /**
     * @return A new factory using the sprites from
     * and the ghosts from {@link #getGhostFactory()}.
     */
    public LevelFactory getLevelFactory() {
        return new LevelFactory(SPRITE_STORE, getGhostFactory());
    }

    /**
     * @return A new factory using the sprites from .
     */
    public GhostFactory getGhostFactory() {
        return new GhostFactory(SPRITE_STORE);
    }

    /**
     * @return A new board factory using the sprite store from
     * .
     */
    public BoardFactory getBoardFactory() {
        return new BoardFactory(SPRITE_STORE);
    }

    /**
     * @return A new factory using the sprites from .
     */
    public PlayerFactory getPlayerFactory() {
        return new PlayerFactory(SPRITE_STORE);
    }

    /**
     * @return A new map parser object using the factories from
     * {@link #getLevelFactory()} and {@link #getBoardFactory()}.
     */
    public MapParser getMapParser() {
        return new MapParser(getLevelFactory(), getBoardFactory());
    }

    /**
     * @return A new factory using the players from {@link #getPlayerFactory()}.
     */
    public GameFactory getGameFactory() {
        return new GameFactory(getPlayerFactory());
    }
}
