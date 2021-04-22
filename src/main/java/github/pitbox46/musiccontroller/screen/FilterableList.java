package github.pitbox46.musiccontroller.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public abstract class FilterableList<K extends Comparable<K>, E extends ExtendedList.AbstractListEntry<E>> extends ExtendedList<E> {
    protected final Map<K,E> ENTRY_MAP = new HashMap<>();
    private Map<K,E> visibleMap = new HashMap<>();

    public FilterableList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
    }

    public abstract void addWithKey(K key);
    public abstract void removeWithKey(K key);

    protected void updateList(Map<K,E> entryMap) {
        this.visibleMap = entryMap;
        this.replaceEntries(sortList(entryMap));
    }

    public void filterList(Predicate<Map.Entry<K,E>> predicate) {
        Map<K,E> outMap = new HashMap<>();

        for(Map.Entry<K,E> entry: ENTRY_MAP.entrySet()) {
            if(predicate.test(entry)) {
                outMap.put(entry.getKey(), entry.getValue());
            }
        }

        this.updateList(outMap);
    }

    public abstract void searchList(String string);

    public abstract Collection<E> sortList(Map<K,E> entryMap);
}
