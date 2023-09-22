///[SinErrores]
interface Iterable<E> {

}

class List<T> implements Iterable<T> {
    private Iterable<T> it;
    public Iterable<T> getIterable() {}
    public void setIterable(Iterable<T> it) {}
}