package tools;

import equipment.Equipment;
import mycraftworld.MyCraftObject;

public abstract class Tool extends MyCraftObject implements Upgradable, Repairable {

  private final boolean isRepairable;
  private final boolean isUpgradable;
  private int durability;
  protected float attackSpeed;
  protected int attackDamage;
  protected int miningDamage;
  protected Material material;

  public Tool(String name, Material material, Equipment equipment) {
    super(name);
    this.material = material;
    this.durability = material.getDurability();
    this.isRepairable = true;
    this.isUpgradable = true;
    setFlammable();
    if (!equipment.addItem(this)) {
      System.out.println(name + " could not be added to the equipment.");
    }
    this.attackDamage = calculateAttackDamage(material);
    this.attackSpeed = calculateAttackSpeed(material);
    this.miningDamage = calculateMiningDamage(material);
  }

  protected abstract void setFlammable();
  protected abstract int calculateAttackDamage(Material material);
  protected abstract float calculateAttackSpeed(Material material);
  protected abstract int calculateMiningDamage(Material material);

  // DURABILITY
  public int getDurability() {
    return durability;
  }

  public void decreaseDurability() {
    if (durability > 0) {
      durability--;
    } else {
      System.out.println(getName() + " is broken.");
    }
  }

  public int getAttackDamage() {
    return attackDamage;
  }

  public int getMiningDamage() {
    return miningDamage;
  }

  public Material getMaterial() {
    return material;
  }

  public void setMaterial(Material material) {
    this.material = material;
    this.durability = material.getDurability();
    this.attackDamage = calculateAttackDamage(material);
    this.attackSpeed = calculateAttackSpeed(material);
    this.miningDamage = calculateMiningDamage(material);
  }

  @Override
  public String getName() {
    return name;
  }

  public void repair() {
    if (isRepairable) {
      System.out.println("Fixed tool: " + getName());
      this.durability = material.getDurability();
    } else {
      System.out.println("This tool cannot be repaired.");
    }
  }

  @Override
  public void upgrade() {
    if (isUpgradable) {
      switch (this.material) {
        case WOOD:
          this.setMaterial(Material.STONE);
          break;
        case STONE:
          this.setMaterial(Material.IRON);
          break;
        case IRON, GOLD:
          this.setMaterial(Material.DIAMOND);
          break;
        default:
          System.out.println("This material cannot be improved.");
          return;
      }
      System.out.println("Tool improved: " + getName());
      this.durability = this.material.getDurability();
      this.attackDamage = calculateAttackDamage(this.material); // Update attack damage
      this.attackSpeed = calculateAttackSpeed(this.material); // Update attack speed
      this.miningDamage = calculateMiningDamage(this.material); // Update mining damage
    } else {
      System.out.println("This tool cannot be improved.");
    }
  }

  public abstract void use();
}
