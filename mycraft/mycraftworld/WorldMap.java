package mycraftworld;

import blocks.*;
import entities.*;
import tools.*;

import java.util.HashMap;
import java.util.Map;

public class WorldMap {
  private final int width;
  private final int height;
  private final Map<Position, Block<?>> blocks;
  private final Map<Position, Entity> entities;
  private Player player;

  public WorldMap(int width, int height) {
    this.width = width;
    this.height = height;
    this.blocks = new HashMap<>();
    this.entities = new HashMap<>();
  }

  public void setPlayer(Player player, int x, int y) {
    if (isValidPosition(x, y)) {
      System.out.println("Invalid position: (" + x + ", " + y + ")");
      return;
    }
    this.player = player;
    Position position = new Position(x, y);
    entities.put(position, player);
    System.out.println("Placed player at (" + x + ", " + y + ")");
  }

  public boolean placeBlock(int x, int y, Block<?> block) {
    Position position = new Position(x, y);
    if (isValidPosition(x, y)) {
      System.out.println("Invalid position: (" + x + ", " + y + ")");
      return false;
    }
    if (blocks.containsKey(position)) {
      System.out.println("Position already occupied: (" + x + ", " + y + ")");
      return false;
    }
    blocks.put(position, block);
    System.out.println("Placed " + block.getName() + " at (" + x + ", " + y + ")");
    block.place();
    return true;
  }

  public boolean placeEntity(int x, int y, Entity entity) {
    Position position = new Position(x, y);
    if (isValidPosition(x, y)) {
      System.out.println("Invalid position: (" + x + ", " + y + ")");
      return false;
    }
    if (entities.containsKey(position)) {
      System.out.println("Position already occupied: (" + x + ", " + y + ")");
      return false;
    }
    entities.put(position, entity);
    System.out.println("Placed " + entity.getName() + " at (" + x + ", " + y + ")");
    return true;
  }

  public boolean mineBlock(int x, int y, Tool tool) {
    Position position = new Position(x, y);
    if (isValidPosition(x, y)) {
      System.out.println("Invalid position: (" + x + ", " + y + ")");
      return false;
    }
    if (isAdjacentToPlayer(x, y)) {
      System.out.println("Block at (" + x + ", " + y + ") is not adjacent to the player.");
      return false;
    }
    Block<?> block = blocks.get(position);
    if (block == null) {
      System.out.println("No block to mine at: (" + x + ", " + y + ")");
      return false;
    }
    if (tool == null) {
      System.out.println("Tool required to mine block at: (" + x + ", " + y + ")");
      return false;
    }
    block.mine(tool);
    if (block.getHardness() <= 0) {
      blocks.remove(position);
      System.out.println("Block removed from position: (" + x + ", " + y + ")");
    }
    return true;
  }

  public boolean killEntity(int x, int y) {
    Position position = new Position(x, y);
    if (isValidPosition(x, y)) {
      System.out.println("Invalid position: (" + x + ", " + y + ")");
      return false;
    }
    if (isAdjacentToPlayer(x, y)) {
      System.out.println("Entity at (" + x + ", " + y + ") is not adjacent to the player.");
      return false;
    }
    Entity entity = entities.get(position);
    if (entity == null || entity instanceof Player) {
      System.out.println("No entity to kill at: (" + x + ", " + y + ")");
      return false;
    }
    player.hit(entity);
    if (entity.getCurrentHealth() <= 0) {
      entities.remove(position);
      System.out.println(entity.getName() + " removed from position: (" + x + ", " + y + ")");
    }
    return true;
  }

  public void displayMap() {
//    numery kolumn
    System.out.print("   "); // przesunięcie dla numerów wierszy
    for (int x = 0; x < width; x++) {
      System.out.print(x + " ");
    }
    System.out.println();

    for (int y = 0; y < height; y++) {
//      numer wiersza
      System.out.print(y + " ");

//       wyrównanie dla jednocyfrowych numerów wierszy
      if (y < 10) {
        System.out.print(" ");
      }

      for (int x = 0; x < width; x++) {
        Position position = new Position(x, y);
        if (entities.containsKey(position)) {
          Entity entity = entities.get(position);
          if (entity instanceof Player) {
            System.out.print("P ");
          } else if (entity instanceof Zombie) {
            System.out.print("Z ");
          } else if (entity instanceof Cow) {
            System.out.print("C ");
          }
        } else if (blocks.containsKey(position)) {
          Block<?> block = blocks.get(position);
          switch (block) {
            case Dirt dirt -> System.out.print("D ");
            case Stone stone -> System.out.print("S ");
            case Wood wood -> System.out.print("W ");
            case null, default -> {
              assert block != null;
              System.out.print(block.getName().charAt(0) + " ");
            }
          }
        } else {
          System.out.print(". ");
        }
      }
      System.out.println();
    }
  }

  public void displayAdjacentBlockStatus() {
    if (player == null) return;
    Position playerPosition = getPlayerPosition();
    if (playerPosition == null) return;
//    kierunki lewo prawo góra dół
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    for (int[] dir : directions) {
      int newX = playerPosition.getX() + dir[0];
      int newY = playerPosition.getY() + dir[1];
      Position newPosition = new Position(newX, newY);
      if (blocks.containsKey(newPosition)) {
        Block<?> block = blocks.get(newPosition);
        System.out.println("Block: " + block.getName() + ", Hardness: " + block.getHardness() + ", Tool: " + block.getToolClass().getSimpleName());
      } else if (entities.containsKey(newPosition) && !(entities.get(newPosition) instanceof Player)) {
        Entity entity = entities.get(newPosition);
        System.out.println("Entity: " + entity.getName() + ", Health: " + entity.getCurrentHealth());
      }
    }
  }

  public boolean movePlayer(int dx, int dy) {
    if (player == null) return false;
    Position currentPosition = getPlayerPosition();
    if (currentPosition == null) return false;

    int newX = currentPosition.getX() + dx;
    int newY = currentPosition.getY() + dy;
    if (isValidPosition(newX, newY)) {
      System.out.println("Invalid move position: (" + newX + ", " + newY + ")");
      return false;
    }

    Position newPosition = new Position(newX, newY);
    if (blocks.containsKey(newPosition) || entities.containsKey(newPosition)) {
      System.out.println("Position occupied: (" + newX + ", " + newY + ")");
      return false;
    }

    entities.remove(currentPosition);
    entities.put(newPosition, player);
    System.out.println("Moved player to (" + newX + ", " + newY + ")");
    return true;
  }

  private boolean isAdjacentToPlayer(int x, int y) {
    Position playerPosition = getPlayerPosition();
    if (playerPosition == null) return true;

    int dx = Math.abs(playerPosition.getX() - x);
    int dy = Math.abs(playerPosition.getY() - y);

    return (dx != 1 || dy != 0) && (dx != 0 || dy != 1);
  }

  public Position getPlayerPosition() {
    for (Map.Entry<Position, Entity> entry : entities.entrySet()) {
      if (entry.getValue() instanceof Player) {
        return entry.getKey();
      }
    }
    return null;
  }

  public boolean isValidPosition(int x, int y) {
    return x < 0 || x >= width || y < 0 || y >= height;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Map<Position, Block<?>> getBlocks() {
    return blocks;
  }

  public Map<Position, Entity> getEntities() {
    return entities;
  }

  public static class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Position position = (Position) o;
      return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
      return 31 * x + y;
    }
  }
}
