package nl.tudelft.jpacman.ui;

import nl.tudelft.jpacman.LevelInformation;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.MapParser;

import java.util.ArrayList;

public class ClassicalGame extends ActualGame {

    public ClassicalGame(int nbActualGame, ArrayList<LevelInformation> listOfLevel, GameFactory gamefactory, MapParser mapParser) {
        super(nbActualGame, listOfLevel, gamefactory, mapParser);
    }

    @Override
    public boolean nextLevel() {
        incrementLevel();
        return this.makeGame();
    }

    /**
     * change the score of actual level.
     */
    public void changeScore(){
        int score = getActualGame().getSinglePlayer().getScore();
        if(score > getLevelList().get(getNbActualGame()).getPoint()) {
            getLevelList().get(getNbActualGame()).setPoint(score);
        }
    }
}
