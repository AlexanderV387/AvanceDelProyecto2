public class Pedido {
    private String nombre;
    private String cedula;
    private String distancia;

    public Pedido(String nombre, String cedula, String distancia) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.distancia = distancia;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getDistancia() {
        return distancia;
    }

    @Override
    public String toString() {
        return nombre + " | " + cedula + " | " + distancia;
    }
}