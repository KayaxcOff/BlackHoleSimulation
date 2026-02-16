package io.github.kayaxcoff.tlx.rendering;

import io.github.kayaxcoff.tlx.object.Mesh;
import io.github.kayaxcoff.tlx.object.SimulationObject;
import io.github.kayaxcoff.tlx.object.Transform;
import org.joml.Vector3f;

public class PhysicsSystem extends SimulationObject {

    protected Vector3f velocity;
    protected Vector3f acceleration;
    protected float mass;
    protected float drag; // Sürtünme/hava direnci

    public PhysicsSystem(Mesh mesh, float mass) {
        super(mesh);
        this.mass = mass;
        this.velocity = new Vector3f(0.0f, 0.0f, 0.0f);
        this.acceleration = new Vector3f(0.0f, 0.0f, 0.0f);
        this.drag = 0.98f;
    }

    public PhysicsSystem(Mesh mesh, Transform transform, float mass) {
        super(mesh, transform);
        this.mass = mass;
        this.velocity = new Vector3f(0.0f, 0.0f, 0.0f);
        this.acceleration = new Vector3f(0.0f, 0.0f, 0.0f);
        this.drag = 0.98f;
    }

    @Override
    public void update(float deltaTime) {
        velocity.add(
                acceleration.x * deltaTime,
                acceleration.y * deltaTime,
                acceleration.z * deltaTime
        );

        // Drag uygula (yavaşça yavaşla)
        velocity.mul(drag);

        // Pozisyonu güncelle: p = p + v * dt
        transform.translate(
                velocity.x * deltaTime,
                velocity.y * deltaTime,
                velocity.z * deltaTime
        );

        // Acceleration'ı sıfırla (her frame yeniden hesaplanacak)
        acceleration.set(0.0f, 0.0f, 0.0f);
    }

    public void applyForce(Vector3f force, float deltaTime) {
        Vector3f acc = new Vector3f(force).div(mass);
        acceleration.add(acc);
    }


    public void addVelocity(Vector3f vel) {
        velocity.add(vel);
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }

    public void setVelocity(float x, float y, float z) {
        this.velocity.set(x, y, z);
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getDrag() {
        return drag;
    }

    public void setDrag(float drag) {
        this.drag = drag;
    }
}
