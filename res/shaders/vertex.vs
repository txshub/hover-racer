#version 400

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 inTexCoord;
layout (location = 2) in vec3 vertexNormal;

out vec2 texCoord;
out vec3 mvVertexNormal;
out vec3 mvVertexPos;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

void main() {
    vec4 mvPos = modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPos;
    texCoord = inTexCoord;
    // w = 0.0 because we want rotation and scale but not translation
    mvVertexNormal = normalize(modelViewMatrix * vec4(vertexNormal, 0.0)).xyz;
    mvVertexPos = mvPos.xyz;
}