package me.levitate.playergradients.gradients;

import lombok.Getter;

@Getter
public class Gradient {
    private final String name;
    private final String placeholder;
    private final String permission;

    public Gradient(String name, String placeholder) {
        this.name = name;
        this.placeholder = placeholder;
        this.permission = "pg." + name;
    }
}
