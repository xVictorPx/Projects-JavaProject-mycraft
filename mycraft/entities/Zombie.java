package entities;

public class Zombie extends Entity {

  public Zombie(String name) {
    super(name);
    this.maxHealth = 30;
    this.currentHealth = maxHealth;
    this.damage = 5;
  }

  @Override
  public void hit(Entity entity) {
    if (this.isDead()) {
      return;
    }
    if (entity instanceof Player) {
      System.out.println("Zombie hits the player.");
      entity.takeDamage(this.getAttackDamage());
    }
  }

  @Override
  public void kill() {
    System.out.println(name + " is killed.");
    currentHealth = 0;
  }

  @Override
  public boolean isOnFire() {
    return false;
  }

  @Override
  public String getStatus() {
    return "Zombie " + name + " - Health: " + currentHealth + "/" + maxHealth;
  }
}
