package io.github.kayaxcoff.tlx.object;

import org.lwjgl.opengl.GL30;

public class Mesh {

    private int vao;
    private int vbo;
    private final int VERTEX_COUNT;
    private final float[] vertices;

    public Mesh(float[] vertices) {
        this.vertices = vertices;
        this.VERTEX_COUNT = vertices.length / 7;
        this.setup();
    }

    private void setup() {
        this.vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this.vao);

        this.vbo = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, this.vbo);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, this.vertices, GL30.GL_STATIC_DRAW);

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

    public void draw() {
        GL30.glBindVertexArray(this.vao);
        GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, this.VERTEX_COUNT);
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        GL30.glDeleteVertexArrays(this.vao);
        GL30.glDeleteBuffers(this.vbo);
    }

    public int getVao() {
        return this.vao;
    }

    public int getVbo() {
        return this.vbo;
    }

    public int getVertexCount() {
        return this.VERTEX_COUNT;
    }

    public float[] getVertices() {
        return this.vertices;
    }

    public static Mesh createCube() {
        float[] vertices = {
               -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 1.0f,
               -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 1.0f,
               -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f, 1.0f,

               -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,
               -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,
               -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f, 1.0f,

               -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 1.0f, 1.0f,
               -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 1.0f,
               -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 1.0f,
               -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 1.0f, 1.0f,
               -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 1.0f, 1.0f,
               -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, 1.0f, 1.0f,

                0.5f, -0.5f, -0.5f,  1.0f, 1.0f, 0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 1.0f, 0.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 1.0f, 0.0f, 1.0f,

               -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 1.0f, 1.0f,
               -0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 1.0f, 1.0f,
               -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 1.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 1.0f, 1.0f,

               -0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 1.0f, 1.0f,
               -0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 1.0f, 1.0f,
               -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 1.0f, 1.0f
        };

        return new Mesh(vertices);
    }

    public static Mesh createQuad() {
        float[] vertices = {
               -0.5f,  0.5f, 0.0f,  1.0f, 0.0f, 0.0f, 1.0f,
               -0.5f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.0f,  0.0f, 0.0f, 1.0f, 1.0f,

               -0.5f,  0.5f, 0.0f,  1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.0f,  0.0f, 0.0f, 1.0f, 1.0f,
                0.5f,  0.5f, 0.0f,  1.0f, 1.0f, 0.0f, 1.0f
        };

        return new Mesh(vertices);
    }

    
    public static Mesh createSphere(int segments, int rings) {
        java.util.ArrayList<Float> vertexList = new java.util.ArrayList<>();

        for (int ring = 0; ring <= rings; ring++) {
            float theta = ring * (float) Math.PI / rings;
            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);

            for (int seg = 0; seg <= segments; seg++) {
                float phi = seg * 2.0f * (float) Math.PI / segments;
                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);

                float x = cosPhi * sinTheta;
                float y = cosTheta;
                float z = sinPhi * sinTheta;

                // Pozisyon
                vertexList.add(x * 0.5f);
                vertexList.add(y * 0.5f);
                vertexList.add(z * 0.5f);

                // Renk (siyah ama biraz gradient için)
                float intensity = 0.1f + (y * 0.5f + 0.5f) * 0.2f;
                vertexList.add(intensity);
                vertexList.add(intensity);
                vertexList.add(intensity);
                vertexList.add(1.0f);
            }
        }

        // İndexleri oluştur ve üçgenlere dönüştür
        java.util.ArrayList<Float> triangleList = new java.util.ArrayList<>();

        for (int ring = 0; ring < rings; ring++) {
            for (int seg = 0; seg < segments; seg++) {
                int current = ring * (segments + 1) + seg;
                int next = current + segments + 1;

                // İlk üçgen
                addVertex(triangleList, vertexList, current);
                addVertex(triangleList, vertexList, next);
                addVertex(triangleList, vertexList, current + 1);

                // İkinci üçgen
                addVertex(triangleList, vertexList, current + 1);
                addVertex(triangleList, vertexList, next);
                addVertex(triangleList, vertexList, next + 1);
            }
        }

        float[] vertices = new float[triangleList.size()];
        for (int i = 0; i < triangleList.size(); i++) {
            vertices[i] = triangleList.get(i);
        }

        return new Mesh(vertices);
    }

    private static void addVertex(java.util.ArrayList<Float> triangleList,
                                  java.util.ArrayList<Float> vertexList,
                                  int index) {
        int startIdx = index * 7; // 7 = 3 pos + 4 color
        for (int i = 0; i < 7; i++) {
            triangleList.add(vertexList.get(startIdx + i));
        }
    }

    // Basit sphere (hızlı test için)
    public static Mesh createSphere() {
        return createSphere(20, 20); // 20 segment, 20 ring
    }

    public static Mesh createBlackHole() {
        // Siyah küre + accretion disk efekti için renkler
        java.util.ArrayList<Float> vertexList = new java.util.ArrayList<>();

        int segments = 30;
        int rings = 30;

        for (int ring = 0; ring <= rings; ring++) {
            float theta = ring * (float) Math.PI / rings;
            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);

            for (int seg = 0; seg <= segments; seg++) {
                float phi = seg * 2.0f * (float) Math.PI / segments;
                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);

                float x = cosPhi * sinTheta;
                float y = cosTheta;
                float z = sinPhi * sinTheta;

                // Pozisyon
                vertexList.add(x * 0.5f);
                vertexList.add(y * 0.5f);
                vertexList.add(z * 0.5f);

                // Renk - kara delik etkisi
                // Merkeze yakınsa tamamen siyah, kenarlara doğru hafif kırmızımsı
                float distanceFromEquator = Math.abs(y);
                float edgeGlow = (1.0f - distanceFromEquator) * 0.3f;

                vertexList.add(edgeGlow); // R - hafif kırmızı
                vertexList.add(0.0f);     // G
                vertexList.add(0.0f);     // B
                vertexList.add(1.0f);     // A
            }
        }

        // İndexleri oluştur
        java.util.ArrayList<Float> triangleList = new java.util.ArrayList<>();

        for (int ring = 0; ring < rings; ring++) {
            for (int seg = 0; seg < segments; seg++) {
                int current = ring * (segments + 1) + seg;
                int next = current + segments + 1;

                addVertex(triangleList, vertexList, current);
                addVertex(triangleList, vertexList, next);
                addVertex(triangleList, vertexList, current + 1);

                addVertex(triangleList, vertexList, current + 1);
                addVertex(triangleList, vertexList, next);
                addVertex(triangleList, vertexList, next + 1);
            }
        }

        float[] vertices = new float[triangleList.size()];
        for (int i = 0; i < triangleList.size(); i++) {
            vertices[i] = triangleList.get(i);
        }

        return new Mesh(vertices);
    }
}
