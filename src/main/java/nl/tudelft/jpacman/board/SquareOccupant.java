package nl.tudelft.jpacman.board;

import java.util.List;

public class SquareOccupant {
    private final Occupation occupation;
    private final Square square;

    public SquareOccupant(Square square) {
        this.occupation = new Occupation();
        this.square = square;
    }

    /**
     * Returns an immutable list of units occupying this square, in the order in
     * which they occupied this square (i.e. oldest first.)
     *
     * @return An immutable list of units occupying this square, in the order in
     * which they occupied this square (i.e. oldest first.)
     */
    public List<Unit> getOccupants() {
        return occupation.getOccupants();
    }

    /**
     * Adds a new occupant to this square.
     *
     * @param occupant The unit to occupy this square.
     */
    public void put(Unit occupant) {
        occupation.put(occupant);
    }

    /**
     * Removes the unit from this square if it was present.
     *
     * @param occupant The unit to be removed from this square.
     */
    public void remove(Unit occupant) {
        occupation.remove(occupant);
    }

    /**
     * Verifies that all occupants on this square have indeed listed this square
     * as the square they are currently occupying.
     *
     * @return <code>true</code> iff all occupants of this square have this
     * square listed as the square they are currently occupying.
     */
    public final boolean invariant() {
        for (Unit occupant : occupation.getOccupants2()) {
            if (occupant.hasSquare() && occupant.getSquare() != this.square) {
                return false;
            }
        }
        return true;
    }
}
