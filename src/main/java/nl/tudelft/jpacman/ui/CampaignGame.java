package nl.tudelft.jpacman.ui;

import nl.tudelft.jpacman.LevelInformation;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.level.MapParser;

import java.util.ArrayList;

public class CampaignGame extends ActualGame {

    public CampaignGame(int nbActualGame, ArrayList<LevelInformation> listOfLevel, GameFactory gamefactory, MapParser mapParser) {
        super(nbActualGame, listOfLevel, gamefactory, mapParser);
    }

    @Override
    public boolean nextLevel() {
        if(this.getNbActualGame() != this.getLevelList().size()-1) {
            incrementLevel();
            return this.makeGame();
        }else{
            return launchBonusLevel();
        }
    }

    @Override
    public void changeScore() {
        int livesLoss = getActualGame().getSinglePlayer().livesLoss();
        int score;
        switch (livesLoss){
            case 0:
                score = 3;
                break;
            case 1:
                score = 2;
                break;
            default:
                score = 1;
        }
        if(getLevelList().get(getNbActualGame()).getPoint() < score){
            getLevelList().get(getNbActualGame()).setPoint(score);
        }
    }

    public boolean launchBonusLevel(){
        int totalS = 0;
        for(int i = 0 ; i < this.getLevelList().size()-1; i++){
            totalS += this.getLevelList().get(i).getPoint();
        }
        if(totalS == 3*(this.getLevelList().size()-1)){
            incrementLevel();
            return this.makeGame();
        }else{
            return false;
        }
    }
}

