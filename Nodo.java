public class Nodo {

    private String dato;
    private Nodo siguiente;

    public Nodo(String dato) {
        this.dato = dato;
    }

    public String getDato() {
        return dato;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
