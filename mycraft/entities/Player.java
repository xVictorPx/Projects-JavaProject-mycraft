package entities;

import tools.*;


public class Player extends Entity {
  private Tool equippedTool;

  public Player(String name) {
    super(name);
    this.isPlayer = true;
    this.equippedTool = null;
  }

  @Override
  public void hit(Entity entity) {
    while (!entity.isDead() && !this.isDead()) {
      if (this.getEquippedTool() instanceof Sword) {
        System.out.println("Player hits " + entity.getName() + " with a sword.");
        entity.takeDamage(this.getEquippedTool().getAttackDamage());
        this.getEquippedTool().decreaseDurability();
      } else {
        System.out.println("Player can only strike with the sword.");
        break;
      }
      if (!entity.isDead() && entity instanceof Zombie) {
        entity.hit(this);
      }
    }
    if (entity.isDead()) {
      if (entity instanceof Cow) {
        ((Cow) entity).onDeath(this);
      }
    }
  }

  @Override
  public void kill() {
    System.out.println(name + " is killed.");
    currentHealth = 0;
    System.out.println("==============");
    System.out.println("  GAME OVER");
    System.out.println("==============");
    System.exit(0);
  }

  @Override
  public boolean isOnFire() {
    return false;
  }

  @Override
  public String getStatus() {
    return "Player " + name + " - Health: " + currentHealth + "/" + maxHealth;
  }

  public Tool getEquippedTool() {
    return equippedTool;
  }

  public void setEquippedTool(Tool tool) {
    this.equippedTool = tool;
  }

  public void setCurrentHealth(int health) {
    this.currentHealth = Math.min(health, this.maxHealth);
  }

  public int getMaxHealth() {
    return this.maxHealth;
  }
}
