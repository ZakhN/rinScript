package com.example.rinscript1;

import javafx.scene.shape.Rectangle;

public class Function extends Rectangle {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    public boolean isFunction() {
        return true;
    }
}
