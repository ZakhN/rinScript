package com.example.rinscript1;

import javafx.scene.shape.Rectangle;

public class VarDef extends Rectangle {
    private String varDefName;
    private Types varDefValue;

    public boolean isVarDef() {
        return true;
    }

    public String getVarDefName() {
        return varDefName;
    }

    public void setVarDefName(String varDefName) {
        this.varDefName = varDefName;
    }

    public Types getVarDefValue() {
        return varDefValue;
    }

    public void setVarDefValue(Types varDefValue) {
        this.varDefValue = varDefValue;
    }
}
