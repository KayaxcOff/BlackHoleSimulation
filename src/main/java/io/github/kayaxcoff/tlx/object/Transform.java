package io.github.kayaxcoff.tlx.object;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {

    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform() {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = new Vector3f(1, 1, 1);
    }

    public Transform(Vector3f position) {
        this.position = new Vector3f(position);
        this.rotation = new Vector3f(0.0f, 0.0f, 0.0f);
        this.scale = new Vector3f(1.0f, 1.0f, 1.0f);
    }

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = new Vector3f(position);
        this.rotation = new Vector3f(rotation);
        this.scale = new Vector3f(scale);
    }

    public Matrix4f getMatrix() {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();

        matrix.translate(this.position);

        matrix.rotateZ((float) Math.toRadians(this.rotation.z));
        matrix.rotateY((float) Math.toRadians(this.rotation.y));
        matrix.rotateX((float) Math.toRadians(this.rotation.x));

        matrix.scale(this.scale);

        return matrix;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    public void translate(Vector3f delta) {
        this.position.add(delta);
    }

    public void translate(float dx, float dy, float dz) {
        this.position.add(dx, dy, dz);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.set(x, y, z);
    }

    public void rotate(Vector3f delta) {
        this.rotation.add(delta);
    }

    public void rotate(float dx, float dy, float dz) {
        this.rotation.add(dx, dy, dz);
    }

    public Vector3f getScale() {
        return this.scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setScale(float x, float y, float z) {
        this.scale.set(x, y, z);
    }

    public void setScale(float uniform) {
        this.scale.set(uniform, uniform, uniform);
    }

    public void scale(Vector3f delta) {
        this.scale.mul(delta);
    }

    public void scale(float factor) {
        this.scale.mul(factor);
    }

    public Transform copy() {
        return new Transform(
                new Vector3f(this.position),
                new Vector3f(this.rotation),
                new Vector3f(this.scale)
        );
    }

    public void reset() {
        this.position.set(0.0f, 0.0f, 0.0f);
        this.rotation.set(0.0f, 0.0f, 0.0f);
        this.scale.set(1.0f, 1.0f, 1.0f);
    }

    @Override
    public String toString() {
        return String.format("Transform[pos=(%.2f, %.2f, %.2f), rot=(%.2f, %.2f, %.2f), scale=(%.2f, %.2f, %.2f)]",
                this.position.x, this.position.y, this.position.z,
                this.rotation.x, this.rotation.y, this.rotation.z,
                this.scale.x, this.scale.y, this.scale.z
        );
    }
}
