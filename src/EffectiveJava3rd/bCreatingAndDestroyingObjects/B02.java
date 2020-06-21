package EffectiveJava3rd.bCreatingAndDestroyingObjects;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

//02 当构造方法参数过多时使用builder模式
public class B02 {
    //可伸缩构造方法模式
    public static class NutritionFacts {
        private final int servingSize;
        private final int servings;
        private final int calories;
        private final int fat;
        private final int sodium;
        private final int carbohydrate;

        public NutritionFacts(int servingSize, int servings) {
            this(servingSize, servings, 0);
        }

        public NutritionFacts(int servingSize, int servings, int calories) {
            this(servingSize, servings, calories, 0);
        }

        public NutritionFacts(int servingSize, int servings, int calories, int fat) {
            this(servingSize, servings, calories, fat, 0);
        }

        public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium) {
            this(servingSize, servings, calories, fat, sodium, 0);
        }

        public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrate) {
            this.servingSize = servingSize;
            this.servings = servings;
            this.calories = calories;
            this.fat = fat;
            this.sodium = sodium;
            this.carbohydrate = carbohydrate;
        }
    }

    //JavaBean 模式
    public static class NutritionFacts2 {
        private int servingSize = -1;
        private int servings = -1;
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;

        public NutritionFacts2() {
        }

        public void setServingSize(int val) {
            servingSize = val;
        }

        public void setServings(int val) {
            servings = val;
        }

        public void setCalories(int val) {
            calories = val;
        }

        public void setFat(int val) {
            fat = val;
        }

        public void setSodium(int val) {
            sodium = val;
        }

        public void setCarbohydrate(int val) {
            carbohydrate = val;
        }
    }

    //Builder 模式
    public static class NutritionFacts3 {
        private final int servingSize;
        private final int servings;
        private final int calories;
        private final int fat;
        private final int sodium;
        private final int carbohydrate;

        public static class Builder {
            private final int servingSize;
            private final int servings;

            private int calories = 0;
            private int fat = 0;
            private int sodium = 0;
            private int carbohydrate = 0;

            public Builder(int servingSize, int servings) {
                this.servingSize = servingSize;
                this.servings = servings;
            }

            public Builder calories(int val) {
                calories = val;
                return this;
            }

            public Builder fat(int val) {
                fat = val;
                return this;
            }

            public Builder sodium(int val) {
                sodium = val;
                return this;
            }

            public Builder carbohydrate(int val) {
                carbohydrate = val;
                return this;
            }

            public NutritionFacts3 build() {
                return new NutritionFacts3(this);
            }
        }

        private NutritionFacts3(Builder builder) {
            servingSize = builder.servingSize;
            servings = builder.servings;
            calories = builder.calories;
            fat = builder.fat;
            sodium = builder.sodium;
            carbohydrate = builder.carbohydrate;
        }
    }

    //带类继承的Builder模式
    public abstract static class Pizza {
        public enum Topping {
            HAM, MUSHROOM, ONION, PEPPER, SAUSAGE
        }
        final Set<Topping> toppings;

        abstract static class Builder<T extends Builder<T>> {
            EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

            public T addTooping(Topping topping) {
                toppings.add(Objects.requireNonNull(topping));
                return self();
            }

            abstract Pizza build();

            protected abstract T self();
        }

        Pizza(Builder<?> builder) {
            toppings = builder.toppings.clone();
        }
    }

    public static class NyPizza extends Pizza {

        public enum Size {
            SMALL, MEDIUM, LARGE
        }

        private final Size size;

        public static class Builder extends Pizza.Builder<Builder> {
            private final Size size;

            public Builder(Size size) {
                this.size = Objects.requireNonNull(size);
            }

            @Override
            public NyPizza build() {
                return new NyPizza(this);
            }

            @Override
            protected Builder self() {
                return this;
            }
        }

        private NyPizza(Builder builder) {
            super(builder);
            size = builder.size;
        }
    }

    public static class Calzone extends Pizza {

        private final boolean sauceInside;

        public static class Builder extends Pizza.Builder<Builder> {
            private boolean sauceInside = false; // Default

            public Builder sauceInside() {
                sauceInside = true;
                return this;
            }

            @Override
            public Calzone build() {
                return new Calzone(this);
            }

            @Override
            protected Builder self() {
                return this;
            }
        }

        private Calzone(Builder builder) {
            super(builder);
            sauceInside = builder.sauceInside;
        }
    }

    //其一个子类的方法被声明为返回在超类中声明的返回类型的子类型，称为协变返回类型(covariant return typing)。
    //当设计类的构造方法或静态工厂的参数超过几个时，Builder模式是一个不错的选择，特别是如果许多参数是可选的或相同类型的。
}
