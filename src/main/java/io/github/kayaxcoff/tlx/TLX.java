package io.github.kayaxcoff.tlx;

import io.github.kayaxcoff.tlx.callbacks.ListenKey;
import io.github.kayaxcoff.tlx.callbacks.ListenMouse;
import io.github.kayaxcoff.tlx.scenes.EditorScene;
import io.github.kayaxcoff.tlx.scenes.LevelScene;
import io.github.kayaxcoff.tlx.scenes.Scene;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class TLX {

    private static TLX instance;
    private static Scene scene;

    private final int SCREEN_WIDTH, SCREEN_HEIGHT;
    private final String SCREEN_TITLE;
    private long window;

    public TLX() {
        this.SCREEN_WIDTH  = 1000;
        this.SCREEN_HEIGHT = 800;
        this.SCREEN_TITLE  = "TLX - Simulation";
    }

    public static TLX get() {
        if (TLX.instance == null) TLX.instance = new TLX();

        return TLX.instance;
    }

    public static void setScene(int idx) {
        switch (idx) {
            case 0 -> TLX.scene = new LevelScene();
            case 1 -> TLX.scene = new EditorScene();
            default -> throw new IllegalStateException("Unexpected value: " + idx);
        }
        TLX.scene.Init();
    }

    public void run() {
        this.Init();
        TLX.setScene(0);
        this.loop();
        this.cleanup();
    }

    public long getWindow() {
        return this.window;
    }

    public int getWidth() {
        return this.SCREEN_WIDTH;
    }

    public int getHeight() {
        return this.SCREEN_HEIGHT;
    }

    private void Init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        this.window = GLFW.glfwCreateWindow(this.SCREEN_WIDTH, this.SCREEN_HEIGHT, this.SCREEN_TITLE, 0, 0);

        this.setupCallbacks();

        GLFW.glfwMakeContextCurrent(this.window);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(this.window);

        GL.createCapabilities();

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LESS);
    }

    private void loop() {
        float beginTime = (float) GLFW.glfwGetTime();
        float endTime;
        float dt = -1.0f;

        while (!GLFW.glfwWindowShouldClose(this.window)) {
            GLFW.glfwPollEvents();

            TLX.scene.background();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            if(dt >= 0) TLX.scene.update(dt);

            if(ListenKey.isKeyPressed(GLFW.GLFW_KEY_SPACE)) TLX.setScene(1);

            GLFW.glfwSwapBuffers(this.window);

            ListenKey.endFrame();
            ListenMouse.endFrame();

            endTime = (float) GLFW.glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    private void cleanup() {
        TLX.scene.cleanup();

        Callbacks.glfwFreeCallbacks(this.window);
        GLFW.glfwDestroyWindow(this.window);
        GLFW.glfwTerminate();
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    private void setupCallbacks() {
        GLFW.glfwSetCursorPosCallback(this.window, ListenMouse::position);
        GLFW.glfwSetMouseButtonCallback(this.window, ListenMouse::button);
        GLFW.glfwSetScrollCallback(this.window, ListenMouse::scroll);

        GLFW.glfwSetKeyCallback(this.window, ListenKey::key);

        // Window resize callback
        GLFW.glfwSetFramebufferSizeCallback(this.window, (window, width, height) -> {
            GL11.glViewport(0, 0, width, height);
        });
    }
}
