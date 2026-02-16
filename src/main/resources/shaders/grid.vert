#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

out vec4 fColor;

uniform mat4 uProjection;
uniform mat4 uView;

void main() {
    vec3 worldPos = aPos;

    vec3 blackHolePos = vec3(0.0, 0.0, -5.0);
    float dist = distance(worldPos, blackHolePos);

    // Bükülme hesapla
    if (dist < 8.0) {
        float strength = (8.0 - dist) / 8.0; // 0 ile 1 arası

        // AŞAĞI ÇEK (uzay-zaman çukuru)
        worldPos.y -= strength * 5.0;

        // KARA DELİĞE DOĞRU ÇEK (radyal)
        vec3 direction = normalize(blackHolePos - worldPos);
        worldPos += direction * strength * 2.5;

        // Renk efekti (turuncu glow)
        fColor = mix(aColor, vec4(1.0, 0.5, 0.0, 1.0), strength * 0.5);
    } else {
        fColor = aColor; // Normal mavi
    }

    gl_Position = uProjection * uView * vec4(worldPos, 1.0);
}