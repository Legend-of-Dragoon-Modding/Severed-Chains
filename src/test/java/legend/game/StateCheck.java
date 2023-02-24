package legend.game;

import legend.game.inventory.screens.ItemListScreen;
import legend.game.inventory.screens.MenuScreen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateCheck {

  private static final Logger LOGGER = LogManager.getFormatterLogger(StateCheck.class);
  // java string formatting in java docs
  // string.format() to see use cases
  // %d decimal or int
  // %s string
  // %4d is right aligned with 4 wide can use in table

  //TODO change logic to junit asserts need to research more on this.


  public static boolean itemListScreenIsState0() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        return itemList.getState() == state .0;
        System.out.println("Passed: itemListScreenIsState0()");
        LOGGER.info("Passed: itemListScreenIsState0()");
      } else {

        System.out.println("Failed: itemListScreenIsState0()");
        LOGGER.info("Failed: itemListScreenIsState0()");

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenIsState0() failed, see stack trace");
    }
  }

  public static void itemListScreenNotState0() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        itemList.getState() != state .0;
        System.out.println("Passed: itemListScreenNotState0()");
        LOGGER.info("Passed: itemListScreenNotState0()");
      } else {
        System.out.println("Failed: itemListScreenNotState0()");
        LOGGER.info("Failed: itemListScreenNotState0()");

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenNotState0() failed, see stack trace");

    }
  }
  public static void itemListScreenIsState1() {
    try {
      //TODO: wait on branch to be merged by Monoxide
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        itemList.getState() == state .1;
        System.out.println("Passed: itemListScreenIsState1()");
        LOGGER.info("Passed: itemListScreenIsState1()");
      } else {
        System.out.println("Failed: itemListScreenIsState1()");
        LOGGER.info("Failed: itemListScreenIsState1()");

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenIsState1() failed, see stack trace");

    }
  }

  public static void itemListScreenNotState1() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        itemList.getState() != state .1;
        System.out.println("Passed: itemListScreenNotState1()");
        LOGGER.info("Passed: itemListScreenNotState1()");
      } else {
        System.out.println("Failed: itemListScreenNotState1()");
        LOGGER.info("Failed: itemListScreenNotState1()");

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenNotState1() failed, see stack trace");

    }
  }

  public static void itemListScreenIsState2() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        itemList.getState() == state .2;
        System.out.println("Passed: itemListScreenIsState2()");
        LOGGER.info("Passed: itemListScreenIsState2()");
      } else {
        System.out.println("Failed: itemListScreenIsState2()");
        LOGGER.info("Failed: itemListScreenIsState2()");

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenIsState2() failed, see stack trace");
    }
  }

  public static void itemListScreenNotState2() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        itemList.getState() != state .2;
        System.out.println("Passed: itemListScreenNotState2()");
        LOGGER.info("Passed: itemListScreenNotState2()");
      } else {
        System.out.println("Failed: itemListScreenNotState2()");
        LOGGER.info("Failed: itemListScreenNotState2()");

      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenNotState2() failed, see stack trace");
    }

    public static void itemListScreenIsState3 () {
      try {
        final MenuScreen<?> screen = SItem.menuStack.getTop();

        if(screen instanceof ItemListScreen itemList) {
          itemList.getState() == state .3;
          System.out.println("Passed: itemListScreenIsState3()");
          LOGGER.info("Passed: itemListScreenIsState3()");
        } else {
          System.out.println("Failed: itemListScreenIsState3()");
          LOGGER.info("Failed: itemListScreenIsState3()");
        }
      } catch(Exception e) {
        throw new RuntimeException(e);
        System.out.println("itemListScreenIsState3() failed, see stack trace");
      }
    }
  }

  public static void itemListScreenIsState4() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        itemList.getState() == state .4;
        System.out.println("Passed: itemListScreenIsState4()");
        LOGGER.info("Passed: itemListScreenIsState4()");
      } else {
        System.out.println("Failed: itemListScreenIsState4()");
        LOGGER.info("Failed: itemListScreenIsState4()");
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenIsState4() failed, see stack trace");
    }
  }

  public static void itemListScreenNotState4() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        itemList.getState() != state .4;
        System.out.println("Passed: itemListScreenNotState4()");
        LOGGER.info("Passed: itemListScreenNotState4()");
      } else {
        System.out.println("Failed: itemListScreenNotState4()");
        LOGGER.info("Failed: itemListScreenNotState4()");
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenNotState4() failed, see stack trace");
    }
  }

  public static void itemListScreenIsState100() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        itemList.getState() == state .100;
        System.out.println("Passed: itemListScreenIsState100()");
        LOGGER.info("Passed: itemListScreenIsState100()");
      } else {
        System.out.println("Failed: itemListScreenIsState100()");
        LOGGER.info("Failed: itemListScreenIsState100()");
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenIsState100() failed, see stack trace");
    }
  }

  public static void itemListScreenNotState100() {
    try {
      final MenuScreen<?> screen = SItem.menuStack.getTop();

      if(screen instanceof ItemListScreen itemList) {
        itemList.getState() != state .100;
        System.out.println("Passed: itemListScreenNotState100()");
        LOGGER.info("Passed: itemListScreenNotState100()");
      } else {
        System.out.println("Failed: itemListScreenNotState100()");
        LOGGER.info("Failed: itemListScreenNotState100()");
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
      System.out.println("itemListScreenNotState100() failed, see stack trace");
    }
  }
}