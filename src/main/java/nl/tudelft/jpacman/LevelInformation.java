package nl.tudelft.jpacman;

public class LevelInformation {
        private final String file;
        private int point;

        public LevelInformation(String file, int point){
            this.file = file;
            this.point = point;
        }

        public String getLevel(){
            return this.file;
        }

        public int getPoint(){
            return this.point;
        }

        public void setPoint(int point){
            this.point = point;
        }
}
