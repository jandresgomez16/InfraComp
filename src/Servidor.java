import java.io.BufferedReader;
import java.io.FileReader;

public class Servidor extends Thread {
    //Variable para asignar identificadores unicos a cada servidor
    private static int identificador = 1;

    //Id del Servidor, lo asigna del identificador global y lo aumenta
    private int id = identificador++;

    //Buffer del que los servidores van a sacar los mensajes
    public static Buffer buffer;

    //Asigna nombre al thread servidor, con su id correspondiente
    public Servidor() {
        super("Servidor " + (identificador - 1));
    }

    @Override
    public void run() {
        synchronized (buffer) {
            //Revisa que el buffer aun tenga clientes activos
            while (buffer.cantidadClientes() > 0) {
                //Revisa que existan mensajes en el buffer
                while (buffer.cantidadMensajes() > 0) {
                    Mensaje mensaje;
                    //Sincroniza sobre el mensaje que saca del buffer
                    synchronized (mensaje = buffer.sacarPeticion()) {
                        if (mensaje == null) break;

                        //Procesa el mensaje
                        mensaje.procesarContenido();

                        System.out.println("Servidor (" + id + ") proceso mensaje (" + mensaje.getId() + ")");

                        //Despierta al thread cliente
                        mensaje.notify();
                    }
                }
                try {
                    //Revisa que exista al menos un cliente activo, ya que de lo contrario podria no despertar
                    if (buffer.cantidadClientes() > 0) {
                        System.out.println("Servidor (" + id + ") durmiendo");
                        buffer.wait();
                        System.out.println("Servidor (" + id + ") despierto");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Servidor (" + id + ") finalizo");
    }

    public static void main(String[] args) {
        int nClientes = 0;
        int nServidores = 0;
        int bufferSize = 0;
        String[] solicitudesCliente = null;

        //Lee los parametros del archivo que se encuentra en la carpeta docs
        try (BufferedReader br = new BufferedReader(new FileReader("docs/parametros.txt"))) {
            nClientes = Integer.parseInt(br.readLine().split(" ")[1]);
            nServidores = Integer.parseInt(br.readLine().split(" ")[1]);
            bufferSize = Integer.parseInt(br.readLine().split(" ")[1]);
            solicitudesCliente = (br.readLine().split(" ")[1]).split(",");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Asigna el mismo Buffer para servidores y clientes
        Servidor.buffer = new Buffer(nClientes, bufferSize);
        Cliente.buffer = Servidor.buffer;

        System.out.println("Hay " + nClientes + " clientes");
        System.out.println("Hay " + nServidores + " servidores");
        System.out.println("Buffer size " + bufferSize);

        //Inicia los clientes
        for (int i = 0; i < nClientes; i++) {
            Cliente c = new Cliente(Integer.parseInt(solicitudesCliente[i]));
            c.start();
        }

        //Inicia los servidores
        for (int i = 0; i < nServidores; i++) {
            Servidor c = new Servidor();
            c.start();
        }
    }
}

