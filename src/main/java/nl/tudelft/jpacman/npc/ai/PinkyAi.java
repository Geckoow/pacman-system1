package nl.tudelft.jpacman.npc.ai;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Navigation;
import nl.tudelft.jpacman.npc.ghost.Pinky;

import java.util.List;
import java.util.Optional;

public class PinkyAi implements Ai {
    public final Pinky pinky;

    public PinkyAi(Pinky pinky){
        this.pinky = pinky;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * When the ghosts are not patrolling their home corners, Pinky wants to go
     * to the place that is four grid spaces ahead of Pac-Man in the direction
     * that Pac-Man is facing. If Pac-Man is facing down, Pinky wants to go to
     * the location exactly four spaces below Pac-Man. Moving towards this place
     * uses the same logic that Blinky uses to find Pac-Man's exact location.
     * Pinky is affected by a targeting bug if Pac-Man is facing up - when he
     * moves or faces up, Pinky tries moving towards a point up, and left, four
     * spaces.
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert pinky.hasSquare();

        Unit player = Navigation.findNearest(Player.class, pinky.getSquare());
        if (player == null) {
            return Optional.empty();
        }
        assert player.hasSquare();
        Square destination = player.squaresAheadOf(Pinky.getSquaresAhead());

        List<Direction> path = Navigation.shortestPath(pinky.getSquare(), destination, pinky);
        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }
}
