package io.github.kayaxcoff.tlx.scenes;

import io.github.kayaxcoff.tlx.object.Mesh;
import io.github.kayaxcoff.tlx.object.Shader;
import io.github.kayaxcoff.tlx.object.SimulationObject;
import io.github.kayaxcoff.tlx.rendering.*;
import io.github.kayaxcoff.tlx.tools.Camera;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class LevelScene extends Scene {

    private Shader shader;
    private Shader particleShader;
    private Shader gridShader;
    private Camera camera;
    private AccretionDisk accretionDisk;
    private ParticleSystem particleSystem;
    private List<SimulationObject> objects;
    private BlackHole blackHole;
    private List<PhysicsSystem> particles;
    private SpaceTimeGrid spaceTimeGrid;

    public LevelScene() {
        System.out.println("LevelScene created");

        this.bgR = 0.1f;
        this.bgG = 0.1f;
        this.bgB = 0.1f;
        this.bgA = 1.0f;
    }

    @Override
    public void Init() {
        this.camera = new Camera(new Vector3f(0, 0, -5), 15.0f);
        this.shader = new Shader("src/main/resources/shaders/default.vert", "src/main/resources/shaders/default.frag");

        this.particleShader = new Shader("src/main/resources/shaders/particle.vert", "src/main/resources/shaders/particle.frag");

        this.gridShader = new Shader("src/main/resources/shaders/grid.vert", "src/main/resources/shaders/grid.frag");

        this.objects = new ArrayList<>();
        this.particles = new ArrayList<>();

        this.particleSystem = new ParticleSystem(1000);

        Mesh blackHoleMesh = Mesh.createBlackHole();
        this.blackHole = new BlackHole(blackHoleMesh, 10.0f);
        this.blackHole.setPosition(0.0f, 0.0f, -5.0f);
        this.blackHole.setRotationSpeed(30.0f);
        this.objects.add(this.blackHole);

        this.accretionDisk = new AccretionDisk(
                new Vector3f(0.0f, 0.0f, -5.0f),
                1.8f,
                5.0f,
                5000
        );
        accretionDisk.setSpawnRate(50.0f);
        accretionDisk.setRotationSpeed(1.5f);

        this.spaceTimeGrid = new SpaceTimeGrid(
                new Vector3f(0.0f, -1.0f, -5.0f),  // Kara deliğin altında
                10.0f,  // Grid boyutu
                30      // Çizgi sayısı
        );
        spaceTimeGrid.setGridColor(0.15f, 0.3f, 0.6f);

        this.createOrbitingParticles(8, 3.0f);
        //this.spawnTestParticles();
    }

    @Override
    public void update(float deltaTime) {
        this.camera.update(deltaTime);
        this.blackHole.update(deltaTime);
        accretionDisk.update(deltaTime);

        for (PhysicsSystem item : this.particles) {
            if (item.isActive()) {
                this.blackHole.applyGravityTo(item, deltaTime);
                item.update(deltaTime);
                item.rotate(100.0f * deltaTime, 50.0f * deltaTime, 0.0f);
            }
        }

        // Grid'i önce çiz (arka planda)
        spaceTimeGrid.render(
                this.gridShader,
                this.camera,
                this.blackHole.getTransform().getPosition(),  // Kara delik pozisyonu
                this.blackHole.getMass(),                     // Kütle
                this.blackHole.getEventHorizonRadius()        // Event horizon
        );

        // Render objects
        for (SimulationObject item : this.objects) {
            if (item.isActive()) {
                item.render(this.shader, this.camera);
            }
        }

        // Render particles (blending için en sona)
        GL30.glEnable(GL30.GL_BLEND);
        GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE);

        accretionDisk.render(particleShader, this.camera);

        GL30.glDisable(GL30.GL_BLEND);
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {
        java.util.Set<Mesh> meshes = new java.util.HashSet<>();
        for (SimulationObject obj : objects) {
            if (obj.getMesh() != null) {
                meshes.add(obj.getMesh());
            }
        }

        for (Mesh mesh : meshes) {
            mesh.cleanup();
        }

        spaceTimeGrid.cleanup();
        accretionDisk.cleanup();
        this.shader.cleanup();
        this.particleShader.cleanup();
        this.gridShader.cleanup();
    }

    private void createOrbitingParticles(int count, float orbitRadius) {
        Mesh particleMesh = Mesh.createCube();

        for (int i = 0; i < count; i++) {
            PhysicsSystem particle = new PhysicsSystem(particleMesh, 1.0f);

            float angle = (float) (2 * Math.PI * i / count);
            float x = orbitRadius * (float) Math.cos(angle);
            float y = orbitRadius * (float) Math.sin(angle);

            particle.setPosition(x, y, -5.0f);
            particle.setScale(0.2f);

            float speed = 2.0f;
            float vx = -speed * (float) Math.sin(angle);
            float vy = speed * (float) Math.cos(angle);
            particle.setVelocity(vx, vy, 0.0f);

            particle.setDrag(1.0f);

            this.particles.add(particle);
            this.objects.add(particle);
        }
    }

    private void spawnTestParticles() {
        for (int i = 0; i < 100; i++) {
            float angle = (float) (Math.random() * Math.PI * 2);
            float radius = 2.0f + (float) Math.random() * 3.0f;

            Vector3f pos = new Vector3f(
                    (float) Math.cos(angle) * radius,
                    (float) (Math.random() - 0.5f) * 2.0f,
                    -5.0f + (float) Math.sin(angle) * radius
            );

            Vector3f vel = new Vector3f(
                    (float) (Math.random() - 0.5f) * 0.1f,  // Daha yavaş hareket
                    (float) (Math.random() - 0.5f) * 0.1f,
                    (float) (Math.random() - 0.5f) * 0.1f
            );

            Vector4f color = new Vector4f(1.0f, 0.5f, 0.0f, 1.0f);

            this.particleSystem.addParticle(new Particle(pos, vel, color, 10.0f, 0.5f)); // 10 saniye lifetime, 0.5 size
        }
    }
}
