package io.github.kayaxcoff.tlx.rendering;

import io.github.kayaxcoff.tlx.object.Mesh;
import io.github.kayaxcoff.tlx.object.SimulationObject;
import io.github.kayaxcoff.tlx.object.Transform;

public class RotatingCube extends SimulationObject {

    private float rotationSpeedX;
    private float rotationSpeedY;

    public RotatingCube(Mesh mesh) {
        super(mesh);
        this.rotationSpeedX = 45.0f;
        this.rotationSpeedY = 60.0f;
    }

    public RotatingCube(Mesh mesh, Transform transform, float speedX, float speedY) {
        super(mesh, transform);
        this.rotationSpeedX = speedX;
        this.rotationSpeedY = speedY;
    }

    @Override
    public void update(float deltaTime) {
        this.transform.rotate(
                this.rotationSpeedX * deltaTime,
                this.rotationSpeedY * deltaTime,
                0.0f
        );
    }

    public void setRotationSpeed(float speedX, float speedY) {
        this.rotationSpeedX = speedX;
        this.rotationSpeedY = speedY;
    }
}
