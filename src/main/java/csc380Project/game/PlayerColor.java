package csc380Project.game;

import javafx.scene.paint.Color;

public class PlayerColor {

    private String name;
    private Color colorForPlayer;


    public PlayerColor(String n) {
        name = n;
        colorForPlayer = Color.color(Math.random(), Math.random(), Math.random());
    }

    public Color getColor() {
        return this.colorForPlayer;
    }

    public String getName() {
        return this.name;
    }

}