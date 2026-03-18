#version 150

uniform sampler2D DiffuseSampler;
uniform float Time;

in vec2 texCoord;
out vec4 fragColor;

void main() {
    // Determine the "glitch" intensity based on time
    // This creates a pulsing/flickering offset
    float offset = 0.005 * sin(Time * 20.0);

    // Sample channels with a horizontal offset (Chromatic Aberration)
    float r = texture(DiffuseSampler, vec2(texCoord.x + offset, texCoord.y)).r;
    float g = texture(DiffuseSampler, texCoord).g;
    float b = texture(DiffuseSampler, vec2(texCoord.x - offset, texCoord.y)).b;

    // Combine into an RGB vector
    vec3 color = vec3(r, g, b);

    // Convert to Grayscale (Luma weights)
    float gray = dot(color, vec3(0.3, 0.59, 0.11));

    // Mix a tiny bit of the original aberrated color back in
    // so the "fringes" are visible through the gray
    vec3 finalColor = mix(vec3(gray), color, 0.3);

    fragColor = vec4(finalColor, 1.0);
}