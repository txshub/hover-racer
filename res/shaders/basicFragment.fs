#version 400

in vec3 normal0;

uniform vec3 sun;

const float ambientLight = 0.1;

void main() {
	float brightness = max(ambientLight, dot(normal0, sun));

	gl_FragColor = brightness * vec4(1.0, 1.0, 1.0, 1.0);
}