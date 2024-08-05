package tools;

import equipment.Equipment;

public class Sword extends Tool {

  public Sword(String name, Material material, Equipment equipment) {
    super(name, material, equipment);

    // Specific values for Sword
    this.attackDamage = calculateAttackDamage(material);
    this.attackSpeed = calculateAttackSpeed(material);
  }

  @Override
  protected void setFlammable() {
    this.isFlammable = this.material == Material.WOOD;
  }

  @Override
  protected int calculateAttackDamage(Material material) {
    return switch (material) {
      case WOOD, GOLD -> 2;
      case STONE -> 4;
      case IRON -> 6;
      case DIAMOND -> 8;
    };
  }

  @Override
  protected float calculateAttackSpeed(Material material) {
    return 1.6f;
  }

  @Override
  protected int calculateMiningDamage(Material material) {
    return 0;
  }

  @Override
  public void use() {
    System.out.println("Swinging the sword made of " + material);
  }

  @Override
  public int getMiningDamage() {
    return 0; // Swords do not have mining damage
  }
}
