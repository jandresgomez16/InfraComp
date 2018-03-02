public class Mensaje {
    //Contenido del mensaje
    private int contenido;

    //Variable para asignar identificadores unicos a cada mensaje
    public static int identificador = 1;

    //Id del mensaje, lo asigna del identificador global y lo aumenta
    private int id = identificador++;

    //Asigna el contenido como el id del mensaje
    public Mensaje() {
        contenido = id;
    }

    //Retorna el id del mensaje
    public int getId() {
        return id;
    }

    //Permite editar el valor del id del mensaje
    public void setId(int id) {
        this.id = id;
    }

    //Retorna el contenido del mensaje
    public int getContenido() {
        return contenido;
    }

    //Asigna el contenido del mensaje
    public void setContenido(int contenido) {
        this.contenido = contenido;
    }

    //Aumenta el valor del contenido del mensaje
    public void procesarContenido() {
        contenido++;
    }
}
