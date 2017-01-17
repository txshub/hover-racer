#version 400

layout (location = 0) in vec3 position;

uniform mat4 worldMatrix;

void main() {
	gl_Position = worldMatrix * vec4(position, 1.0);
}