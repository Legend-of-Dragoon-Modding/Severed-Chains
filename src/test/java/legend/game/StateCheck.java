package legend.game;

import legend.game.inventory.screens.ItemListScreen;
import legend.game.inventory.screens.MenuScreen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateCheck {


  //TODO change logic to junit asserts need to research more on this.


  public static boolean itemListScreenIsState0() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() == state .0;

      } else {
          return false;

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenIsState0() failed, see stack trace");
    }
  }

  public static boolean itemListScreenNotState0() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() != state .0;

      } else {
          return false;

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenNotState0() failed, see stack trace");

    }
  }
  public static boolean itemListScreenIsState1() {
    try {
      //TODO: wait on branch to be merged by Monoxide
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() == state .1;

      } else {
        return false;

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenIsState1() failed, see stack trace");

    }
  }

  public static boolean itemListScreenNotState1() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() != state .1;

      } else {
          return false;
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenNotState1() failed, see stack trace");

    }
  }

  public static boolean itemListScreenIsState2() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() == state .2;

      } else {
          return false;

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenIsState2() failed, see stack trace");
    }
  }

  public static boolean itemListScreenNotState2() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() != state .2;

      } else {
          return false;

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenNotState2() failed, see stack trace");
    }

    public static boolean itemListScreenIsState3 () {
      try {
        final MenuScreen<?> screen = SItem.menuStack.getTop();

        if(screen instanceof ItemListScreen itemList) {
          return itemList.getState() == state .3;

        } else {
            return false;
        }
      } catch(Exception e) {
        throw new RuntimeException(e);
        System.out.println("itemListScreenIsState3() failed, see stack trace");
      }
    }
  }

  public static boolean itemListScreenIsState4() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() == state .4;

      } else {
          return false;
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenIsState4() failed, see stack trace");
    }
  }

  public static boolean itemListScreenNotState4() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() != state .4;

      } else {
          return false;
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenNotState4() failed, see stack trace");
    }
  }

  public static boolean itemListScreenIsState100() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() == state .100;

      } else {
          return false;
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenIsState100() failed, see stack trace");
    }
  }

  public static boolean itemListScreenNotState100() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() != state .100;

      } else {
          return false;
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenNotState100() failed, see stack trace");
    }
  }
}w