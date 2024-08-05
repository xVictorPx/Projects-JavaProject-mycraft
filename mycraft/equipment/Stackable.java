package equipment;

public interface Stackable {
  int count();
  void addToStack(int amount);
  void removeFromStack(int amount);

  default boolean canStackWith(Stackable other) {
    return this.getClass().equals(other.getClass());
  }
}
