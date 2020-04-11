package nl.tudelft.jpacman.board;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Occupation {
    /**
     * The units occupying this square, in order of appearance.
     */
    private final List<Unit> occupants;

    public Occupation() {
        this.occupants = new ArrayList<>();
    }

    public List<Unit> getOccupants2() {
        return occupants;
    }

    /**
     * Returns an immutable list of units occupying this square, in the order in
     * which they occupied this square (i.e. oldest first.)
     *
     * @return An immutable list of units occupying this square, in the order in
     * which they occupied this square (i.e. oldest first.)
     */
    public List<Unit> getOccupants() {
        return ImmutableList.copyOf(occupants);
    }

    /**
     * Adds a new occupant to this square.
     *
     * @param occupant The unit to occupy this square.
     */
    public void put(Unit occupant) {
        assert occupant != null;
        assert !occupants.contains(occupant);

        occupants.add(occupant);
    }

    /**
     * Removes the unit from this square if it was present.
     *
     * @param occupant The unit to be removed from this square.
     */
    public void remove(Unit occupant) {
        assert occupant != null;
        occupants.remove(occupant);
    }
}
