package EffectiveJava3rd.dClassesAndInterfaces;

//23 类层次结构优于标签类
public class D23 {
    // Tagged class - vastly inferior to a class hierarchy!
    static class Figure {
        enum Shape { RECTANGLE, CIRCLE };
        // Tag field - the shape of this figure
        final Shape shape;
        // These fields are used only if shape is RECTANGLE
        double length;
        double width;
        // This field is used only if shape is CIRCLE
        double radius;
        // Constructor for circle
        Figure(double radius) {
            shape = Shape.CIRCLE;
            this.radius = radius;
        }
        // Constructor for rectangle
        Figure(double length, double width) {
            shape = Shape.RECTANGLE;
            this.length = length;
            this.width = width;
        }
        double area() {
            switch(shape) {
                case RECTANGLE:
                    return length * width;
                case CIRCLE:
                    return Math.PI * (radius * radius);
                default:
                    throw new AssertionError(shape);
            } }
    }
    //标签类是冗长的，容易出错的，而且效率低下。

    //幸运的是，像Java这样的面向对象的语言为定义一个能够表示多种风格对象的单一数据类型提供了更好的选择：子类型化(subtyping)。标签类仅仅是一个类层次的简单的模仿。
    // Class hierarchy replacement for a tagged class
    abstract class Figure2 {
        abstract double area();
    }
    class Circle extends Figure2 {
        final double radius;
        Circle(double radius) { this.radius = radius; }
        @Override double area() { return Math.PI * (radius * radius); }
    }
    class Rectangle extends Figure2 {
        final double length;
        final double width;
        Rectangle(double length, double width) {
            this.length = length;
            this.width = width;
        }
        @Override double area() { return length * width; }
    }
    //类层次的另一个优点是可以使它们反映类型之间的自然层次关系，从而提高了灵活性，并提高了编译时类型检查的效率。

    //标签类很少有适用的情况。
    //如果你想写一个带有明显标签属性的类，请考虑标签属性是否可以被删除，而类是否被类层次替换。
    //当遇到一个带有标签属性的现有类时，可以考虑将其重构为一个类层次中。
}
