package io.github.kayaxcoff.tlx.callbacks;

import org.lwjgl.glfw.GLFW;

public class ListenMouse {

    private static ListenMouse instance;

    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private final boolean[] mouseButtonPressed = new boolean[9];
    private boolean isDragging;
    private boolean firstMouse = true;

    private ListenMouse() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static ListenMouse get() {
        if (ListenMouse.instance == null) ListenMouse.instance = new ListenMouse();

        return ListenMouse.instance;
    }

    public static void position(long window, double xPos, double yPos) {
        if(get().firstMouse) {
            get().lastX = xPos;
            get().lastY = yPos;
            get().firstMouse = false;
        }

        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void button(long window, int button, int action, int mods) {
        if(button < get().mouseButtonPressed.length) {
            if (action == GLFW.GLFW_PRESS) {
                get().mouseButtonPressed[button] = true;
            } else if (action == GLFW.GLFW_RELEASE) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void scroll(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX() {
        return (float) get().xPos;
    }

    public static float getY() {
        return (float) get().yPos;
    }

    public static float getDx() {
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float) get().scrollX;
    }

    public static float getScrollY() {
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        }
        return false;
    }

    public static boolean isButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        }
        return false;
    }

    public static float getLastX() {
        return (float) get().lastX;
    }

    public static float getLastY() {
        return (float) get().lastY;
    }
}
