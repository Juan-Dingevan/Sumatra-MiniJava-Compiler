///meow&woof&moo&exitosamente
class Animal {
    public String sound() {
        return " ";
    }
}

class Cat extends Animal {
    public String sound() {
        return "meow";
    }
}


class Dog extends Animal {
    public String sound() {
        return "woof";
    }
}


class Cow extends Animal {
    public String sound() {
        return "moo";
    }
}

class Init {
    public static void main() {
        var animal = new Animal();
        var i = 0;

        while(i < 3) {

            if(i == 0) {
                animal = new Cat();
            } else if(i == 1) {
                animal = new Dog();
            } else {
                animal = new Cow();
            }

            System.printSln(animal.sound());
            i = i + 1;
        }
    }
}