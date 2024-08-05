package tools;

import equipment.Equipment;

public class Shovel extends Tool {

  public Shovel(String name, Material material, Equipment equipment) {
    super(name, material, equipment);
    this.attackDamage = calculateAttackDamage(material);
    this.attackSpeed = calculateAttackSpeed(material);
    this.miningDamage = calculateMiningDamage(material);
  }

  @Override
  protected void setFlammable() {
    this.isFlammable = this.material == Material.WOOD;
  }

  @Override
  protected int calculateAttackDamage(Material material) {
    return switch (material) {
      case WOOD, GOLD -> 1;
      case STONE -> 2;
      case IRON -> 3;
      case DIAMOND -> 4;
    };
  }

  @Override
  protected float calculateAttackSpeed(Material material) {
    return switch (material) {
      case WOOD, STONE -> 0.8f;
      case IRON -> 0.9f;
      case GOLD, DIAMOND -> 1.0f;
    };
  }
  @Override
  protected int calculateMiningDamage(Material material) {
    return switch (material) {
      case WOOD, GOLD -> 2;
      case STONE -> 4;
      case IRON -> 6;
      case DIAMOND -> 8;
    };
  }

  @Override
  public void use() {
    System.out.println("Digging with the shovel made of " + material);
  }

  public int getMiningDamage() {
    return miningDamage;
  }
}
