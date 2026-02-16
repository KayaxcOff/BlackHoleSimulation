package io.github.kayaxcoff.tlx.callbacks;

import org.lwjgl.glfw.GLFW;

public class ListenKey {

    private static ListenKey instance;

    private final boolean[] keyDown = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private final boolean[] keyPressedThisFrame = new boolean[GLFW.GLFW_KEY_LAST + 1];
    private final boolean[] keyReleasedThisFrame = new boolean[GLFW.GLFW_KEY_LAST + 1];

    private ListenKey() {}

    public static ListenKey get() {
        if (instance == null) instance = new ListenKey();
        return instance;
    }

    public static void key(long window, int key, int scancode, int action, int mods) {

        if (key < 0 || key > GLFW.GLFW_KEY_LAST) return;

        if (action == GLFW.GLFW_PRESS) {
            if (!get().keyDown[key]) { // ilk basış
                get().keyPressedThisFrame[key] = true;
            }
            get().keyDown[key] = true;
        }

        else if (action == GLFW.GLFW_RELEASE) {
            get().keyDown[key] = false;
            get().keyReleasedThisFrame[key] = true;
        }

        if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
            GLFW.glfwSetWindowShouldClose(window, true);
        }
    }

    public static boolean isKeyDown(int key) {
        return key >= 0 && key <= GLFW.GLFW_KEY_LAST && get().keyDown[key];
    }

    public static boolean isKeyPressed(int key) {
        return key >= 0 && key <= GLFW.GLFW_KEY_LAST && get().keyPressedThisFrame[key];
    }

    public static boolean isKeyReleased(int key) {
        return key >= 0 && key <= GLFW.GLFW_KEY_LAST && get().keyReleasedThisFrame[key];
    }

    public static void endFrame() {
        for (int i = 0; i < get().keyPressedThisFrame.length; i++) {
            get().keyPressedThisFrame[i] = false;
            get().keyReleasedThisFrame[i] = false;
        }
    }
}