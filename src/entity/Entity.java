package entity;

import graphics.model.Model;
import math.Vector3f;

public class Entity {

  private Vector3f pos;
  private Model model;
  
  public Entity(Vector3f pos, Model model) {
    this.pos = pos;
    this.model = model;
  }

  public Vector3f getPos() {
    return pos;
  }

  public void setPos(Vector3f pos) {
    this.pos = pos;
  }

  public Model getModel() {
    return model;
  }

  public void setModel(Model model) {
    this.model = model;
  }
  
  
}
