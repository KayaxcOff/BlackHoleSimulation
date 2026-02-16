package io.github.kayaxcoff.tlx.rendering;

import io.github.kayaxcoff.tlx.object.Shader;
import io.github.kayaxcoff.tlx.tools.Camera;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class AccretionDisk {

    private final ParticleSystem particleSystem;
    private final Vector3f center;
    private float innerRadius;
    private float outerRadius;
    private float thickness;
    private float rotationSpeed;
    private float spawnRate; // Saniyede kaç particle spawn olacak
    private float timeSinceLastSpawn;

    public AccretionDisk(Vector3f center, float innerRadius, float outerRadius, int maxParticles) {
        this.center = new Vector3f(center);
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.thickness = 0.3f; // Diskin kalınlığı
        this.rotationSpeed = 2.0f; // Radyan/saniye
        this.spawnRate = 100.0f; // Saniyede 100 particle
        this.timeSinceLastSpawn = 0.0f;

        this.particleSystem = new ParticleSystem(maxParticles);

        // İlk particle'ları oluştur
        initializeParticles(maxParticles / 2);
    }

    private void initializeParticles(int count) {
        for (int i = 0; i < count; i++) {
            spawnParticle();
        }
    }

    private void spawnParticle() {
        // Disk içinde rastgele bir konum
        float radius = innerRadius + (float) Math.random() * (outerRadius - innerRadius);
        float angle = (float) (Math.random() * Math.PI * 2);
        float height = ((float) Math.random() - 0.5f) * thickness;

        Vector3f position = new Vector3f(
                center.x + radius * (float) Math.cos(angle),
                center.y + height,
                center.z + radius * (float) Math.sin(angle)
        );

        // Orbital hız hesapla (Kepler yasalarına benzer)
        // Merkeze yakın = daha hızlı, uzak = daha yavaş
        float orbitalSpeed = rotationSpeed / (float) Math.sqrt(radius);

        // Teğetsel yön (dönme yönü)
        float tangentX = -(float) Math.sin(angle);
        float tangentZ = (float) Math.cos(angle);

        Vector3f velocity = new Vector3f(
                tangentX * orbitalSpeed,
                (float) (Math.random() - 0.5f) * 0.1f, // Hafif yukarı-aşağı hareket
                tangentZ * orbitalSpeed
        );

        // Renk - merkeze yakınsa mavi/beyaz, uzaksa kırmızı
        Vector4f color = getColorForRadius(radius);

        // Lifetime - sürekli yenilenir
        float lifetime = 5.0f + (float) Math.random() * 5.0f;

        particleSystem.addParticle(new Particle(position, velocity, color, lifetime));
    }

    private Vector4f getColorForRadius(float radius) {
        // Normalize radius (0 = inner, 1 = outer)
        float t = (radius - innerRadius) / (outerRadius - innerRadius);

        // İçten dışa renk geçişi:
        // İç (sıcak): Beyaz-Mavi (çok sıcak)
        // Orta: Sarı-Turuncu
        // Dış (soğuk): Kırmızı-Koyu kırmızı

        Vector4f color = new Vector4f();

        if (t < 0.3f) {
            // İç bölge - Beyaz/Mavi
            float localT = t / 0.3f;
            color.x = 0.5f + localT * 0.5f; // Mavi -> Beyaz
            color.y = 0.7f + localT * 0.3f;
            color.z = 1.0f;
            color.w = 0.8f + (float) Math.random() * 0.2f;
        } else if (t < 0.7f) {
            // Orta bölge - Sarı/Turuncu
            float localT = (t - 0.3f) / 0.4f;
            color.x = 1.0f;
            color.y = 0.9f - localT * 0.4f;
            color.z = 0.3f - localT * 0.3f;
            color.w = 0.7f + (float) Math.random() * 0.2f;
        } else {
            // Dış bölge - Kırmızı/Koyu kırmızı
            float localT = (t - 0.7f) / 0.3f;
            color.x = 1.0f - localT * 0.3f;
            color.y = 0.3f - localT * 0.3f;
            color.z = 0.0f;
            color.w = 0.5f + (float) Math.random() * 0.3f;
        }

        return color;
    }

    public void update(float deltaTime) {
        // Particle'ları güncelle
        particleSystem.update(deltaTime);

        // Yeni particle spawn et
        timeSinceLastSpawn += deltaTime;
        float spawnInterval = 1.0f / spawnRate;

        while (timeSinceLastSpawn >= spawnInterval) {
            spawnParticle();
            timeSinceLastSpawn -= spawnInterval;
        }
    }

    public void render(Shader shader, Camera camera) {
        particleSystem.render(shader, camera);
    }

    public void cleanup() {
        particleSystem.cleanup();
    }

    // Getters/Setters
    public void setCenter(Vector3f center) {
        this.center.set(center);
    }

    public void setCenter(float x, float y, float z) {
        this.center.set(x, y, z);
    }

    public float getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(float innerRadius) {
        this.innerRadius = innerRadius;
    }

    public float getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(float outerRadius) {
        this.outerRadius = outerRadius;
    }

    public float getThickness() {
        return thickness;
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public float getSpawnRate() {
        return spawnRate;
    }

    public void setSpawnRate(float spawnRate) {
        this.spawnRate = spawnRate;
    }

    public int getParticleCount() {
        return particleSystem.getParticleCount();
    }
}