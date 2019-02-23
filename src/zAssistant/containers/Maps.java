package zAssistant.containers;

import net.mindview.util.CountingMapData;
import zUtils.Print;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Maps {
    public static void printKeys(Map<Integer, String> map) {
        Print.printnb("Size = " + map.size() + ", ");
        Print.printnb("Keys: ");
        Print.print(map.keySet());
    }
    public static void test(Map<Integer, String> map) {
        Print.print("----- " + map.getClass().getSimpleName() + " -----");
        map.putAll(new CountingMapData(25));
        map.putAll(new CountingMapData(25));
        printKeys(map);
        Print.printnb("Values: ");
        Print.print(map.values());
        Print.print(map);
        Print.print("map.containsKey(11): " + map.containsKey(11));
        Print.print("map.get(11): " + map.get(11));
        Print.print("map.containsValue(\"F0\"): " + map.containsValue("F0"));
        Integer key = map.keySet().iterator().next();
        Print.print("First key in map: " + key);
        map.remove(key);
        printKeys(map);
        map.clear();
        Print.print("map.isEmpty():" + map.isEmpty());
        map.putAll(new CountingMapData(25));
        map.keySet().removeAll(map.keySet());
        Print.print("map.isEmpty():" + map.isEmpty());
    }

    public static void main(String[] args) {
        test(new HashMap<>());
        test(new TreeMap<>());
        test(new LinkedHashMap<>());
        test(new IdentityHashMap<>());
        test(new ConcurrentHashMap<>());
        test(new WeakHashMap<>());
    }
}
