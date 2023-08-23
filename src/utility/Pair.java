package utility;

public class Pair<A, B> {
    protected A firstElement;
    protected B secondElement;

    public Pair(A firstElement, B secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    public A getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(A firstElement) {
        this.firstElement = firstElement;
    }

    public B getSecondElement() {
        return secondElement;
    }

    public void setSecondElement(B secondElement) {
        this.secondElement = secondElement;
    }
}
