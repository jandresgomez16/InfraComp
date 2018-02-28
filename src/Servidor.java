import java.io.BufferedReader;
import java.io.FileReader;

public class Servidor extends Thread {
    private static int identificador = 1;
    private int id = identificador++;
    public static Buffer buffer;

    @Override
    public void run() {
        synchronized (buffer) {
            while (buffer.cantidadClientes() > 0) {
                while (buffer.cantidadMensajes() > 0) {
                    Mensaje mensaje;
                    synchronized (mensaje  = buffer.sacarPeticion()) {
                        if (mensaje == null) break;
                        mensaje.procesarContenido();
                        System.out.println("Servidor (" + id + ") proceso mensaje (" + mensaje.getId() + ")");
                        mensaje.notify();
                    }
                }
                try {
                    System.out.println("Servidor (" + id + ") durmiendo");
                    buffer.wait();
                    System.out.println("Servidor (" + id + ") despierto");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } System.out.println("Servidor (" + id + ") finalizo");
    }

    public static void main(String[] args) {
        int nClientes = 0;
        int nServidores = 0;
        int bufferSize = 0;
        int solicitudesCliente = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("docs/parametros.txt"))) {
            nClientes = Integer.parseInt(br.readLine().split(" ")[1]);
            nServidores = Integer.parseInt(br.readLine().split(" ")[1]);
            bufferSize = Integer.parseInt(br.readLine().split(" ")[1]);
            solicitudesCliente = Integer.parseInt(br.readLine().split(" ")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Servidor.buffer = new Buffer(nClientes, bufferSize);
        Cliente.buffer = Servidor.buffer;

        System.out.println("Hay " + nClientes + " clientes");
        System.out.println("Hay " + nServidores + " servidores");
        System.out.println("Buffer size " + bufferSize);
        System.out.println("Hay " + solicitudesCliente + " solicitudes por cliente\n");

        for (int i = 0; i < nClientes; i++) {
            Cliente c = new Cliente(solicitudesCliente);
            c.start();
        }

        for (int i = 0; i < nServidores; i++) {
            Servidor c = new Servidor();
            c.start();
        }
    }
}

