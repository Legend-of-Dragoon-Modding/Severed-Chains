package legend.game.types;

import legend.game.inventory.InventoryEntry;

import java.util.ArrayList;

/** This class is mostly just to get around stupid generics restrictions */
public class MenuEntries<T extends InventoryEntry> extends ArrayList<MenuEntryStruct04<T>> {

}
