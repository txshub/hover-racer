package engine.graphics.shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.graphics.model.Material;

public class BasicShader2 extends Shader {

  public BasicShader2() {
    super();
    
    addVertexShader(loadShader("vertex.vs"));
    addFragmentShader(loadShader("fragment.fs"));
    compileShader();
    
    // Create the uniforms
    addUniform("projectionMatrix");
    addUniform("modelViewMatrix");
    addUniform("texture_sampler");
    createMaterialUniform("material");
    addUniform("specularPower");
    addUniform("ambientLight");
    createPointLightUniform("pointLight");
  }

  @Override
  public void bindAttributes() {}
  
  public void updateProjectionMatrix(Matrix4f matrix) {
    setUniform("projectionMatrix", matrix);
  }
  
  public void updateModelViewMatrix(Matrix4f matrix) {
    setUniform("modelViewMatrix", matrix);
  }
  
  public void updateTextureSampler(int value) {
    setUniform("texture_sampler", value);
  }
  
  public void updateMaterial(Material material) {
    setUniform("material.color", material.getColor());
    setUniform("material.useColor", material.isTextured() ? 0 : 1);
    setUniform("material.reflectance", material.getReflectance());
  }
  
  public void updateAmbientLight(Vector3f ambientLight) {
    setUniform("ambientLight", ambientLight);
  }
  
  public void updateSpecularPower(float specularPower) {
    setUniform("specularPower", specularPower);
  }
  
  public void updatePointLight(PointLight pointLight) {
    setUniform("pointLight.color", pointLight.getColor());
    setUniform("pointLight.position", pointLight.getPosition());
    setUniform("pointLight.intensity", pointLight.getIntensity());
    PointLight.Attenuation att = pointLight.getAttenuation();
    setUniform("pointLight.att.constant", att.getConstant());
    setUniform("pointLight.att.linear", att.getLinear());
    setUniform("pointLight.att.exponent", att.getExponent());
  }
  
  public void createPointLightUniform(String uniformName) {
    addUniform(uniformName + ".color");
    addUniform(uniformName + ".position");
    addUniform(uniformName + ".intensity");
    addUniform(uniformName + ".att.constant");
    addUniform(uniformName + ".att.linear");
    addUniform(uniformName + ".att.exponent");
  }
  
  public void createMaterialUniform(String uniformName) {
    addUniform(uniformName + ".color");
    addUniform(uniformName + ".useColor");
    addUniform(uniformName + ".reflectance");
  }

}
