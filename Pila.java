public class Pila {

    private Nodo cima;

    // Mete un dato arriba de la pila
    public void push(String dato) {
        Nodo nuevo = new Nodo(dato);
        nuevo.setSiguiente(cima);
        cima = nuevo;
    }

    // Saca y devuelve el dato de arriba (null si la pila está vacía)
    public String pop() {
        if (cima == null) {
            return null;
        }
        String dato = cima.getDato();
        cima = cima.getSiguiente();
        return dato;
    }

    // Devuelve el dato de arriba sin sacarlo
    public String peek() {
        if (cima == null) {
            return null;
        }
        return cima.getDato();
    }

    public boolean estaVacia() {
        return cima == null;
    }
}
