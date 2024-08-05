package mycraftworld;

public abstract class MyCraftObject {
  protected String name;
  protected boolean isFlammable;

  public MyCraftObject(String name) {
    this.name = name;
    this.isFlammable = false;
  }

  public abstract String getName();
}
