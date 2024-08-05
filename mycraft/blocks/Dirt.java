package blocks;

import equipment.Equipment;
import tools.Shovel;
import tools.Tool;

public class Dirt extends Block<Shovel> {

  public Dirt(Equipment equipment) {
    super("Dirt", 8, true, Shovel.class, equipment);
  }

  @Override
  public void mine(Tool tool) {
    if (canBeMinedWith(tool)) {
      int remainingHardness = getHardness();
      while (remainingHardness > 0) {
        if (tool.getDurability() <= 0) {
          System.out.println(tool.getName() + " is broken and can no longer be used.");
          break;
        }
        System.out.println("Mining " + getName() + " with " + tool.getName() + "... Remaining hardness: " + remainingHardness);
        remainingHardness -= tool.getMiningDamage();
        tool.decreaseDurability();
        if (remainingHardness <= 0) {
          System.out.println(getName() + " block mined successfully.");
          setHardness(0);
          collect();
          break;
        }
      }
    } else {
      System.out.println("Cannot mine " + getName() + " with " + tool.getName() + ". Incorrect tool.");
    }
  }

  @Override
  public void collect() {
    boolean itemAdded = false;
    Equipment equipment = getEquipment();
    for (Object item : equipment.getItems()) {
      if (item instanceof Block<?> existingBlock && ((Block<?>) item).getName().equals(this.getName())) {
        if (existingBlock.isStackable() && existingBlock.count() < 64) {
          existingBlock.addToStack(1);
          itemAdded = true;
          break;
        }
      }
    }
    if (!itemAdded) {
      Dirt newBlock = new Dirt(equipment);
      newBlock.addToStack(0);
      equipment.addItem(newBlock);
    }
  }

  @Override
  public void place() {
    System.out.println("Placing the " + getName() + " block.");
  }
}
