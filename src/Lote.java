import java.util.ArrayList;

public class Lote {
    public static final String PREPARACION = "En preparacion";
    public static final String EJECUCION = "En ejecucion";
    public static final String CERRADO = "Cerrado";

    private Nodo nodo;
    private ArrayList<Trabajo> trabajos;
    private ArrayList<Trabajo> resultadosRegistrados;
    private String estado;

    public Lote(Nodo nodo) {
        if (nodo == null) {
            throw new IllegalArgumentException("El lote necesita un nodo valido.");
        }

        this.nodo = nodo;
        this.trabajos = new ArrayList<Trabajo>();
        this.resultadosRegistrados = new ArrayList<Trabajo>();
        this.estado = PREPARACION;
    }

    public Nodo getNodo() {
        return nodo;
    }

    public ArrayList<Trabajo> getTrabajos() {
        return trabajos;
    }

    public String getEstado() {
        return estado;
    }

    public double calcularHoras() {
        double total = 0;
        for (Trabajo trabajo : trabajos) {
            total += trabajo.getHorasEstimadas();
        }
        return total;
    }
    public double calcularCapacidadDisponible() {
        return nodo.getCapacidadMaxima() - calcularHoras();
    }

    public boolean agregarTrabajo(Trabajo trabajo) {
        if (!estado.equals(PREPARACION)) {
            throw new IllegalStateException("Solo se pueden asignar trabajos a un lote en preparacion.");
        }
        if (trabajo == null) {
            throw new IllegalArgumentException("El trabajo indicado no existe.");
        }
        if (!trabajo.getEstado().equals(Trabajo.EN_COLA)) {
            throw new IllegalStateException("El trabajo debe estar en cola para poder asignarlo.");
        }
        if (!trabajo.puedeUsarNodo(nodo)) {
            throw new IllegalArgumentException("El trabajo no es compatible con el nodo seleccionado.");
        }
        if (calcularHoras() + trabajo.getHorasEstimadas() > nodo.getCapacidadMaxima()) {
            throw new IllegalArgumentException("La asignacion supera la capacidad maxima del nodo.");
        }

        trabajos.add(trabajo);
        trabajo.setEstado(Trabajo.ASIGNADO);
        return true;
    }

    public boolean retirarTrabajo(Trabajo trabajo) {
        if (!estado.equals(PREPARACION)) {
            throw new IllegalStateException("Solo se pueden retirar trabajos de un lote en preparacion.");
        }
        if (trabajo == null || !trabajos.contains(trabajo)) {
            throw new IllegalArgumentException("El trabajo no pertenece a este lote.");
        }
        trabajos.remove(trabajo);
        trabajo.setEstado(Trabajo.EN_COLA);
        return true;
    }
    public void iniciarLote() {
        if (!estado.equals(PREPARACION)) {
            throw new IllegalStateException("Solo se puede iniciar un lote en preparacion.");
        }
        if (trabajos.isEmpty()) {
            throw new IllegalStateException("No se puede iniciar un lote vacio.");
        }

        for (Trabajo trabajo : trabajos) {
            if (trabajo.getIntentos() > 0) {
                trabajo.registrarCargoReintento();
            }
        }
        estado = EJECUCION;
        for (Trabajo trabajo : trabajos) {
            trabajo.setEstado(Trabajo.EJECUCION);
        }
    }

    public void registrarResultado(Trabajo trabajo, boolean completado) {
        if (!estado.equals(EJECUCION)) {
            throw new IllegalStateException("Los resultados solo se registran en un lote en ejecucion.");
        }
        if (trabajo == null || !trabajos.contains(trabajo)) {
            throw new IllegalArgumentException("El trabajo no pertenece a este lote.");
        }
        if (resultadosRegistrados.contains(trabajo)) {
            throw new IllegalStateException("Este trabajo ya tiene un resultado registrado en el lote.");
        }

        trabajo.aumentarIntentos();

        if (completado) {
            trabajo.setEstado(Trabajo.COMPLETADO);
        } else if (trabajo.getIntentos() < trabajo.getMaxIntentos()) {
            trabajo.setEstado(Trabajo.EN_COLA);
        } else {
            trabajo.setEstado(Trabajo.CANCELADO);
        }

        resultadosRegistrados.add(trabajo);

        if (resultadosRegistrados.size() == trabajos.size()) {
            cerrarLote();
        }
    }
    public void cerrarLote() {
        if (!estado.equals(EJECUCION)) {
            throw new IllegalStateException("Solo se puede cerrar un lote en ejecucion.");
        }
        if (resultadosRegistrados.size() != trabajos.size()) {
            throw new IllegalStateException("No se puede cerrar el lote porque faltan resultados.");
        }
        estado = CERRADO;
    }
    @Override
    public String toString() {
        return "Nodo: " + nodo.getCodigo()
                + "Estado: " + estado
                + "Trabajos: " + trabajos.size()
                + "Horas asignadas: " + String.format("%.2f", calcularHoras())
                + "Capacidad disponible: " + String.format("%.2f", calcularCapacidadDisponible());
    }
}
