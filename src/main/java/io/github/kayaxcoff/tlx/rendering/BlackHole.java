package io.github.kayaxcoff.tlx.rendering;

import io.github.kayaxcoff.tlx.object.Mesh;
import io.github.kayaxcoff.tlx.object.SimulationObject;
import io.github.kayaxcoff.tlx.object.Transform;
import org.joml.Vector3f;

public class BlackHole extends SimulationObject {

    private float mass;
    private float eventHorizonRadius;
    private float rotationSpeed;
    private Vector3f color;

    public BlackHole(Mesh mesh, float mass) {
        super(mesh);
        this.mass = mass;
        this.eventHorizonRadius = this.calculateEventHorizonRadius(mass);
        this.rotationSpeed = 50.0f;
        this.color = new Vector3f(0.0f, 0.0f, 0.0f);

        this.transform.setScale(this.eventHorizonRadius);
    }

    public BlackHole(Mesh mesh, Transform transform, float mass) {
        super(mesh, transform);
        this.mass = mass;
        this.eventHorizonRadius = this.calculateEventHorizonRadius(mass);
        this.rotationSpeed = 50.0f;
        this.color = new Vector3f(0.0f, 0.0f, 0.0f);

        transform.setScale(this.eventHorizonRadius);
    }

    private float calculateEventHorizonRadius(float mass) {
        return 0.5f + (mass * 0.1f);
    }

    @Override
    public void update(float deltaTime) {
        this.transform.rotate(0.0f, this.rotationSpeed * deltaTime, 0.0f);
    }

    public Vector3f calculateGravitationalForce(SimulationObject obj) {
        Vector3f blackHolePos = this.transform.getPosition();
        Vector3f objPos = obj.getTransform().getPosition();

        Vector3f direction = new Vector3f(blackHolePos).sub(objPos);
        float distance = direction.length();

        if (distance < this.eventHorizonRadius) {
            distance = this.eventHorizonRadius;
        }

        float forceMagnitude = (this.mass * 10.0f) / (distance * distance);

        direction.normalize();
        direction.mul(forceMagnitude);

        return direction;
    }

    public boolean isInsideEventHorizon(SimulationObject obj) {
        Vector3f blackHolePos = this.transform.getPosition();
        Vector3f objPos = obj.getTransform().getPosition();
        float distance = blackHolePos.distance(objPos);

        return distance < this.eventHorizonRadius;
    }

    public void applyGravityTo(PhysicsSystem obj, float deltaTime) {
        Vector3f force = this.calculateGravitationalForce(obj);

        if (isInsideEventHorizon(obj)) {
            obj.setActive(false);
            return;
        }

        obj.applyForce(force, deltaTime);
    }

    public float getMass() {
        return this.mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
        this.eventHorizonRadius = calculateEventHorizonRadius(mass);
        this.transform.setScale(this.eventHorizonRadius);
    }

    public float getEventHorizonRadius() {
        return this.eventHorizonRadius;
    }

    public float getRotationSpeed() {
        return this.rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public Vector3f getColor() {
        return this.color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
    }
}
