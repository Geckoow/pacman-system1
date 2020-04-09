package nl.tudelft.jpacman.npc.ai;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomAi implements Ai {
    public final Ghost ghost;
    public RandomAi(Ghost ghost){
        this.ghost = ghost;
    }
    @Override
    public Optional<Direction> nextAiMove() {
        return Optional.empty();
    }
    public Direction randomMove() {
        Square square = ghost.getSquare();
        List<Direction> directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (square.getSquareAt(direction).isAccessibleTo(ghost)) {
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
