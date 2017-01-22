#version 400

in vec2 texCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;

out vec4 fragColor;

// Attenuation is the loss of light intensity over distance
struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

// Point light has color, position, intensity (0-1) and attenuation parameters
struct PointLight {
    vec3 color;
    vec3 position; // Position is in view coordinates
    float intensity;
    Attenuation att;
};

// A material has a base color (if not using a texture) and reflectance index
struct Material {
    vec3 color;
    int useColor;
    float reflectance;
};

uniform sampler2D texture_sampler;
uniform vec3 ambientLight; // Contains a color that affects every fragment
uniform float specularPower; // The exponent used when calculating specular
uniform Material material; // The characteristics of the material
uniform PointLight pointLight;
uniform vec3 cameraPos; // Position in view space coordinates

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);
    
    // Diffuse light
    vec3 lightDirection = light.position - position;
    vec3 toLightSource = normalize(lightDirection);
    float diffuseFactor = max(dot(normal, toLightSource), 0.0);
    diffuseColor = vec4(light.color, 1.0) * light.intensity * diffuseFactor;
    
    // Specular light
    // The camera direction is cameraPos - position but cameraPos is always 
    // 0,0,0 so it cancels to just -position
    vec3 cameraDirection = normalize(-position);
    vec3 fromLightSource = -toLightSource;
    // Reflect the ray from the light around the normal
    vec3 reflectedLight = normalize(reflect(fromLightSource, normal));
    float specularFactor = max( dot(cameraDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColor = specularFactor * material.reflectance * vec4(light.color, 1.0);
    
    // Attenuation
    float distance = length(lightDirection);
    float attenuationInv = light.att.constant + light.att.linear * distance + light.att.exponent * distance * distance;
    
    return (diffuseColor + specColor) / attenuationInv;
};

void main() {
    vec4 baseColor;
	if (material.useColor == 1) {
		baseColor = vec4(material.color, 1);
	} else {
		baseColor = texture(texture_sampler, texCoord);
	}
    
    vec4 lightColor = calcPointLight(pointLight, mvVertexPos, mvVertexNormal);
    vec4 totalLight = vec4(ambientLight, 1.0);
    totalLight += lightColor;
    
    fragColor = baseColor * totalLight;
}