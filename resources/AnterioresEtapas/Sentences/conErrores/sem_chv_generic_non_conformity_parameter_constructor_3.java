///[Error:User|13]
class Generic<E> {
    public Generic() {}
}

class User {
    public User(Generic<Object> g) {

    }

    public void m() {
        var x = new Generic<String>();
        var u = new User(x);
    }
}

class Main {
    public static void main() {}
}