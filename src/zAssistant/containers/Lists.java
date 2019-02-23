package zAssistant.containers;

import net.mindview.util.Countries;
import zUtils.Print;

import java.util.*;

public class Lists {
    private static boolean b;
    private static String s;
    private static int i;
    private static Iterator<String> it;
    private static ListIterator<String> lit;
    public static void basicTest(List<String> a) {
        a.add(1, "x");
        a.add("x");
        a.addAll(Countries.names(25));
        a.addAll(3, Countries.names(25));
        b = a.contains("1");
        b = a.containsAll(Countries.names(25));
        s = a.get(1);
        i = a.indexOf("1");
        b = a.isEmpty();
        it = a.iterator();
        lit = a.listIterator();
        lit = a.listIterator(3);
        i = a.lastIndexOf("1");
        a.remove(1);
        a.remove("3");
        a.set(1, "y");
        a.retainAll(Countries.names(25));
        a.removeAll(Countries.names(25));
        i = a.size();
        a.clear();
    }
    public static void iterMotion(List<String> a) {
        ListIterator<String> lit = a.listIterator();
        b = lit.hasNext();
        b = lit.hasPrevious();
        s = lit.next();
        i = lit.nextIndex();
        s = lit.previous();
        i = lit.previousIndex();
    }
    public static void iterManipulation(List<String> a) {
        ListIterator<String> lit = a.listIterator();
        lit.add("47");
        lit.next();
        lit.remove();
        lit.next();
        lit.set("47");
    }
    public static void testVisual(List<String> a) {
        Print.print(a);
        List<String> b = Countries.names(25);
        Print.print("b = " + b);
        a.addAll(b);
        a.addAll(b);
        Print.print(a);
        ListIterator<String> x = a.listIterator(a.size() / 2);
        x.add("one");
        Print.print(a);
        Print.print(x.next());
        x.remove();
        Print.print(x.next());
        x.set("47");
        Print.print(a);
        x = a.listIterator(a.size());
        while (x.hasPrevious()) {
            Print.printnb(x.previous() + " ");
        }
        Print.print();
        Print.print("testVisual finished");
    }
    public static void testLinkedList() {
        LinkedList<String> ll = new LinkedList<>();
        ll.addAll(Countries.names(25));
        Print.print(ll);
        ll.addFirst("one");
        ll.addFirst("two");
        Print.print(ll);
        Print.print(ll.getFirst());
        Print.print(ll.removeFirst());
        Print.print(ll.removeFirst());
        Print.print(ll.removeLast());
        Print.print(ll);
    }

    public static void main(String[] args) {
        basicTest(new LinkedList<>(Countries.names(25)));
        basicTest(new ArrayList<>(Countries.names(25)));
        iterMotion(new LinkedList<>(Countries.names(25)));
        iterMotion(new ArrayList<>(Countries.names(25)));
        iterManipulation(new LinkedList<>(Countries.names(25)));
        iterManipulation(new ArrayList<>(Countries.names(25)));
        testVisual(new LinkedList<>(Countries.names(25)));
        testLinkedList();
    }
}
