package com.example.androrisk.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ThreatList {

    /**
     * An array of sample (dummy) items.
     */
    public static List<ThreatItem> ITEMS = new ArrayList<ThreatItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, ThreatItem> ITEM_MAP = new HashMap<String, ThreatItem>();

    static {
        // Add 3 sample items.
        addItem(new ThreatItem("1", "Threat 1"));
        addItem(new ThreatItem("2", "Threat 2"));
        addItem(new ThreatItem("3", "Threat 3"));
        addItem(new ThreatItem("4", "Threat 4"));
        addItem(new ThreatItem("5", "Threat 5"));
    }

    private static void addItem(ThreatItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ThreatItem {
        public String id;
        public String content;

        public ThreatItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
