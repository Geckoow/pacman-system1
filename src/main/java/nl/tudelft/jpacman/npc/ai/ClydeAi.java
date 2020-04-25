package nl.tudelft.jpacman.npc.ai;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.Clyde;
import nl.tudelft.jpacman.npc.ghost.Navigation;

import java.util.List;
import java.util.Optional;

public class ClydeAi implements Ai {
    private final Clyde clyde;

    public ClydeAi(Clyde clyde) {
        this.clyde = clyde;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Clyde has two basic AIs, one for when he's far from Pac-Man, and one for
     * when he is near to Pac-Man.
     * When Clyde is far away from Pac-Man (beyond eight grid spaces),
     * Clyde behaves very much like Blinky, trying to move to Pac-Man's exact
     * location. However, when Clyde gets within eight grid spaces of Pac-Man,
     * he automatically changes his behavior and runs away
     * </p>
     */
    @Override
    public Optional<Direction> nextAiMove() {
        assert clyde.hasSquare();

        Unit nearest = Navigation.findNearest(Player.class, clyde.getSquare());
        if (nearest == null) {
            return Optional.empty();
        }
        assert nearest.hasSquare();
        Square target = nearest.getSquare();

        List<Direction> path = Navigation.shortestPath(clyde.getSquare(), target, clyde);
        if (path != null && !path.isEmpty()) {
            Direction direction = path.get(0);
            if (path.size() <= Clyde.getSHYNESS()) {
                return Optional.ofNullable(Ghost.getOPPOSITES().get(direction));
            }
            return Optional.of(direction);
        }
        return Optional.empty();
    }
}
