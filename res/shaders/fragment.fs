#version 400

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform vec3 color;
uniform int useColor;

void main() {
	if (useColor == 1) {
		fragColor = vec4(color, 1f);
	} else {
		fragColor = texture(texture_sampler, texCoord);
	}
}