package edu.utn.model.square;

import edu.utn.model.Player;

public class Tree extends Land {

    public Tree(int x, int y) {
        super(x, y);
    }

    @Override
    public void playerStandsOn(Player player) {

    }

    @Override
    public String name() {
        return "Arbol";
    }

    @Override
    public void fill() {

    }
}