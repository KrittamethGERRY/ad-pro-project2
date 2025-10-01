package se233.notcontra.model;

import java.util.HashMap;

import javafx.scene.input.KeyCode;

public class Keys {
    private HashMap<KeyCode, Boolean> keys;
    public Keys() {
        keys = new HashMap<>();
    }

    public void add(KeyCode key) {
        keys.put(key, true);
    }

    public void remove(KeyCode key) {
        keys.put(key, false);
    }

    public boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }
}