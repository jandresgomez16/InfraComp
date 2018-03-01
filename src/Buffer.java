import java.util.ArrayList;

public class Buffer {
    //Cola donde se van a guardar los mensajes del buffer.
    private ArrayList<Mensaje> cola = new ArrayList<>();

    //Variable que representa el numero de clientes que tienen al menos una solicitud no procesada o sin enviar
    private Integer clientesActivos;

    //Tamaño maximo de la cola del buffer
    private int size;

    public Buffer(int clientesActivos, int size) {
        this.clientesActivos = clientesActivos;
        this.size = size;
    }

    //Monta una petición a la cola. Si la monta retorna true, de lo contrario retorna false
    public synchronized boolean enviarPeticion(Mensaje mensaje) {
        //Mira que el buffer tenga espacio para almacenar el mensaje
        if (cola.size() >= size) {
            if (cola.size() > size) System.err.println("La cola excede el tamanio del Buffer; " + cola.size());
            return false;
        }
        cola.add(mensaje);

        //notifica a los threads del servidor de que se han agregado elementos a la cola
        this.notifyAll();

        return true;
    }

    //Saca una petición de la cola. Si la cola esta vacia, retorna null
    public synchronized Mensaje sacarPeticion() {
        if (cola.size() == 0) return null;
        Mensaje mensaje = cola.get(0);
        cola.remove(0);
        return mensaje;
    }

    //cantidad de mensajes en la cola
    public synchronized int cantidadMensajes() {
        return cola.size();
    }

    //cantidad de clientes que tienen al menus una solicitud no procesada o sin enviar
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