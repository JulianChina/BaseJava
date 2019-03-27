package ThinkingInJava.zUtils;

import java.lang.reflect.Method;
import java.util.*;

public class ContainerMethodDifferences {
    static Set<String> methodSet(Class<?> type) {
        Set<String> result = new TreeSet<>();
        for (Method m : type.getMethods()) {
            result.add(m.getName());
        }
        return result;
    }
    static void interfaces(Class<?> type) {
        Print.printnb("Interfaces in " + type.getSimpleName() + ": ");
        List<String> result = new ArrayList<>();
        for (Class<?> c : type.getInterfaces()) {
            result.add(c.getSimpleName());
        }
        Print.print(result);
    }

    static Set<String> object = methodSet(Object.class);
    static { object.add("clone"); }
    static void difference(Class<?> subset, Class<?> superset) {
        Print.printnb(subset.getSimpleName() + " extends " + superset.getSimpleName() + ", adds: ");
        Set<String> comp = Sets.difference(methodSet(subset), methodSet(superset));
        comp.removeAll(object);
        Print.print(comp);
        interfaces(subset);
    }

    public static void main(String[] args) {
        Print.print("Collection: " + methodSet(Collection.class));
        interfaces(Collection.class);
        difference(Set.class, Collection.class);
        difference(HashSet.class, Set.class);
        difference(LinkedHashSet.class, HashSet.class);
        difference(SortedSet.class, Set.class);
        difference(TreeSet.class, Set.class);
        difference(List.class, Collection.class);
        difference(ArrayList.class, List.class);
        difference(LinkedList.class, List.class);
        difference(Queue.class, Collection.class);
        difference(PriorityQueue.class, Queue.class);
        Print.print("Map: " + methodSet(Map.class));
        difference(HashMap.class, Map.class);
        difference(LinkedHashMap.class, HashMap.class);
        difference(SortedMap.class, Map.class);
        difference(TreeMap.class, Map.class);
    }
}
