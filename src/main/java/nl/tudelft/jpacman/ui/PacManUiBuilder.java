package nl.tudelft.jpacman.ui;

import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.ui.ScorePanel.ScoreFormatter;

/**
 * Builder for the JPac-Man UI.
 *
 * @author Jeroen Roosen 
 */
public class PacManUiBuilder {



    /**
     * Way to format the score.
     */
    private ScoreFormatter scoreFormatter = null;

    /**
     * Creates a new Pac-Man UI builder without any mapped keys or buttons.
     */
    public PacManUiBuilder() {

    }

    /**
     * Creates a new Pac-Man UI with the set keys and buttons.
     *
     *            The game to build the UI for.
     * @return A new Pac-Man UI with the set keys and buttons.
     */
    public PacManUI build(GameFactory gameFactory, MapParser mapParser) {

        return new PacManUI(scoreFormatter, gameFactory, mapParser);
    }


    /**
     * Provide formatter for the score.
     *
     * @param scoreFormatter
     *         The score formatter to be used.
     *
     * @return The builder.
     */
    public PacManUiBuilder withScoreFormatter(ScoreFormatter scoreFormatter) {
        this.scoreFormatter = scoreFormatter;
        return this;
    }
}
