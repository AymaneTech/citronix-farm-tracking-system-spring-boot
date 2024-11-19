package com.wora.citronix.tree.domain;

public enum Level {
    OLD, MATURE, YOUNG;

    public static Level fromAge(int age) {
        if (age <= 3) return YOUNG;
        else if (age > 3 && age <= 10) return MATURE;
        else return OLD;
    }
}
