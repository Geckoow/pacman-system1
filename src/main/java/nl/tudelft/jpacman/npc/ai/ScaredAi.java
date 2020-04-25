package nl.tudelft.jpacman.npc.ai;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.npc.ghost.Navigation;

import java.util.List;
import java.util.Optional;

public class ScaredAi implements Ai {

    private final Ghost ghost;

    public ScaredAi(Ghost ghost){
        this.ghost = ghost;
    }
    @Override
    public Optional<Direction> nextAiMove() {
        assert ghost.hasSquare();

        Unit nearest = Navigation.findNearest(Player.class, ghost.getSquare());
        if (nearest == null) {
            return Optional.empty();
        }
        assert nearest.hasSquare();
        Square target = nearest.getSquare();

        List<Direction> path = Navigation.shortestPath(ghost.getSquare(), target, ghost);
        if (path != null && !path.isEmpty()) {
            Direction direction = path.get(0);
            if (path.size() <= Ghost.getFEARNESS()) {
                return Optional.ofNullable(Ghost.getOPPOSITES().get(direction));
            }
            return Optional.of(direction);
        }
        return Optional.empty();
    }
}
