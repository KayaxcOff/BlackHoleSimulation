package io.github.kayaxcoff.tlx.scenes;

import org.lwjgl.opengl.GL11;

public abstract class Scene {

    protected float bgR, bgG, bgB, bgA;

    public Scene() {}

    public abstract void Init();
    public abstract void update(float deltaTime);
    public abstract void render();
    public abstract void cleanup();

    public void background() {
        GL11.glClearColor(this.bgR, this.bgG, this.bgB, this.bgA);
    }
}
