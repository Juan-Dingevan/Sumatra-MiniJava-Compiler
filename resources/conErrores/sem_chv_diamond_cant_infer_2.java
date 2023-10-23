///[Error:new|10]
class G<E> {
    public boolean b() {
        return true;
    }
}

class A {
    public void m() {
        if((new G<>()).b()) {
            //do something
        }
    }
}

class Main {
    public static void main() {}
}
