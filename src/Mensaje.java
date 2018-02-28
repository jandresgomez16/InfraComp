public class Mensaje {
    private int contenido;
    public static int identificador = 1;
    private int id = identificador++;

    public Mensaje() {
        contenido = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getContenido() {
        return contenido;
    }

    public void setContenido(int contenido) {
        this.contenido = contenido;
    }

    public void procesarContenido() {
        contenido++;
    }
}
