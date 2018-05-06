package csc380Project.game;

import javafx.scene.paint.Color;
import java.util.Random;

public class PlayerColor {

    private String name;
    private Random rand;
    private Color colorForPlayer;
    private Color[] colorList;

    public PlayerColor(String n) {
        name = n;
        rand = new Random();
        colorForPlayer = Color.BLACK;
        colorList = new Color[] {Color.GREEN, Color.LIME, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.SILVER, Color.TEAL, Color.VIOLET, Color.YELLOW, Color.TURQUOISE, Color.SLATEGREY, Color.OLIVE, Color.MINTCREAM};
    }

    public void setColor() {
        int x = rand.nextInt(14);
        colorForPlayer = colorList[x];
    }

    public Color getColor() {
        return this.colorForPlayer;
    }

    public String getName() {
        return this.name;
    }

}