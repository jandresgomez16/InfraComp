public class Cliente extends Thread {
    public static int identificador = 1;
    private int id = identificador++;
    public static Buffer buffer;
    private Mensaje mensaje;
    private int count;

    public Cliente(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            synchronized (mensaje = new Mensaje()) {
                while (!buffer.enviarPeticion(mensaje)) yield();

                System.out.println("Cliente (" + id + ") sube el mensaje (" + mensaje.getId() + ") al servidor");

                try {
                    System.out.println("Cliente (" + id + ") durmiendo");
                    mensaje.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mensaje.getContenido() - mensaje.getId() == 1)
                    System.out.println("Cliente (" + id + ") con mensaje (" + mensaje.getId() + ") fue procesado correctamente");
                else
                    System.err.println("Cliente (" + id + ") con mensaje (" + mensaje.getId() + ") no fue procesado correctamente");
            }
        }
        buffer.apagarCliente();
        System.out.println("Cliente (" + id + ") finalizo sus solicitudes");
    }
}