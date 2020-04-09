package nl.tudelft.jpacman.npc.ai;

import nl.tudelft.jpacman.board.Direction;

import java.util.Optional;

public interface Ai {
    public Optional<Direction> nextAiMove();
    //public Direction randomMove();
}

