package blocks;

import equipment.Equipment;
import equipment.Stackable;
import tools.Tool;

public abstract class Block<T extends Tool> implements Stackable {
  private final String name;
  private int hardness;
  private final boolean stackable;
  private final Class<T> toolClass;
  private final Equipment equipment;
  private int count;

  public Block(String name, int hardness, boolean stackable, Class<T> toolClass, Equipment equipment) {
    this.name = name;
    this.hardness = hardness;
    this.stackable = stackable;
    this.toolClass = toolClass;
    this.equipment = equipment;
    this.count = 1;
  }

  public String getName() {
    return name;
  }

  public int getHardness() {
    return hardness;
  }

  public void setHardness(int hardness) {
    this.hardness = hardness;
  }

  public boolean isStackable() {
    return stackable;
  }

  @Override
  public int count() {
    return count;
  }

  @Override
  public void addToStack(int count) {
    this.count += count;
  }

  @Override
  public void removeFromStack(int count) {
    this.count -= count;
  }

  public Equipment getEquipment() {
    return equipment;
  }

  public Class<T> getToolClass() {
    return toolClass;
  }

  public boolean canBeMinedWith(Tool tool) {
    return toolClass.isInstance(tool);
  }

  public abstract void mine(Tool tool);

  public abstract void collect();

  public abstract void place();
}
