package ru.mipt.bit.platformer.input;

public interface InputSource {
    boolean isKeyPressed(int keyCode);

    default boolean isKeyJustPressed(int keyCode) {
        return false;
    }
}