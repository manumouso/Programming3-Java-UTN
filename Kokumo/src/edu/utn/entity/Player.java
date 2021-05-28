package edu.utn.entity;


import edu.utn.entity.ninja.Ninja;

import java.util.List;

public class Player {

    private int id;
    private String name;
    private List<Ninja> ninjas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ninja> getNinjas() {
        return ninjas;
    }

    public void setNinjas(List<Ninja> ninjas) {
        this.ninjas = ninjas;
    }
}