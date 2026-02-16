package io.github.kayaxcoff.tlx.tools;

import io.github.kayaxcoff.tlx.callbacks.ListenKey;
import io.github.kayaxcoff.tlx.callbacks.ListenMouse;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {

    private final Matrix4f projectionMatrix, viewMatrix;
    private final Vector3f position, target;

    private float distance, yaw, pitch;
    private float orbitSpeed;
    private float zoomSpeed;
    private final float panSpeed;
    private float fov;
    private float aspectRatio;
    private final float nearPlane, farPlane;

    private final Vector2f lastMousePos;
    private boolean firstMouse;

    public Camera(Vector3f target, float distance) {
        this.target = new Vector3f(target);
        this.distance = distance;
        this.position = new Vector3f();

        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();

        this.yaw = 0.0f;
        this.pitch = 30.0f;

        this.orbitSpeed = 50.0f;
        this.zoomSpeed = 5.0f;
        this.panSpeed = 3.0f;

        this.fov = 45.0f;
        this.aspectRatio = 1000.0f / 800.0f;
        this.nearPlane = 0.1f;
        this.farPlane = 100.0f;

        // Mouse
        this.lastMousePos = new Vector2f();
        this.firstMouse = true;

        this.updateCameraPosition();
        this.adjustProjection();
    }

    public Camera() {
        this(new Vector3f(0, 0, 0), 15.0f);
    }

    public void update(float deltaTime) {
        this.handleKeyboardInput(deltaTime);
        this.handleMouseInput(deltaTime);
        this.updateCameraPosition();
    }

    private void handleKeyboardInput(float deltaTime) {
        if (ListenKey.isKeyDown(GLFW.GLFW_KEY_A)) {
            this.yaw -= this.orbitSpeed * deltaTime;
        }
        if (ListenKey.isKeyDown(GLFW.GLFW_KEY_D)) {
            this.yaw += this.orbitSpeed * deltaTime;
        }
        if (ListenKey.isKeyDown(GLFW.GLFW_KEY_W)) {
            this.pitch += this.orbitSpeed * deltaTime;
        }
        if (ListenKey.isKeyDown(GLFW.GLFW_KEY_S)) {
            this.pitch -= this.orbitSpeed * deltaTime;
        }

        if (this.pitch > 89.0f) this.pitch = 89.0f;
        if (this.pitch < -89.0f) this.pitch = -89.0f;

        if (ListenKey.isKeyDown(GLFW.GLFW_KEY_Q)) {
            this.distance += this.zoomSpeed * deltaTime;
        }
        if (ListenKey.isKeyDown(GLFW.GLFW_KEY_E)) {
            this.distance -= this.zoomSpeed * deltaTime;
        }

        if (this.distance < 2.0f) this.distance = 2.0f;
        if (this.distance > 50.0f) this.distance = 50.0f;

        Vector3f right = new Vector3f();
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f forward = new Vector3f(this.target).sub(this.position).normalize();
        forward.cross(up, right);
        right.normalize();

        float panDelta = this.panSpeed * deltaTime;

        if (ListenKey.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            this.target.sub(right.x * panDelta, right.y * panDelta, right.z * panDelta);
        }
        if (ListenKey.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            this.target.add(right.x * panDelta, right.y * panDelta, right.z * panDelta);
        }
        if (ListenKey.isKeyDown(GLFW.GLFW_KEY_UP)) {
            this.target.add(0, panDelta, 0);
        }
        if (ListenKey.isKeyDown(GLFW.GLFW_KEY_DOWN)) {
            this.target.sub(0, panDelta, 0);
        }
    }

    private void handleMouseInput(float deltaTime) {
        if (ListenMouse.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            float mouseX = ListenMouse.getX();
            float mouseY = ListenMouse.getY();

            if (this.firstMouse) {
                this.lastMousePos.set(mouseX, mouseY);
                this.firstMouse = false;
            }

            float deltaX = mouseX - this.lastMousePos.x;
            float deltaY = mouseY - this.lastMousePos.y;

            this.lastMousePos.set(mouseX, mouseY);

            this.yaw += deltaX * 0.1f;
            this.pitch -= deltaY * 0.1f;

            if (this.pitch > 89.0f) this.pitch = 89.0f;
            if (this.pitch < -89.0f) this.pitch = -89.0f;
        } else {
            this.firstMouse = true;
        }

        float scroll = ListenMouse.getScrollY();
        if (scroll != 0) {
            this.distance -= scroll * 0.5f;
            if (this.distance < 2.0f) this.distance = 2.0f;
            if (this.distance > 50.0f) this.distance = 50.0f;
        }
    }

    private void updateCameraPosition() {
        float yawRad = (float) Math.toRadians(this.yaw);
        float pitchRad = (float) Math.toRadians(this.pitch);

        this.position.x = this.target.x + this.distance * (float) (Math.cos(pitchRad) * Math.sin(yawRad));
        this.position.y = this.target.y + this.distance * (float) Math.sin(pitchRad);
        this.position.z = this.target.z + this.distance * (float) (Math.cos(pitchRad) * Math.cos(yawRad));
    }

    public void adjustProjection() {
        this.projectionMatrix.identity();
        this.projectionMatrix.perspective(
                (float) Math.toRadians(this.fov),
                this.aspectRatio,
                this.nearPlane,
                this.farPlane
        );
    }

    public void setAspectRatio(float width, float height) {
        this.aspectRatio = width / height;
        this.adjustProjection();
    }

    public Matrix4f getViewMatrix() {
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(
                this.position,
                this.target,
                new Vector3f(0, 1, 0) // Up vector
        );
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    // Getters/Setters
    public Vector3f getPosition() {
        return this.position;
    }

    public Vector3f getTarget() {
        return this.target;
    }

    public void setTarget(Vector3f target) {
        this.target.set(target);
    }

    public void setTarget(float x, float y, float z) {
        this.target.set(x, y, z);
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
        if (this.distance < 2.0f) this.distance = 2.0f;
        if (this.distance > 50.0f) this.distance = 50.0f;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
        if (this.pitch > 89.0f) this.pitch = 89.0f;
        if (this.pitch < -89.0f) this.pitch = -89.0f;
    }

    public void setOrbitSpeed(float speed) {
        this.orbitSpeed = speed;
    }

    public void setZoomSpeed(float speed) {
        this.zoomSpeed = speed;
    }

    public void setFov(float fov) {
        this.fov = fov;
        this.adjustProjection();
    }
}
