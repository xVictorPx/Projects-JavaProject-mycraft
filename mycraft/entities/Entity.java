package entities;

import mycraftworld.MyCraftObject;

public abstract class Entity extends MyCraftObject {

  protected int maxHealth;
  protected int currentHealth;
  protected int damage;
  protected boolean isPlayer;

  public Entity(String name) {
    super(name);
    this.maxHealth = 100;
    this.currentHealth = maxHealth;
    this.damage = 10;
    this.isPlayer = false;
  }

  public String getName() {
    return name;
  }

  public int getCurrentHealth() {
    return currentHealth;
  }

  public void takeDamage(int damage) {
    this.currentHealth -= damage;
    if (this.currentHealth < 0) {
      this.currentHealth = 0;
    }
    System.out.println(name + " health: " + currentHealth);
    if (this.currentHealth <= 0) {
      this.kill();
    }
  }

  public int getAttackDamage() {
    return this.damage;
  }

  public boolean isDead() {
    return this.currentHealth <= 0;
  }

  public abstract void hit(Entity entity);
  public abstract void kill();
  public abstract boolean isOnFire();
  public abstract String getStatus();
}
