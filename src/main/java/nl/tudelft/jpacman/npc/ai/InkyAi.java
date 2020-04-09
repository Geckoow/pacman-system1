package nl.tudelft.jpacman.npc.ai;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.ghost.Blinky;
import nl.tudelft.jpacman.npc.ghost.Inky;
import nl.tudelft.jpacman.npc.ghost.Navigation;

import java.util.List;
import java.util.Optional;

public class InkyAi implements Ai{
    public final Inky inky;

    public InkyAi(Inky inky){
        this.inky = inky;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Inky has the most complicated AI of all. Inky considers two things: Blinky's
     * location, and the location two grid spaces ahead of Pac-Man. Inky
     * draws a line from Blinky to the spot that is two squares in front of
     * Pac-Man and extends that line twice as far. Therefore, if Inky is
     * alongside Blinky when they are behind Pac-Man, Inky will usually
     * follow Blinky the whole time. But if Inky is in front of Pac-Man when
     * Blinky is far behind him, Inky tends to want to move away from Pac-Man
     * (in reality, to a point very far ahead of Pac-Man). Inky is affected
     * by a similar targeting bug that affects Speedy. When Pac-Man is moving or
     * facing up, the spot Inky uses to draw the line is two squares above
     * and left of Pac-Man.
     * </p>
     *
     * <p>
     * <b>Implementation:</b>
     * To actually implement this in jpacman we have the following approximation:
     * first determine the square of Blinky (A) and the square 2
     * squares away from Pac-Man (B). Then determine the shortest path from A to
     * B regardless of terrain and walk that same path from B. This is the
     * destination.
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert inky.hasSquare();
        Unit blinky = Navigation.findNearest(Blinky.class, inky.getSquare());
        Unit player = Navigation.findNearest(Player.class, inky.getSquare());

        if (blinky == null || player == null) {
            return Optional.empty();
        }

        assert player.hasSquare();
        Square playerDestination = player.squaresAheadOf(inky.getSquaresAhead());

        List<Direction> firstHalf = Navigation.shortestPath(blinky.getSquare(),
            playerDestination, null);

        if (firstHalf == null) {
            return Optional.empty();
        }

        Square destination = followPath(firstHalf, playerDestination);
        List<Direction> path = Navigation.shortestPath(inky.getSquare(),
            destination, inky);

        if (path != null && !path.isEmpty()) {
            return Optional.ofNullable(path.get(0));
        }
        return Optional.empty();
    }
    private Square followPath(List<Direction> directions, Square start) {
        Square destination = start;

        for (Direction d : directions) {
            destination = destination.getSquareAt(d);
        }

        return destination;
    }
}
