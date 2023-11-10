///meow&woof&moo&exitosamente
interface Animal {
    String sound();
}

class Cat implements Animal {
    public void playWithYarn() {}

    public String sound() {
        return "meow";
    }
}


class Dog implements Animal {
    public void nap() {}

    public String sound() {
        return "woof";
    }

    public void runAfterMailman() {}
}


class Cow implements Animal {
    public void waveTail() {}
    public void eatGrass() {}
    public String sound() {
        return "moo";
    }


}

class Init {
    private static Animal animal;

    public static void main() {
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