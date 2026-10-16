package io.github.FernandoJelvez.tarea2_jelvez_rojas.model;

public class Compra {
    private final Long id;
    private final String producto;
    private final Double monto;
    private final String unidad;
    private final String fecha;

    public Compra(Long id, String producto, Double monto, String unidad, String fecha) {
        this.id = id;
        this.producto = producto;
        this.monto = monto;
        this.unidad = unidad;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public String getProducto() {
        return producto;
    }

    public Double getMonto() {
        return monto;
    }

    public String getUnidad() {
        return unidad;
    }

    public String getFecha() {
        return fecha;
    }
}
