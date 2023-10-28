///[SinErrores]
interface List<E> {
    E get(int i);
}

class Node<E> {
    private Node<E> next;
    private E e;

    public Node(E e) {
        this.e = e;
    }

    public E getE() {
        return e;
    }

    public void setNext(Node<E> n) {
        next = n;
    }

    public Node<E> getNext() {
        return next;
    }
}

class LinkedList<E> implements List<E> {
    private Node<E> head;
    private Node<E> tail;

    public LinkedList() {
        head = new Node<>(null);
        tail = new Node<>(null);

        head.setNext(null);
        tail.setNext(null);

        tail = head;
    }

    public void addElement(E e) {
        var newNode = new Node<E>(e);
        tail.setNext(newNode);
        tail = newNode;
    }

    public E get(int n) {
        var i = 0;
        var currentNode = head;

        while(i < n) {
            currentNode = currentNode.getNext();
            i = i + 1;
        }

        return currentNode.getE();
    }
}

class StringList extends LinkedList<String> {
}

class Main {
    private static String myFavoriteString;

    public static void main() {
        var sl = new StringList();
        sl.addElement("Compiladores");
        sl.addElement("e");
        sl.addElement("Interpretes");
        sl.addElement("Logro:");
        sl.addElement("Genericidad Controlada");
        sl.addElement("2023");
        sl.addElement("DCIC");
        sl.addElement("UNS");
        myFavoriteString = sl.get(4);
    }
}