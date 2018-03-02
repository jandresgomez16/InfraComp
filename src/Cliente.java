public class Cliente extends Thread {
    //Variable para asignar identificadores unicos a cada cliente
    public static int identificador = 1;

    //Id del cliente, lo asigna del identificador global y lo aumenta
    private int id = identificador++;

    //Buffer al que los clientes van a cargar los mensajes
    public static Buffer buffer;

    //Mensaje actual del cliente
    private Mensaje mensaje;

    //Cantidad de mensajes que debe enviar el cliente antes apagarse
    private int count;

    //Asigna la cuenta de solicitudes a enviar
    public Cliente(int count) {
        super("Cliente " + (identificador - 1));
        this.count = count;
    }

    @Override
    public void run() {
        //Realiza la cantidad de solicitudes que se le asigna
        for (int i = 0; i < count; i++) {
            synchronized (mensaje = new Mensaje()) {
                //Intenta subir el mensaje al buffer. Si no puede, cede el procesador y vuelve a intentarlo mas tarde
                while (!buffer.enviarPeticion(mensaje)) yield();

                System.out.println("Cliente (" + id + ") sube el mensaje (" + mensaje.getId() + ") al buffer");

                //Se duerme hasta que el thread servidor le notifique que el mensaje ya fue procesado
                try {
                    System.out.println("Cliente (" + id + ") durmiendo");
                    mensaje.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Revisa que el mensaje fue procesado correctamente
                if (mensaje.getContenido() - mensaje.getId() == 1)
                    System.out.println("Cliente (" + id + ") con mensaje (" + mensaje.getId() + ") fue procesado correctamente");
                else
                    System.err.println("Cliente (" + id + ") con mensaje (" + mensaje.getId() + ") no fue procesado correctamente");
            }
        }

        //Se apaga, para que el buffer sepa que este cliente no volvera a enviar mensajes
        buffer.apagarCliente();
        System.out.println("Cliente (" + id + ") finalizo sus solicitudes");
    }
}