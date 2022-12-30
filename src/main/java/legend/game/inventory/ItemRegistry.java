package legend.game.inventory;

import legend.game.modding.registries.MutableRegistry;

public class ItemRegistry extends MutableRegistry<Item> {
  @Override
  protected int getNextId() {
    for(int i = 0; i < Integer.MAX_VALUE; i++) {
      // Reserved by script engine for giving gold amounts
      if(i == 0xfb || i == 0xfc || i == 0xfd || i == 0xfe || i == 0xff) {
        continue;
      }

      if(!this.idToName.containsKey(i)) {
        return i;
      }
    }

    throw new RuntimeException("Ran out of IDs");
  }
}
