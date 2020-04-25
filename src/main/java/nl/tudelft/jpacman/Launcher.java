package nl.tudelft.jpacman;

import java.io.IOException;
import nl.tudelft.jpacman.ui.PacManUiBuilder;

/**
 * Creates and launches the JPacMan UI.
 * 
 * @author Jeroen Roosen
 */
@SuppressWarnings("PMD.TooManyMethods")
public class Launcher extends Core{




    /**
     * Creates and starts a JPac-Man game.
     */
    public void launch() {
        PacManUiBuilder builder = new PacManUiBuilder();
        setPacManUI(builder.build(getGameFactory(), getMapParser()));
        getPacManUI().start();
    }

    /**
     * Disposes of the UI. For more information see
     * {@link javax.swing.JFrame#dispose()}.
     *
     * Precondition: The game was launched first.
     */
    public void dispose() {
        assert getPacManUI() != null;
        getPacManUI().dispose();
    }

    /**
     * Main execution method for the Launcher.
     *
     * @param args
     *            The command line arguments - which are ignored.
     */
    public static void main(String[] args) {
        new Launcher().launch();
    }
}
