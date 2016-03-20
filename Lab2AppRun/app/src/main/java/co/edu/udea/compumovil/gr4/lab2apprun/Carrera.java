package co.edu.udea.compumovil.gr4.lab2apprun;

/**
 * Created by MattNP on 19/03/2016.
 */
public class Carrera {

    private String id;
    private String nombre;
    private String distancia;
    private String lugar;
    private String fecha;
    //private String foto;
    private String telefono;
    private String correo;

    public Carrera(String id, String nombre, String distancia, String lugar, String fecha, String telefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.distancia = distancia;
        this.lugar = lugar;
        this.fecha = fecha;
        this.telefono = telefono;
        this.correo = correo;
    }

    public Carrera() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
