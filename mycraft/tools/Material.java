package tools;

public enum Material {
  WOOD(59),
  STONE(131),
  IRON(250),
  DIAMOND(1561),
  GOLD(32);

  private final int durability;

  Material(int durability) {
    this.durability = durability;
  }

  public int getDurability() {
    return durability;
  }
}
