public class Nodo {
    private String codigo;
    private double capacidadMaxima;
    private boolean gpu;

    public Nodo(String codigo, double capacidadMaxima, boolean gpu) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo del nodo no puede estar vacio.");
        }
        if (capacidadMaxima <= 0 || Double.isNaN(capacidadMaxima)
                || Double.isInfinite(capacidadMaxima)) {
            throw new IllegalArgumentException("La capacidad del nodo debe ser mayor que cero.");
        }

        this.codigo = codigo.trim();
        this.capacidadMaxima = capacidadMaxima;
        this.gpu = gpu;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public boolean tieneGPU() {
        return gpu;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo
                + "Capacidad: " + String.format("%.2f", capacidadMaxima) + " horas"
                + "GPU: " + (gpu ? "Si" : "No");
    }
}
