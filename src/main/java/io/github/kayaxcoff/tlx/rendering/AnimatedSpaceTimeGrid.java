package io.github.kayaxcoff.tlx.rendering;

import io.github.kayaxcoff.tlx.object.Shader;
import io.github.kayaxcoff.tlx.tools.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class AnimatedSpaceTimeGrid {

    private int vao, vbo;
    private int vertexCount;
    private final Vector3f center;
    private final float gridSize;
    private final int gridResolution;
    private final float lineSpacing;
    private final Vector3f gridColor;

    private float animationTime;
    private float waveSpeed;
    private float waveAmplitude;

    public AnimatedSpaceTimeGrid(Vector3f center, float gridSize, int gridResolution) {
        this.center = new Vector3f(center);
        this.gridSize = gridSize;
        this.gridResolution = gridResolution;
        this.lineSpacing = gridSize / gridResolution;
        this.gridColor = new Vector3f(0.2f, 0.4f, 0.8f);

        this.animationTime = 0.0f;
        this.waveSpeed = 2.0f;
        this.waveAmplitude = 0.0f; // Başlangıçta düz, bükülme olmadan

        generateGrid();
    }

    private void generateGrid() {
        List<Float> vertices = new ArrayList<>();
        float halfSize = gridSize / 2.0f;

        // X yönünde çizgiler
        for (int i = 0; i <= gridResolution; i++) {
            float x = -halfSize + (i * lineSpacing);

            // Her çizgiyi segments'e böl (animasyon için)
            int segments = 20;
            for (int j = 0; j < segments; j++) {
                float t1 = (float) j / segments;
                float t2 = (float) (j + 1) / segments;

                float z1 = -halfSize + t1 * gridSize;
                float z2 = -halfSize + t2 * gridSize;

                // Segment başlangıcı
                vertices.add(center.x + x);
                vertices.add(center.y);
                vertices.add(center.z + z1);
                vertices.add(gridColor.x);
                vertices.add(gridColor.y);
                vertices.add(gridColor.z);
                vertices.add(1.0f);

                // Segment sonu
                vertices.add(center.x + x);
                vertices.add(center.y);
                vertices.add(center.z + z2);
                vertices.add(gridColor.x);
                vertices.add(gridColor.y);
                vertices.add(gridColor.z);
                vertices.add(1.0f);
            }
        }

        // Z yönünde çizgiler
        for (int i = 0; i <= gridResolution; i++) {
            float z = -halfSize + (i * lineSpacing);

            int segments = 20;
            for (int j = 0; j < segments; j++) {
                float t1 = (float) j / segments;
                float t2 = (float) (j + 1) / segments;

                float x1 = -halfSize + t1 * gridSize;
                float x2 = -halfSize + t2 * gridSize;

                vertices.add(center.x + x1);
                vertices.add(center.y);
                vertices.add(center.z + z);
                vertices.add(gridColor.x);
                vertices.add(gridColor.y);
                vertices.add(gridColor.z);
                vertices.add(1.0f);

                vertices.add(center.x + x2);
                vertices.add(center.y);
                vertices.add(center.z + z);
                vertices.add(gridColor.x);
                vertices.add(gridColor.y);
                vertices.add(gridColor.z);
                vertices.add(1.0f);
            }
        }

        vertexCount = vertices.size() / 7;

        float[] vertexArray = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            vertexArray[i] = vertices.get(i);
        }

        setupBuffers(vertexArray);
    }

    private void setupBuffers(float[] vertices) {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        vbo = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STATIC_DRAW);

        int posSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (posSize + colorSize) * Float.BYTES;

        GL30.glVertexAttribPointer(0, posSize, GL30.GL_FLOAT, false, vertexSizeBytes, 0);
        GL30.glEnableVertexAttribArray(0);

        GL30.glVertexAttribPointer(1, colorSize, GL30.GL_FLOAT, false, vertexSizeBytes, posSize * Float.BYTES);
        GL30.glEnableVertexAttribArray(1);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void update(float deltaTime) {
        animationTime += deltaTime * waveSpeed;
    }

    public void render(Shader shader, Camera camera) {
        shader.use();
        shader.upload("uProjection", camera.getProjectionMatrix());
        shader.upload("uView", camera.getViewMatrix());

        Matrix4f transform = new Matrix4f().identity();

        // Hafif dalga efekti (opsiyonel)
        if (waveAmplitude > 0.0f) {
            float wave = (float) Math.sin(animationTime) * waveAmplitude;
            transform.translate(0, wave, 0);
        }

        shader.upload("uTransform", transform);

        GL30.glBindVertexArray(vao);
        GL30.glDrawArrays(GL30.GL_LINES, 0, vertexCount);
        GL30.glBindVertexArray(0);

        shader.detach();
    }

    public void cleanup() {
        GL30.glDeleteVertexArrays(vao);
        GL30.glDeleteBuffers(vbo);
    }

    public void setWaveAmplitude(float amplitude) {
        this.waveAmplitude = amplitude;
    }

    public void setWaveSpeed(float speed) {
        this.waveSpeed = speed;
    }

    public Vector3f getGridColor() {
        return gridColor;
    }

    public void setGridColor(float r, float g, float b) {
        this.gridColor.set(r, g, b);
        cleanup();
        generateGrid();
    }
}