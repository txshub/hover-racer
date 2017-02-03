#version 400

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;

out vec3 normal0;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 worldMatrix;

void main() {
	gl_Position = projectionMatrix * viewMatrix * worldMatrix * vec4(position, 1.0);
	
	normal0 = (worldMatrix * vec4(normal, 0.0)).xyz;
}