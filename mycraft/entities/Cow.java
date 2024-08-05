package entities;

public class Cow extends Entity {

  public Cow(String name) {
    super(name);
    this.maxHealth = 15;
    this.currentHealth = maxHealth;
    this.damage = 0;
  }

  @Override
  public void hit(Entity entity) {
    System.out.println("The cow deals no damage.");
  }

  @Override
  public void kill() {
    System.out.println(name + " is killed.");
    currentHealth = 0;
  }

  public void onDeath(Entity killer) {
    if (killer instanceof Player player) {
      player.setCurrentHealth(player.getMaxHealth());
    }
  }

  @Override
  public boolean isOnFire() {
    return false;
  }

  @Override
  public String getStatus() {
    return "Cow " + name + " - Health: " + currentHealth + "/" + maxHealth;
  }
}
