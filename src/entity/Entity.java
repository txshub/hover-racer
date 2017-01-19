package entity;

import graphics.model.Model;
import math.Vector3f;

public class Entity {

  private Vector3f pos;
  private Vector3f rot;
  private float scale;
  private Model model;
  
  public Entity(Vector3f pos, Vector3f rot, float scale, Model model) {
    this.pos = pos;
    this.rot = rot;
    this.scale = scale;
    this.model = model;
  }

  public Vector3f getPos() {
    return pos;
  }

  public void setPos(Vector3f pos) {
    this.pos = pos;
  }
  
  public Vector3f getRot() {
    return rot;
  }
  
  public void setRot(Vector3f rot) {
    this.rot = rot;
  }

  public Model getModel() {
    return model;
  }

  public void setModel(Model model) {
    this.model = model;
  }

  public float getScale() {
    return scale;
  }

  public void setScale(float scale) {
    this.scale = scale;
  }
  
  
}
