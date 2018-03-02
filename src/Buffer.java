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

        //notifica a un servidor thread para que procese el mensaje
        this.notify();

        return true;
    }

    //Saca una petición de la cola. Si la cola esta vacia, retorna null
    public synchronized Mensaje sacarPeticion() {
        if (cola.size() == 0) return null;
        Mensaje mensaje = cola.get(0);
        cola.remove(0);
        return mensaje;
    }

    //Cantidad de mensajes en la cola
    public synchronized int cantidadMensajes() {
        return cola.size();
    }

    //Cantidad de clientes que tienen al menus una solicitud no procesada o sin enviar
    public synchronized int cantidadClientes() {
        synchronized (clientesActivos) {
            return clientesActivos;
        }
    }

    //reduce la cantidad de clientes activos
    public void apagarCliente() {
        synchronized (clientesActivos) {
            clientesActivos -= 1;

            //Notifica a los thread servidor que puedan estar dormidos que ya no hay mas clientes que antender, para que despierten y terminen su ejecucion
            if(clientesActivos == 0){
                synchronized (this) {
                    this.notifyAll();
                }
            }
        }
    }
}