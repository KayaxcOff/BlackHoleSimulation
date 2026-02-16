package io.github.kayaxcoff.tlx.rendering;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Particle {

    private Vector3f position;
    private Vector3f velocity;
    private Vector4f color;
    private float lifetime;
    private float maxLifetime;
    private float size;

    public Particle(Vector3f position, Vector3f velocity, Vector4f color, float lifetime) {
        this.position = new Vector3f(position);
        this.velocity = new Vector3f(velocity);
        this.color = new Vector4f(color);
        this.lifetime = lifetime;
        this.maxLifetime = lifetime;
        this.size = 1.0f;
    }

    public Particle(Vector3f position, Vector3f velocity, Vector4f color, float lifetime, float size) {
        this(position, velocity, color, lifetime);
        this.size = size;
    }

    public void update(float deltaTime) {
        // Update position
        position.add(
                velocity.x * deltaTime,
                velocity.y * deltaTime,
                velocity.z * deltaTime
        );

        lifetime -= deltaTime;

        color.w = lifetime / maxLifetime;
    }

    public boolean isAlive() {
        return lifetime > 0;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color.set(color);
    }

    public float getLifetime() {
        return lifetime;
    }

    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
