package io.github.kayaxcoff.tlx.rendering;

import io.github.kayaxcoff.tlx.object.Shader;
import io.github.kayaxcoff.tlx.tools.Camera;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystem {

    private int vao, vbo;
    private int instanceVBO;
    private int maxParticles;
    private List<Particle> particles;
    private float[] instanceData;

    private static final float[] PARTICLE_VERTICES = {
           -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f,
           -0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f,
           -0.5f,  0.5f, 0.0f
    };

    public ParticleSystem(int maxParticles) {
        this.maxParticles = maxParticles;
        this.particles = new ArrayList<>();
        this.instanceData = new float[maxParticles * 7];

        this.setupBuffers();
    }

    private void setupBuffers() {
        this.vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this.vao);

        this.vbo = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, this.vbo);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, PARTICLE_VERTICES, GL30.GL_STATIC_DRAW);

        GL30.glVertexAttribPointer(0, 3, GL30.GL_FLOAT, false, 3 * Float.BYTES, 0);
        GL30.glEnableVertexAttribArray(0);

        this.instanceVBO = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, this.instanceVBO);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, (long) this.maxParticles * 7 * Float.BYTES, GL30.GL_DYNAMIC_DRAW);

        GL30.glVertexAttribPointer(1, 3, GL30.GL_FLOAT, false, 7 * Float.BYTES, 0);
        GL30.glEnableVertexAttribArray(1);
        GL33.glVertexAttribDivisor(1, 1);

        GL30.glVertexAttribPointer(2, 4, GL30.GL_FLOAT, false, 7 * Float.BYTES, 3 * Float.BYTES);
        GL30.glEnableVertexAttribArray(2);
        GL33.glVertexAttribDivisor(2, 1);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void addParticle(Particle particle) {
        if (this.particles.size() < this.maxParticles) {
            this.particles.add(particle);
        }
    }

    public void update(float deltaTime) {
        particles.removeIf(p -> !p.isAlive());

        for (Particle p : particles) {
            p.update(deltaTime);
        }

        updateInstanceData();
    }

    private void updateInstanceData() {
        int index = 0;
        for (Particle p : this.particles) {
            Vector3f pos = p.getPosition();
            Vector4f color = p.getColor();

            this.instanceData[index++] = pos.x;
            this.instanceData[index++] = pos.y;
            this.instanceData[index++] = pos.z;
            this.instanceData[index++] = color.x;
            this.instanceData[index++] = color.y;
            this.instanceData[index++] = color.z;
            this.instanceData[index++] = color.w;
        }

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, this.instanceVBO);
        GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, this.instanceData);
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
    }

    public void render(Shader shader, Camera camera) {
        if (this.particles.isEmpty()) return;

        shader.use();
        shader.upload("uProjection", camera.getProjectionMatrix());
        shader.upload("uView", camera.getViewMatrix());

        GL30.glBindVertexArray(this.vao);
        GL33.glDrawArraysInstanced(GL30.GL_TRIANGLES, 0, 6, this.particles.size());
        GL30.glBindVertexArray(0);

        shader.detach();
    }

    public void cleanup() {
        GL30.glDeleteVertexArrays(this.vao);
        GL30.glDeleteBuffers(this.vbo);
        GL30.glDeleteBuffers(this.instanceVBO);
    }

    public int getParticleCount() {
        return this.particles.size();
    }

    public int getMaxParticles() {
        return this.maxParticles;
    }

    public List<Particle> getParticles() {
        return this.particles;
    }

    public void clear() {
        this.particles.clear();
    }
}
