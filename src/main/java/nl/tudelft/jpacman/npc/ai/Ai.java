package nl.tudelft.jpacman.npc.ai;

import nl.tudelft.jpacman.board.Direction;

import java.util.Optional;

public interface Ai {
    /**
     * Tries to calculate a move based on the behaviour of the npc.
     *
     * @return an optional containing the move or empty if the current state of the game
     * makes the ai move impossible
     */
    Optional<Direction> nextAiMove();
}

