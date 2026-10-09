import java.util.ArrayList;

public class RenderGT {
    private ArrayList<Trabajo> trabajos;
    private ArrayList<Nodo> nodos;
    private ArrayList<Lote> lotes;

    public RenderGT() {
        trabajos = new ArrayList<Trabajo>();
        nodos = new ArrayList<Nodo>();
        lotes = new ArrayList<Lote>();
    }

    public ArrayList<Trabajo> getTrabajos() {
        return trabajos;
    }

    public ArrayList<Nodo> getNodos() {
        return nodos;
    }

    public ArrayList<Lote> getLotes() {
        return lotes;
    }

    public void cargarDatosIniciales() {
        if (!trabajos.isEmpty() || !nodos.isEmpty()) {
            throw new IllegalStateException("Los datos iniciales ya fueron cargados.");
        }

        trabajos.add(new ImagenFija("IF001", "Estudio Luz", "Retrato", 1.5, "Diurno"));
        trabajos.add(new ImagenFija("IF002", "Estudio Noche", "Portada", 2, "Nocturno"));
        trabajos.add(new Animacion("AN001", "Motion GT", "Corto", 9.5, "Diurno", 610));
        trabajos.add(new Animacion("AN002", "Pixeles", "Anuncio", 3.2, "Nocturno", 150));
        trabajos.add(new AnimacionPostproduccion("AP001", "Motion GT", "Trailer", 9.5,
                "Diurno", 610, 1800));
        trabajos.add(new AnimacionPostproduccion("AP002", "VFX Maya", "Intro", 4,
                "Nocturno", 150, 500));
        trabajos.add(new SimulacionFisica("SF001", "Fluidos GT", "Oceano", 5.5,
                "Nocturno", 10));
        trabajos.add(new SimulacionFisica("SF002", "Tela Studio", "Vestuario", 8,
                "Nocturno", 40));

        nodos.add(new Nodo("N01", 12, false));
        nodos.add(new Nodo("N02", 30, true));
        nodos.add(new Nodo("N03", 8, false));
    }

    public void registrarTrabajo(Trabajo trabajo) {
        if (trabajo == null) {
            throw new IllegalArgumentException("El trabajo no puede ser nulo.");
        }
        if (buscarTrabajo(trabajo.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un trabajo con el codigo " + trabajo.getCodigo() + ".");
        }
        trabajo.registrarIngresoInicial();
        trabajos.add(trabajo);
    }
    public Trabajo buscarTrabajo(String codigo) {
        for (Trabajo trabajo : trabajos) {
            if (trabajo.getCodigo().equalsIgnoreCase(codigo)) {
                return trabajo;
            }
        }
        return null;
    }
    public void registrarNodo(Nodo nodo) {
        if (nodo == null) {
            throw new IllegalArgumentException("El nodo no puede ser nulo.");
        }
        if (buscarNodo(nodo.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un nodo con el codigo " + nodo.getCodigo() + ".");
        }

        nodos.add(nodo);
    }
    public Nodo buscarNodo(String codigo) {
        for (Nodo nodo : nodos) {
            if (nodo.getCodigo().equalsIgnoreCase(codigo)) {
                return nodo;
            }
        }
        return null;
    }

    public Lote crearLote(Nodo nodo) {
        if (nodo == null || !nodos.contains(nodo)) {
            throw new IllegalArgumentException("El nodo indicado no esta registrado.");
        }
        for (Lote lote : lotes) {
            if (lote.getNodo() == nodo && !lote.getEstado().equals(Lote.CERRADO)) {
                throw new IllegalStateException("El nodo ya tiene un lote sin cerrar.");
            }
        }
        Lote lote = new Lote(nodo);
        lotes.add(lote);
        return lote;
    }
    public double cotizar(Trabajo trabajo) {
        if (trabajo == null) {
            throw new IllegalArgumentException("El trabajo no puede ser nulo.");
        }
        return trabajo.calcularCosto();
    }
    public int consultarTiempoEntrega(Trabajo trabajo) {
        if (trabajo == null) {
            throw new IllegalArgumentException("El trabajo no puede ser nulo.");
        }
        return trabajo.calcularTiempoEntrega();
    }
    public String generarReporteTrabajos() {
        ArrayList<String> categorias = obtenerCategorias();
        String[] estados = {Trabajo.EN_COLA, Trabajo.ASIGNADO, Trabajo.EJECUCION,
            Trabajo.COMPLETADO, Trabajo.CANCELADO};
        String reporte = "REPORTE DE TRABAJOS\n";

        for (String categoria : categorias) {
            reporte += "\n" + categoria + ":\n";
            for (String estadoBuscado : estados) {
                int cantidad = 0;
                for (Trabajo trabajo : trabajos) {
                    if (trabajo.getCategoria().equals(categoria)
                            && trabajo.getEstado().equals(estadoBuscado)) {
                        cantidad++;
                    }
                }
                reporte += "  " + estadoBuscado + ": " + cantidad + "\n";
            }
        }
        return reporte;
    }

    public String generarReporteIngresos() {
        ArrayList<String> categorias = obtenerCategorias();
        double totalRegistro = 0;
        double totalReintentos = 0;
        String reporte = "REPORTE DE INGRESOS\n";

        for (Trabajo trabajo : trabajos) {
            totalRegistro += trabajo.getIngresoRegistro();
            totalReintentos += trabajo.getIngresoReintentos();
        }

        reporte += "Ingresos por registro: Q" + String.format("%.2f", totalRegistro) + "\n";
        reporte += "Ingresos por reintentos: Q" + String.format("%.2f", totalReintentos) + "\n";
        reporte += "Ingreso total: Q" + String.format("%.2f", totalRegistro + totalReintentos) + "\n";
        reporte += "\nIngresos por categoria:\n";

        for (String categoria : categorias) {
            double registroCategoria = 0;
            double reintentosCategoria = 0;
            for (Trabajo trabajo : trabajos) {
                if (trabajo.getCategoria().equals(categoria)) {
                    registroCategoria += trabajo.getIngresoRegistro();
                    reintentosCategoria += trabajo.getIngresoReintentos();
                }
            }
            reporte += "  " + categoria
                    + ": Q" + String.format("%.2f", registroCategoria + reintentosCategoria)
                    + " (registro Q" + String.format("%.2f", registroCategoria)
                    + ", reintentos Q" + String.format("%.2f", reintentosCategoria) + ")\n";
        }
        return reporte;
    }

    public String generarReporteLotesActivos() {
        String reporte = "LOTES NO CERRADOS\n";
        int cantidad = 0;

        for (int i = 0; i < lotes.size(); i++) {
            Lote lote = lotes.get(i);
            if (!lote.getEstado().equals(Lote.CERRADO)) {
                cantidad++;
                reporte += "Lote " + (i + 1)
                        + "Nodo: " + lote.getNodo().getCodigo()
                        + "Estado: " + lote.getEstado()
                        + "Horas: " + String.format("%.2f", lote.calcularHoras())
                        + "Disponible: " + String.format("%.2f", lote.calcularCapacidadDisponible())
                        + "\n";
            }
        }
        if (cantidad == 0) {
            reporte += "No hay lotes sin cerrar.\n";
        }
        return reporte;
    }

    private ArrayList<String> obtenerCategorias() {
        ArrayList<String> categorias = new ArrayList<String>();
        for (Trabajo trabajo : trabajos) {
            if (!categorias.contains(trabajo.getCategoria())) {
                categorias.add(trabajo.getCategoria());
            }
        }
        return categorias;
    }
}
