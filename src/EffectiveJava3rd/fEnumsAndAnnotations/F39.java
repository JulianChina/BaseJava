package EffectiveJava3rd.fEnumsAndAnnotations;

import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//39 注解优于命名模式
public class F39 {
    //过去，通常使用命名模式(naming patterns)来指示某些程序元素需要通过工具或框架进行特殊处理。
    //命名模式的第二个缺点是无法确保它们仅用于适当的程序元素。
    //命名模式的第三个缺点是它们没有提供将参数值与程序元素相关联的好的方法。

    // Marker annotation type declaration

    /**
     * Indicates that the annotated method is a test method. * Use only on parameterless static methods.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Test {

    }

    // Program containing marker annotations
    public static class Sample {
        @Test
        public static void m1() {
        }  // Test should pass

        public static void m2() {
        }

        @Test
        public static void m3() {     // Test should fail
            throw new RuntimeException("Boom");
        }

        public static void m4() {
        }

        @Test
        public void m5() {
        } // INVALID USE: nonstatic method

        public static void m6() {
        }

        @Test
        public static void m7() {    // Test should fail
            throw new RuntimeException("Crash");
        }

        public static void m8() {
        }
    }

    //Test注解对Sample类的语义没有直接影响。他们只提供信息供相关程序使用。更一般地说，注解不会改变注解代码的语义，但可以通过诸如这个简单的测试运行器等工具对其进行特殊处理。
    public static class RunTests {
        public static void main(String[] args) throws Exception {
            int tests = 0;
            int passed = 0;
            Class<?> testClass = Class.forName(args[0]);
            for (Method m : testClass.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Test.class)) {
                    tests++;
                    try {
                        m.invoke(null);
                        passed++;
                    } catch (InvocationTargetException wrappedExc) {
                        Throwable exc = wrappedExc.getCause();
                        System.out.println(m + " failed: " + exc);
                    } catch (Exception exc) {
                        System.out.println("Invalid @Test: " + m);
                    }
                }
            }
            System.out.printf("Passed: %d, Failed: %d%n", passed, tests - passed);
        }
    }

    // Annotation type with a parameter

    /**
     * Indicates that the annotated method is a test method that * must throw the designated exception to succeed.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ExceptionTest {
        Class<? extends Throwable> value();
    }

    // Program containing annotations with a parameter
    public static class Sample2 {
        @ExceptionTest(ArithmeticException.class)
        public static void m1() { // Test should pass
            int i = 0;
            i = i / i;
        }

        @ExceptionTest(ArithmeticException.class)
        public static void m2() { // Should fail (wrong exception)
            int[] a = new int[0];
            int i = a[1];
        }

        @ExceptionTest(ArithmeticException.class)
        public static void m3() {
        }  // Should fail (no exception)
    }

    public static class RunTests2 {
        public static void main(String[] args) throws Exception {
            int tests = 0;
            int passed = 0;
            Class<?> testClass = Class.forName(args[0]);
            for (Method m : testClass.getDeclaredMethods()) {
                if (m.isAnnotationPresent(ExceptionTest.class)) {
                    tests++;
                    try {
                        m.invoke(null);
                        System.out.printf("Test %s failed: no exception%n", m);
                    } catch (InvocationTargetException wrappedEx) {
                        Throwable exc = wrappedEx.getCause();
                        Class<? extends Throwable> excType = m.getAnnotation(ExceptionTest.class).value();
                        if (excType.isInstance(exc)) {
                            passed++;
                        } else {
                            System.out.printf("Test %s failed: expected %s, got %s%n", m, excType.getName(), exc);
                        }
                    } catch (Exception exc) {
                        System.out.println("Invalid @Test: " + m);
                    }
                }
            }
            System.out.printf("Passed: %d, Failed: %d%n", passed, tests - passed);
        }
    }

    // Annotation type with an array parameter @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD)
    public @interface ExceptionArrayTest {
        Class<? extends Exception>[] value();
    }
    //注解中数组参数的语法很灵活。它针对单元素数组进行了优化。所有以前的ExceptionTest注解仍然适用于ExceptionArrayTest的新数组参数版本，并且会生成单元素数组。

    public static class Sample3 {
        // Code containing an annotation with an array parameter
        @ExceptionArrayTest({IndexOutOfBoundsException.class, NullPointerException.class})
        public static void doublyBad() {
            List<String> list = new ArrayList<>();
            // The spec permits this method to throw either
            // IndexOutOfBoundsException or NullPointerException
            list.addAll(5, null);
        }
    }

    public static class RunTests3 {
        public static void main(String[] args) throws Exception {
            int tests = 0;
            int passed = 0;
            Class<?> testClass = Class.forName(args[0]);
            for (Method m : testClass.getDeclaredMethods()) {
                if (m.isAnnotationPresent(ExceptionArrayTest.class)) {
                    tests++;
                    try {
                        m.invoke(null);
                        System.out.printf("Test %s failed: no exception%n", m);
                    } catch (Throwable wrappedExc) {
                        Throwable exc = wrappedExc.getCause();
                        int oldPassed = passed;
                        Class<? extends Exception>[] excTypes = m.getAnnotation(ExceptionArrayTest.class).value();
                        for (Class<? extends Exception> excType : excTypes) {
                            if (excType.isInstance(exc)) {
                                passed++;
                                break;
                            }
                        }
                        if (passed == oldPassed)
                            System.out.printf("Test %s failed: %s %n", m, exc);
                    }
                }
            }
            System.out.printf("Passed: %d, Failed: %d%n", passed, tests - passed);
        }
    }

    //从Java8开始，还有另一种方法来执行多值注解。可以使用@Repeatable元注解来标示注解的声明，而不用使用数组参数声明注解类型，以指示注解可以重复应用于单个元素。
    // Repeatable annotation type
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Repeatable(ExceptionRepeatableTestContainer.class)
    public @interface ExceptionRepeatableTest {
        Class<? extends Exception> value();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ExceptionRepeatableTestContainer {
        ExceptionRepeatableTest[] value();
    }

    public static class Sample4 {
        // Code containing a repeated annotation
        @ExceptionRepeatableTest(IndexOutOfBoundsException.class)
        @ExceptionRepeatableTest(NullPointerException.class)
        public static void doublyBad() {
            List<String> list = new ArrayList<>();
            // The spec permits this method to throw either
            // IndexOutOfBoundsException or NullPointerException
            list.addAll(5, null);
        }
    }

    //重复注解会生成包含注解类型的合成注解。
    //要使用isAnnotationPresent检测重复和非重复的注解，需要检查注解类型及其包含的注解类型。
    public static class RunTests4 {
        public static void main(String[] args) throws Exception {
            int tests = 0;
            int passed = 0;
            Class<?> testClass = Class.forName(args[0]);
            for (Method m : testClass.getDeclaredMethods()) {
                // Processing repeatable annotations
                if (m.isAnnotationPresent(ExceptionRepeatableTest.class)
                        || m.isAnnotationPresent(ExceptionRepeatableTestContainer.class)) {
                    tests++;
                    try {
                        m.invoke(null);
                        System.out.printf("Test %s failed: no exception%n", m);
                    } catch (Throwable wrappedExc) {
                        Throwable exc = wrappedExc.getCause();
                        int oldPassed = passed;
                        ExceptionRepeatableTest[] excTests = m.getAnnotationsByType(ExceptionRepeatableTest.class);
                        for (ExceptionRepeatableTest excTest : excTests) {
                            if (excTest.value().isInstance(exc)) {
                                passed++;
                                break;
                            }
                        }
                        if (passed == oldPassed)
                            System.out.printf("Test %s failed: %s %n", m, exc);
                    }
                }
            }
            System.out.printf("Passed: %d, Failed: %d%n", passed, tests - passed);
        }
    }

    //当可以使用注解代替时，没有理由使用命名模式。
    //除了特定的开发者(toolsmith)之外，大多数程序员都不需要定义注解类型。但所有程序员都应该使用Java提供的预定义注解类型(条目40，27)。
}
