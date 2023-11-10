///0: meow&yarn&1: woof&bone toy!&2: moo&c&exitosamente
interface Animal {
    String sound();
    int id();
    void play();
}

class Cat implements Animal {
    public int id() {
        return 0;
    }
    private void playWithYarn() {
        System.printSln("yarn");
    }

    public String sound() {
        return "meow";
    }

    public void play() {
        this.playWithYarn();
    }
}


class Dog implements Animal {
    public void play() {
        System.printSln("bone toy!");
    }
    public void nap() {}

    public int id() {
        return 1;
    }

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

    public void play() {
        System.printCln('c');
    }

    public int id() {
        return 2;
    }
}

class Init {
    private static Animal animal;

    public static void main() {
        var i = 0;
        var id = 0;
        while(i < 3) {

            if(i == 0) {
                animal = new Cat();
            } else if(i == 1) {
                animal = new Dog();
            } else {
                animal = new Cow();
            }

            id = animal.id();
            System.printI(id);
            System.printS(": ");
            System.printSln(animal.sound());
            animal.play();

            i = i + 1;
        }
    }
}