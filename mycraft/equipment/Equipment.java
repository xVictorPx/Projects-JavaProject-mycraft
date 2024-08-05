package equipment;

import java.util.ArrayList;
import java.util.List;
import blocks.*;
import tools.*;

public class Equipment {
  private static final int MAX_ITEMS = 36;
  private final List<Object> items;
  private Tool activeTool;

  public Equipment() {
    items = new ArrayList<>();
  }

  public boolean addItem(Object item) {
    if (items.size() >= MAX_ITEMS) {
      System.out.println("Inventory is full. Cannot add more items.");
      return false;
    }

    if (item instanceof Stackable) {
      return addStackableItem((Stackable) item);
    } else if (item instanceof Tool) {
      return addNonStackableItem(item);
    } else {
      System.out.println("Unsupported item type.");
      return false;
    }
  }

  private boolean addStackableItem(Stackable stackableItem) {
    boolean added = false;
    for (Object obj : items) {
      if (obj instanceof Stackable && ((Stackable) obj).canStackWith(stackableItem)) {
        Stackable existingItem = (Stackable) obj;
        int availableSpace = 64 - existingItem.count();
        if (availableSpace > 0) {
          int toAdd = Math.min(availableSpace, stackableItem.count());
          existingItem.addToStack(toAdd);
          stackableItem.removeFromStack(toAdd);
          if (stackableItem.count() > 0) {
            items.add(stackableItem);
          }
          added = true;
          break;
        }
      }
    }
    if (!added) {
      items.add(stackableItem);
    }
    return true;
  }

  private boolean addNonStackableItem(Object item) {
    for (Object obj : items) {
      if (obj.getClass().equals(item.getClass())) {
        System.out.println("Cannot add another " + item.getClass().getSimpleName() + ". Only one instance allowed.");
        return false;
      }
    }
    items.add(item);
    return true;
  }

  public void removeItem(Object item) {
    items.remove(item);
  }

  public List<Object> getItems() {
    return items;
  }

  public void setActiveTool(Tool tool) {
    this.activeTool = tool;
  }

  public Tool getActiveTool() {
    return activeTool;
  }

  public void displayItems() {
    for (Object item : items) {
      if (item instanceof Stackable stackableItem) {
        System.out.println(item.getClass().getSimpleName() + " - " + stackableItem.count() + " items");
      } else if (item instanceof Tool toolItem) {
        String active = (toolItem == activeTool) ? " (Active)" : "";
        System.out.println(toolItem.getClass().getSimpleName() + " (Material: " + toolItem.getMaterial() +"; Durability: " + toolItem.getDurability() + "; Mining Damage: "+ toolItem.getMiningDamage()+"; Attack Damage: " + toolItem.getAttackDamage() +")" + active);
      } else if (item instanceof Block<?> blockItem) {
        System.out.println(blockItem.getName() + " - " + blockItem.count() + " items");
      } else {
        System.out.println(item.getClass().getSimpleName());
      }
    }
  }

  public boolean decrementStackableItem(Stackable item) {
    for (Object obj : items) {
      if (obj instanceof Stackable && ((Stackable) obj).canStackWith(item)) {
        Stackable stackableItem = (Stackable) obj;
        stackableItem.removeFromStack(1);
        if (stackableItem.count() == 0) {
          items.remove(stackableItem);
        }
        return true;
      }
    }
    return false;
  }
}
