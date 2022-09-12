package com.example.rinscript1;

import java.util.HashMap;
import java.util.Map;

public class Scopes {
    Map<String, VarDef> varDefs;

    public Scopes() {
        this.varDefs = new HashMap<>();
    }
}
