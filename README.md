# Black Hole Simulation - TLX

A real-time 3D black hole simulation featuring gravitational lensing, accretion disk physics, and spacetime curvature visualization.

![Black Hole Simulation](screenshot.png)

## Features

🌀 **Gravitational Lensing** - Spacetime grid warps around the black hole  
💫 **Accretion Disk** - Thousands of particles orbiting with Keplerian dynamics  
🎨 **Temperature-based Rendering** - Color gradient from blue (hot inner disk) to red (cooler outer disk)  
🎮 **Interactive Camera** - WASD + mouse controls for orbit camera  
⚛️ **Physics Simulation** - Gravitational forces, event horizon, particle dynamics

## Controls

| Key | Action |
|-----|--------|
| **W/A/S/D** | Orbit camera around black hole |
| **Q/E** | Zoom in/out |
| **Arrow Keys** | Pan camera target |
| **Right Mouse + Drag** | Free camera rotation |
| **Mouse Scroll** | Zoom |

## Technical Details

### Architecture
- **Particle System**: GPU-instanced rendering for 5000+ particles
- **Shader-based Warping**: Vertex shader implements simplified Schwarzschild metric
- **Physics Engine**: Custom gravitational force calculations with event horizon detection

### Technologies
- **Java 21** - Core application
- **LWJGL 3** - OpenGL bindings
- **JOML** - Mathematics library
- **GLSL 330** - Shader programming

## Installation

### Prerequisites
- Java 21 or higher
- Maven or Gradle

### Build & Run
```bash
git clone https://github.com/KayaxcOff/BlackHoleSimulation.git
cd tlx-simulation
mvn clean package
java -jar target/tlx-simulation.jar
```

## Project Structure
```
src/main/
├── java/io/github/kayaxcoff/tlx/
│   ├── object/          # Core 3D objects (Mesh, Transform, Shader)
│   ├── rendering/       # Rendering systems (Particles, Grid, BlackHole)
│   ├── scenes/          # Scene management
│   ├── tools/           # Camera, utilities
│   └── callbacks/       # Input handling
└── resources/shaders/   # GLSL shaders
    ├── default.vert/frag
    ├── particle.vert/frag
    └── grid.vert/frag
```

## Physics Model

The simulation uses a simplified model of general relativity:

- **Schwarzschild Radius**: `r_s = 0.5 + (mass × 0.1)`
- **Warp Strength**: `∝ (r_s / r)²` for distances > r_s
- **Orbital Velocity**: `v ∝ 1/√r` (Keplerian orbits)

## Rendering Pipeline

1. **Grid Rendering** (background) - with gravitational warping
2. **3D Objects** - black hole sphere + orbiting cubes
3. **Particle System** (alpha blended) - accretion disk with additive blending

## Performance

- **60 FPS** with 5000 particles on mid-range GPU
- Instanced rendering reduces draw calls
- Efficient vertex shader calculations

## Future Enhancements

- [ ] Photon ring visualization
- [ ] Gravitational redshift effects
- [ ] Time dilation visualization
- [ ] Multiple black hole interactions
- [ ] Relativistic beaming

## Credits

Developed as an educational project exploring:
- Real-time rendering techniques
- GPU programming
- Astrophysics visualization
- Game engine architecture

## License

MIT License - see LICENSE file for details

## References

- [General Relativity Visualization](https://en.wikipedia.org/wiki/Schwarzschild_metric)
- [Accretion Disk Physics](https://en.wikipedia.org/wiki/Accretion_disk)
- [LWJGL Documentation](https://www.lwjgl.org/)

---

**Note**: This is a simplified educational simulation. Real black holes are described by the full Einstein field equations, which are significantly more complex.