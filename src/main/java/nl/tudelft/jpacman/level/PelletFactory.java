package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.sprite.PacManSprites;

public class PelletFactory {
    /**
     * The sprite store that provides sprites for units.
     */
    private final PacManSprites sprites;

    private final int pellet_value = LevelFactory.getPelletValue();

    private final int fruit_value = LevelFactory.getFruitValue();

    private final int power_value = LevelFactory.getPowerpillValue();

    private final int FRUIT_NUMBER = 5;

    public PelletFactory(PacManSprites spriteStore) {
        this.sprites = spriteStore;
    }

    public PacManSprites getSprites() {
        return sprites;
    }

    /**
     * Creates a new powerpill.
     *
     * @return The new powerpill.
     */

    public Pellet createFruit() {
        int rand = (int) (Math.random() * FRUIT_NUMBER);
        switch (rand) {
            case 0:
                return createApple();
            case 1:
                return createCherry();
            case 2:
                return createMelon();
            case 3:
                return createOrange();
            case 4:
                return createStrawberry();
        }
        return createPellet();
    }

    /**
     * Creates a new pellet.
     *
     * @return The new pellet.
     */
    public Pellet createPellet() {
        return new Pellet(pellet_value, sprites.getPelletSprite());
    }

    /**
     * Creates a new fruit.
     *
     * @return The new fruit.
     */
    public Pellet createApple() {
        return new Pellet(fruit_value, sprites.getAppleSprite());
    }

    /**
     * Creates a new fruit.
     *
     * @return The new fruit.
     */
    public Pellet createCherry() {
        return new Pellet(fruit_value, sprites.getCherrySprite());
    }

    /**
     * Creates a new fruit.
     *
     * @return The new fruit.
     */
    public Pellet createMelon() {
        return new Pellet(fruit_value, sprites.getMelonSprite());
    }

    /**
     * Creates a new fruit.
     *
     * @return The new fruit.
     */
    public Pellet createOrange() {
        return new Pellet(fruit_value, sprites.getOrangeSprite());
    }

    /**
     * Creates a new fruit.
     *
     * @return The new fruit.
     */
    public Pellet createStrawberry() {
        return new Pellet(fruit_value, sprites.getStrawberrySprite());
    }

    public PowerPill createPowerPill() {
        return new PowerPill(power_value, sprites.getPowerPillSprite());
    }
}
