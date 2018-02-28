import java.util.ArrayList;

public class Buffer {
    private ArrayList<Mensaje> cola = new ArrayList<>();
    private Integer clientesActivos;
    private int size;

    public Buffer(int clientesActivos, int size) {
        this.clientesActivos = clientesActivos;
        this.size = size;
    }

    public synchronized boolean enviarPeticion(Mensaje mensaje) {
        if (cola.size() >= size) {
            if (cola.size() > size) System.err.println("La cola excede el tamanio del Buffer; " + cola.size());
            return false;
        }
        cola.add(mensaje);
        this.notifyAll();
        return true;
    }

    public synchronized Mensaje sacarPeticion() {
        if (cola.size() == 0) return null;
        Mensaje mensaje = cola.get(0);
        cola.remove(0);
        return mensaje;
    }

    public synchronized int cantidadMensajes() {
        return cola.size();
    }

    public synchronized int cantidadClientes() {
        synchronized (clientesActivos) {
            return clientesActivos;
        }
    }

    public void apagarCliente() {
        synchronized (clientesActivos) {
            clientesActivos -= 1;
            synchronized (this) {
                this.notifyAll();
            }
        }
    }
}