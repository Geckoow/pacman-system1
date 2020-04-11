package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Square;

import java.util.List;

public class LevelRegisterPlayer {
    /**
     * The squares from which players can start this game.
     */
    private final List<Square> startSquares;
    /**
     * The start current selected starting square.
     */
    private int startSquareIndex;

    public LevelRegisterPlayer(List<Square> startPositions) {
        this.startSquares = startPositions;
    }

    public void setStartSquareIndex(int startSquareIndex) {
        this.startSquareIndex = startSquareIndex;
    }

    /**
     * Registers a player on this level, assigning him to a starting position. A
     * player can only be registered once, registering a player again will have
     * no effect.
     *
     * @param player The player to register.
     */
    public void registerPlayer(Player player, List<Player> thisPlayers) {
        assert player != null;
        assert !startSquares.isEmpty();

        if (thisPlayers.contains(player)) {
            return;
        }
        thisPlayers.add(player);
        Square square = startSquares.get(startSquareIndex);
        player.occupy(square);
        startSquareIndex++;
        startSquareIndex %= startSquares.size();
    }
}
