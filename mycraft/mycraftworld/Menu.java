package mycraftworld;

import blocks.*;
import equipment.*;
import entities.*;
import tools.*;

import java.util.Random;
import java.util.Scanner;

public class Menu {
  private final WorldMap worldMap;
  private Player player;
  private final Equipment equipment;
  private boolean isMapGenerated = false;

  public Menu() {
    this.worldMap = new WorldMap(10, 10);
    this.equipment = new Equipment();
  }

  public void generateMap() {
    if (isMapGenerated) {
      System.out.println("Map has already been generated. Cannot generate again.");
      return;
    }

//     Stworzenie gracza
    player = new Player("Hero");
    worldMap.setPlayer(player, 0, 0); // Na początku gracz na pozycji (0,0)

//    Stworzenie narzędzi
    Tool sword = new Sword("Sword", Material.WOOD, equipment);
    Tool pickaxe = new Pickaxe("Pickaxe", Material.WOOD, equipment);
    Tool axe = new Axe("Axe", Material.WOOD, equipment);
    Tool shovel = new Shovel("Shovel", Material.WOOD, equipment);

//    Początkowe narzędzie - miecz
    player.setEquippedTool(sword);
    equipment.setActiveTool(sword);

//     Stworzenie bloków i mobów
    createBlocksAndMobs();

//     Ustawienie mapy jako wygenerowanej
    isMapGenerated = true;

//     Wyświetlenie mapy i sąsiednich do gracza bloków
    System.out.println("Map with randomly placed blocks and mobs:");
    worldMap.displayMap();
    worldMap.displayAdjacentBlockStatus();
    printSeparator();
  }

  private void createBlocksAndMobs() {
    Dirt[] dirtBlocks = new Dirt[9];
    Stone[] stoneBlocks = new Stone[9];
    Wood[] woodBlocks = new Wood[9];
    Zombie[] zombies = new Zombie[10];
    Cow[] cows = new Cow[10];

    for (int i = 0; i < 9; i++) {
      dirtBlocks[i] = new Dirt(equipment);
      stoneBlocks[i] = new Stone(equipment);
      woodBlocks[i] = new Wood(equipment);
    }

    for (int i = 0; i < 10; i++) {
      zombies[i] = new Zombie("Zombie " + (i + 1));
      cows[i] = new Cow("Cow " + (i + 1));
    }

//     Ustawienie bloków i mobów w losowych miejscach
    Random random = new Random();

    for (Dirt dirt : dirtBlocks) {
      placeRandomly(worldMap, dirt, random);
    }

    for (Stone stone : stoneBlocks) {
      placeRandomly(worldMap, stone, random);
    }

    for (Wood wood : woodBlocks) {
      placeRandomly(worldMap, wood, random);
    }

    for (Zombie zombie : zombies) {
      placeRandomly(worldMap, zombie, random);
    }

    for (Cow cow : cows) {
      placeRandomly(worldMap, cow, random);
    }
  }

  private void placeRandomly(WorldMap worldMap, Object obj, Random random) {
    int x, y;
    boolean placed = false;

    while (!placed) {
      x = random.nextInt(worldMap.getWidth());
      y = random.nextInt(worldMap.getHeight());
      if (x == 0 && y == 0) continue;

      if (obj instanceof Dirt) {
        placed = worldMap.placeBlock(x, y, (Dirt) obj);
      } else if (obj instanceof Stone) {
        placed = worldMap.placeBlock(x, y, (Stone) obj);
      } else if (obj instanceof Wood) {
        placed = worldMap.placeBlock(x, y, (Wood) obj);
      } else if (obj instanceof Zombie) {
        placed = worldMap.placeEntity(x, y, (Zombie) obj);
      } else if (obj instanceof Cow) {
        placed = worldMap.placeEntity(x, y, (Cow) obj);
      }
    }
  }

  public void displayMenu() {
    Scanner scanner = new Scanner(System.in);
    boolean exit = false;

    while (!exit) {
      printSeparator();
      System.out.println("Menu:");
      System.out.println("1. Generate Map");
      System.out.println("2. Display Map Legend");
      if (isMapGenerated) {
        System.out.println("3. Display Map");
        System.out.println("4. Display Inventory");
        System.out.println("5. Move Player");
        System.out.println("6. Mine Blocks/Kill Mobs");
        System.out.println("7. Display Player Status");
      }
      System.out.println("8. Exit");
      System.out.print("Choose an option: ");

      String input = scanner.nextLine().trim().toUpperCase();
      int choice = parseChoice(input);

      switch (choice) {
        case 1:
          generateMap();
          break;
        case 2:
          displayMapLegend();
          waitForEnter(scanner);
          break;
        case 3:
          handleDisplayMap(scanner);
          break;
        case 4:
          handleDisplayInventory(scanner);
          break;
        case 5:
          handleMovePlayer(scanner);
          break;
        case 6:
          handleMineOrKill(scanner);
          break;
        case 7:
          if (isMapGenerated) {
            displayPlayerStatus();
            waitForEnter(scanner);
          } else {
            System.out.println("Map has not been generated yet.");
            waitForEnter(scanner);
          }
          break;
        case 8:
          exit = true;
          break;
        default:
          System.out.println("Invalid choice, please try again.");
          waitForEnter(scanner);
      }
    }
  }

  private int parseChoice(String input) {
    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private void displayMapLegend() {
    printSeparator();
    System.out.println("Map Legend:");
    System.out.println("P - Player");
    System.out.println("C - Cow");
    System.out.println("Z - Zombie");
    System.out.println("W - Wood");
    System.out.println("S - Stone");
    System.out.println("D - Dirt");
  }

  private void handleDisplayMap(Scanner scanner) {
    if (isMapGenerated) {
      worldMap.displayMap();
      worldMap.displayAdjacentBlockStatus();
      waitForEnter(scanner);
    } else {
      System.out.println("Map has not been generated yet.");
      waitForEnter(scanner);
    }
  }

  private void handleDisplayInventory(Scanner scanner) {
    if (isMapGenerated) {
      displayInventory(scanner);
    } else {
      System.out.println("Map has not been generated yet.");
      waitForEnter(scanner);
    }
  }

  private void handleMovePlayer(Scanner scanner) {
    if (isMapGenerated) {
      movePlayer(scanner);
    } else {
      System.out.println("Map has not been generated yet.");
      waitForEnter(scanner);
    }
  }

  private void handleMineOrKill(Scanner scanner) {
    if (isMapGenerated) {
      mineOrKill(scanner);
    } else {
      System.out.println("Map has not been generated yet.");
      waitForEnter(scanner);
    }
  }

  private void displayPlayerStatus() {
    printSeparator();
    System.out.println(player.getStatus());
    System.out.println("On fire: " + player.isOnFire());
  }

  private void displayInventory(Scanner scanner) {
    boolean backToMainMenu = false;
    while (!backToMainMenu) {
      printSeparator();
      System.out.println("Inventory:");
      equipment.displayItems();
      System.out.println("Choose an action:");
      System.out.println("1. Select Tool to Use");
      System.out.println("2. Select Block to Place");
      System.out.println("3. Repair Tool");
      System.out.println("4. Upgrade Tool");
      System.out.println("R. Return to Main Menu");
      System.out.print("Choose an option: ");

      String input = scanner.nextLine().trim().toUpperCase();

      switch (input) {
        case "1":
          selectTool(scanner);
          break;
        case "2":
          selectBlock(scanner);
          break;
        case "3":
          repairTool(scanner);
          break;
        case "4":
          upgradeTool(scanner);
          break;
        case "R":
          backToMainMenu = true;
          break;
        default:
          System.out.println("Invalid choice, please try again.");
      }
    }
  }

  private void mineOrKill(Scanner scanner) {
    boolean backToMainMenu = false;
    while (!backToMainMenu) {
      printSeparator();
      System.out.println("Mine Blocks/Kill Mobs:");
      System.out.println("W - Up");
      System.out.println("S - Down");
      System.out.println("A - Left");
      System.out.println("D - Right");
      System.out.println("R - Return to Main Menu");
      System.out.print("Choose a direction: ");

      String direction = scanner.nextLine().trim().toUpperCase();

      if (direction.equals("R")) {
        backToMainMenu = true;
        continue;
      }

      int[] delta = getDelta(direction);

      if (delta == null) {
        System.out.println("Invalid direction, please try again.");
        continue;
      }

      WorldMap.Position playerPosition = worldMap.getPlayerPosition();
      WorldMap.Position targetPosition = new WorldMap.Position(playerPosition.getX() + delta[0], playerPosition.getY() + delta[1]);

      if (worldMap.getBlocks().containsKey(targetPosition)) {
        Block<?> block = worldMap.getBlocks().get(targetPosition);
        if (player.getEquippedTool() == null || !block.canBeMinedWith(player.getEquippedTool())) {
          System.out.println("Cannot mine " + block.getName() + " with the current tool. Please select a suitable tool from the inventory.");
        } else {
          worldMap.mineBlock(targetPosition.getX(), targetPosition.getY(), player.getEquippedTool());
        }
      } else if (worldMap.getEntities().containsKey(targetPosition)) {
        Entity entity = worldMap.getEntities().get(targetPosition);
        if (entity instanceof Player) {
          System.out.println("Cannot attack the player.");
        } else {
          worldMap.killEntity(targetPosition.getX(), targetPosition.getY());
        }
      } else {
        System.out.println("No block or entity in the selected direction.");
      }

      waitForEnter(scanner);
    }
  }

  private int[] getDelta(String direction) {
    return switch (direction) {
      case "W" -> new int[]{0, -1};
      case "S" -> new int[]{0, 1};
      case "A" -> new int[]{-1, 0};
      case "D" -> new int[]{1, 0};
      default -> null;
    };
  }

  private boolean selectTool(Scanner scanner) {
    printSeparator();
    System.out.println("Select a tool to use:");
    int index = 1;
    for (Object item : equipment.getItems()) {
      if (item instanceof Tool tool) {
        String active = (tool == equipment.getActiveTool()) ? " (Active)" : "";
        System.out.println(index + ". " + tool.getName() + " (Material: " + tool.getMaterial() +"; Durability: " + tool.getDurability() + "; Mining Damage: "+ tool.getMiningDamage()+"; Attack Damage: " + tool.getAttackDamage() +")" + active);
        index++;
      }
    }
    System.out.print("Choose an option: ");
    String input = scanner.nextLine().trim().toUpperCase();
    int choice = parseChoice(input);

    if (choice == -1) {
      System.out.println("Invalid choice, please try again.");
      return false;
    }

    return setActiveTool(choice);
  }

  private boolean setActiveTool(int choice) {
    int index = 1;
    for (Object item : equipment.getItems()) {
      if (item instanceof Tool) {
        if (index == choice) {
          player.setEquippedTool((Tool) item);
          equipment.setActiveTool((Tool) item);
          System.out.println("Equipped tool: " + ((Tool) item).getName());
          ((Tool) item).use();
          return true;
        }
        index++;
      }
    }
    System.out.println("Invalid choice, please try again.");
    return false;
  }

  private boolean selectBlock(Scanner scanner) {
    printSeparator();
    System.out.println("Select a block to place:");
    int index = 1;
    for (Object item : equipment.getItems()) {
      if (item instanceof Block) {
        System.out.println(index + ". " + ((Block<?>) item).getName());
        index++;
      }
    }
    System.out.print("Choose an option: ");
    String input = scanner.nextLine().trim().toUpperCase();
    int choice = parseChoice(input);

    if (choice == -1) {
      System.out.println("Invalid choice, please try again.");
      return false;
    }

    return placeSelectedBlock(choice, scanner);
  }

  private boolean placeSelectedBlock(int choice, Scanner scanner) {
    int index = 1;
    for (Object item : equipment.getItems()) {
      if (item instanceof Block) {
        if (index == choice) {
          Block<?> block = (Block<?>) item;
          return placeBlockInDirection(block, scanner);
        }
        index++;
      }
    }
    System.out.println("Invalid choice, please try again.");
    return false;
  }

  private boolean placeBlockInDirection(Block<?> block, Scanner scanner) {
    boolean validDirection = false;
    while (!validDirection) {
      printSeparator();
      System.out.println("Choose a direction to place the block:");
      System.out.println("W - Up");
      System.out.println("S - Down");
      System.out.println("A - Left");
      System.out.println("D - Right");
      System.out.println("R - Return to Main Menu");
      System.out.print("Choose a direction: ");
      String direction = scanner.nextLine().trim().toUpperCase();
      if (direction.equals("R")) return false;
      int[] delta = getDelta(direction);
      if (delta == null) {
        System.out.println("Invalid direction, please try again.");
        continue;
      }
      WorldMap.Position playerPosition = worldMap.getPlayerPosition();
      int x = playerPosition.getX() + delta[0];
      int y = playerPosition.getY() + delta[1];
      if (worldMap.placeBlock(x, y, block)) {
        if (block instanceof Stackable) {
          ((Stackable) block).removeFromStack(1);
          if (((Stackable) block).count() == 0) {
            equipment.removeItem(block);
          }
        }
        System.out.println("Placed block: " + block.getName() + " at (" + x + ", " + y + ")");
        return true;
      } else {
        System.out.println("Cannot place block at the selected position.");
        return false;
      }
    }
    return false;
  }

  private boolean repairTool(Scanner scanner) {
    printSeparator();
    System.out.println("Select a tool to repair:");
    int index = 1;
    for (Object item : equipment.getItems()) {
      if (item instanceof Tool tool) {
        String active = (tool == equipment.getActiveTool()) ? " (Active)" : "";
        System.out.println(index + ". " + tool.getName() + " (Material: " + tool.getMaterial() +"; Durability: " + tool.getDurability() + "; Mining Damage: "+ tool.getMiningDamage()+"; Attack Damage: " + tool.getAttackDamage() +")" + active);
        index++;
      }
    }
    System.out.print("Choose an option: ");
    String input = scanner.nextLine().trim().toUpperCase();
    int choice = parseChoice(input);

    if (choice == -1) {
      System.out.println("Invalid choice, please try again.");
      return false;
    }

    return repairSelectedTool(choice);
  }

  private boolean repairSelectedTool(int choice) {
    int index = 1;
    for (Object item : equipment.getItems()) {
      if (item instanceof Tool) {
        if (index == choice) {
          Tool tool = (Tool) item;
          tool.repair();
          return true;
        }
        index++;
      }
    }
    System.out.println("Invalid choice, please try again.");
    return false;
  }

  private boolean upgradeTool(Scanner scanner) {
    printSeparator();
    System.out.println("Select a tool to upgrade:");
    int index = 1;
    for (Object item : equipment.getItems()) {
      if (item instanceof Tool tool) {
        String active = (tool == equipment.getActiveTool()) ? " (Active)" : "";
        System.out.println(index + ". " + tool.getName() + " (Material: " + tool.getMaterial() +"; Durability: " + tool.getDurability() + "; Mining Damage: "+ tool.getMiningDamage()+"; Attack Damage: " + tool.getAttackDamage() +")" + active);
        index++;
      }
    }
    System.out.print("Choose an option: ");
    String input = scanner.nextLine().trim().toUpperCase();
    int choice = parseChoice(input);

    if (choice == -1) {
      System.out.println("Invalid choice, please try again.");
      return false;
    }

    return upgradeSelectedTool(choice);
  }

  private boolean upgradeSelectedTool(int choice) {
    int index = 1;
    for (Object item : equipment.getItems()) {
      if (item instanceof Tool) {
        if (index == choice) {
          Tool tool = (Tool) item;
          Material oldMaterial = tool.getMaterial();
          tool.upgrade();
          if (tool.getMaterial() == oldMaterial) {
            System.out.println("This tool cannot be improved.");
          }
          return true;
        }
        index++;
      }
    }
    System.out.println("Invalid choice, please try again.");
    return false;
  }

  private void movePlayer(Scanner scanner) {
    boolean moving = true;

    while (moving) {
      printSeparator();
      System.out.println("Move Player:");
      System.out.println("W - Move Up");
      System.out.println("S - Move Down");
      System.out.println("A - Move Left");
      System.out.println("D - Move Right");
      System.out.println("R - Exit Move Menu");
      System.out.print("Choose a direction: ");

      String direction = scanner.nextLine().trim().toUpperCase();

      if (direction.equals("R")) {
        moving = false;
        continue;
      }

      int[] delta = getDelta(direction);

      if (delta == null) {
        System.out.println("Invalid direction, please try again.");
        continue;
      }

      if (worldMap.movePlayer(delta[0], delta[1])) {
        worldMap.displayMap();
        worldMap.displayAdjacentBlockStatus();
      }
    }
  }

  private void waitForEnter(Scanner scanner) {
    System.out.println("Press Enter to return to the menu...");
    scanner.nextLine();
  }

  private void printSeparator() {
    System.out.println("------------------------------------------------");
  }
}
