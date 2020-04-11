package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Square;

import java.util.ArrayList;
import java.util.List;

public class LevelPlayerInfo {
    private LevelRegisterPlayer levelRegisterPlayer;
    /**
     * The players on this level.
     */
    private final List<Player> players;

    public LevelPlayerInfo(List<Square> startPositions) {
        this.levelRegisterPlayer = new LevelRegisterPlayer(startPositions);
        this.players = new ArrayList<>();
    }

    public void setStartSquareIndex(int startSquareIndex) {
        levelRegisterPlayer.setStartSquareIndex(startSquareIndex);
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Registers a player on this level, assigning him to a starting position. A
     * player can only be registered once, registering a player again will have
     * no effect.
     *
     * @param player The player to register.
     */
    public void registerPlayer(Player player) {
        levelRegisterPlayer.registerPlayer(player, this.players);
    }

    /**
     * Returns <code>true</code> iff at least one of the players in this level
     * is alive.
     *
     * @return <code>true</code> if at least one of the registered players is
     * alive.
     */
    public boolean isAnyPlayerAlive() {
        for (Player player : players) {
            if (player.isAlive()) {
                return true;
            }
        }
        return false;
    }
}
