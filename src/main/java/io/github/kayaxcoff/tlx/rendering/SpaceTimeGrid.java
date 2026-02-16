package io.github.kayaxcoff.tlx.rendering;

import io.github.kayaxcoff.tlx.object.Shader;
import io.github.kayaxcoff.tlx.tools.Camera;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class SpaceTimeGrid {

    private int vao, vbo;
    private int vertexCount;
    private final Vector3f center;
    private final float gridSize;      // Grid'in toplam boyutu
    private final int gridResolution;  // Kaç çizgi olacak
    private final float lineSpacing;   // Çizgiler arası mesafe
    private final Vector3f gridColor;

    public SpaceTimeGrid(Vector3f center, float gridSize, int gridResolution) {
        this.center = new Vector3f(center);
        this.gridSize = gridSize;
        this.gridResolution = gridResolution;
        this.lineSpacing = gridSize / gridResolution;
        this.gridColor = new Vector3f(0.2f, 0.4f, 0.8f); // Mavi çizgiler

        generateGrid();
    }

    private void generateGrid() {
        List<Float> vertices = new ArrayList<>();
        float halfSize = gridSize / 2.0f;

        System.out.println("=== GRID DEBUG ===");
        System.out.println("Grid center: " + center);
        System.out.println("Grid size: " + gridSize);
        System.out.println("First vertex will be at: (" + (center.x - halfSize) + ", " + center.y + ", " + (center.z - halfSize) + ")");

        // X yönünde çizgiler (Z ekseni boyunca)
        for (int i = 0; i <= gridResolution; i++) {
            float x = -halfSize + (i * lineSpacing);

            // Başlangıç noktası
            vertices.add(center.x + x);
            vertices.add(center.y);
            vertices.add(center.z - halfSize);
            vertices.add(gridColor.x);
            vertices.add(gridColor.y);
            vertices.add(gridColor.z);
            vertices.add(1.0f); // alpha

            // Bitiş noktası
            vertices.add(center.x + x);
            vertices.add(center.y);
            vertices.add(center.z + halfSize);
            vertices.add(gridColor.x);
            vertices.add(gridColor.y);
            vertices.add(gridColor.z);
            vertices.add(1.0f); // alpha
        }

        // Z yönünde çizgiler (X ekseni boyunca)
        for (int i = 0; i <= gridResolution; i++) {
            float z = -halfSize + (i * lineSpacing);

            // Başlangıç noktası
            vertices.add(center.x - halfSize);
            vertices.add(center.y);
            vertices.add(center.z + z);
            vertices.add(gridColor.x);
            vertices.add(gridColor.y);
            vertices.add(gridColor.z);
            vertices.add(1.0f); // alpha

            // Bitiş noktası
            vertices.add(center.x + halfSize);
            vertices.add(center.y);
            vertices.add(center.z + z);
            vertices.add(gridColor.x);
            vertices.add(gridColor.y);
            vertices.add(gridColor.z);
            vertices.add(1.0f); // alpha
        }

        vertexCount = vertices.size() / 7;

        // Convert to array
        float[] vertexArray = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) {
            vertexArray[i] = vertices.get(i);
        }

        setupBuffers(vertexArray);
    }

    private void setupBuffers(float[] vertices) {
        // VAO
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // VBO
        vbo = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertices, GL30.GL_STATIC_DRAW);

        // Position attribute (location = 0)
        int posSize = 3;
        int colorSize = 4;
        int vertexSizeBytes = (posSize + colorSize) * Float.BYTES;

        GL30.glVertexAttribPointer(0, posSize, GL30.GL_FLOAT, false, vertexSizeBytes, 0);
        GL30.glEnableVertexAttribArray(0);

        // Color attribute (location = 1)
        GL30.glVertexAttribPointer(1, colorSize, GL30.GL_FLOAT, false, vertexSizeBytes, posSize * Float.BYTES);
        GL30.glEnableVertexAttribArray(1);

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void render(Shader shader, Camera camera, Vector3f blackHolePos, float blackHoleMass, float eventHorizonRadius) {
        shader.use();
        shader.upload("uProjection", camera.getProjectionMatrix());
        shader.upload("uView", camera.getViewMatrix());

        //org.joml.Matrix4f identity = new org.joml.Matrix4f().identity();
        //shader.upload("uTransform", identity);

        // YORUMA AL - şimdilik kullanmıyoruz
        // shader.upload("uBlackHolePos", blackHolePos);
        // shader.upload("uBlackHoleMass", blackHoleMass);
        // shader.upload("uEventHorizonRadius", eventHorizonRadius);

        GL30.glBindVertexArray(vao);
        GL30.glDrawArrays(GL30.GL_LINES, 0, vertexCount);
        GL30.glBindVertexArray(0);

        shader.detach();
    }

    public void cleanup() {
        GL30.glDeleteVertexArrays(vao);
        GL30.glDeleteBuffers(vbo);
    }

    // Getters/Setters
    public Vector3f getCenter() {
        return center;
    }

    public void setCenter(Vector3f center) {
        this.center.set(center);
        regenerate();
    }

    public void setCenter(float x, float y, float z) {
        this.center.set(x, y, z);
        regenerate();
    }

    public Vector3f getGridColor() {
        return gridColor;
    }

    public void setGridColor(Vector3f color) {
        this.gridColor.set(color);
        regenerate();
    }

    public void setGridColor(float r, float g, float b) {
        this.gridColor.set(r, g, b);
        regenerate();
    }

    private void regenerate() {
        cleanup();
        generateGrid();
    }
}