package io.github.kayaxcoff.tlx.object;

import io.github.kayaxcoff.tlx.tools.Camera;

public class SimulationObject {

    protected Transform transform;
    protected Mesh mesh;
    protected boolean flag;

    public SimulationObject(Mesh mesh) {
        this.mesh = mesh;
        this.transform = new Transform();
        this.flag = true;
    }

    public SimulationObject(Mesh mesh, Transform transform) {
        this.mesh = mesh;
        this.transform = transform;
        this.flag = true;
    }

    public void update(float deltaTime) {}

    public void render(Shader shader, Camera camera) {
        if (!this.flag) return;

        shader.use();
        shader.upload("uTransform", this.transform.getMatrix());
        shader.upload("uProjection", camera.getProjectionMatrix());
        shader.upload("uView", camera.getViewMatrix());

        this.mesh.draw();

        shader.detach();
    }

    public void cleanup() {
        if (this.mesh != null) {
            this.mesh.cleanup();
        }
    }

    public void setPosition(float x, float y, float z) {
        this.transform.setPosition(x, y, z);
    }

    public void translate(float dx, float dy, float dz) {
        this.transform.translate(dx, dy, dz);
    }

    public void setRotation(float x, float y, float z) {
        this.transform.setRotation(x, y, z);
    }

    public void rotate(float dx, float dy, float dz) {
        this.transform.rotate(dx, dy, dz);
    }

    public void setScale(float scale) {
        this.transform.setScale(scale);
    }

    public void setScale(float x, float y, float z) {
        this.transform.setScale(x, y, z);
    }

    public Transform getTransform() {
        return this.transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public Mesh getMesh() {
        return this.mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public boolean isActive() {
        return this.flag;
    }

    public void setActive(boolean active) {
        this.flag = active;
    }
}
