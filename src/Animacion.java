public class Animacion extends Trabajo {
    private int fotogramas;

    public Animacion(String codigo, String estudio, String proyecto,
            double horasEstimadas, String horario, int fotogramas) {
        super(codigo, estudio, proyecto, horasEstimadas, horario);

        if (fotogramas <= 0) {
            throw new IllegalArgumentException("La cantidad de fotogramas debe ser positiva.");
        }
        this.fotogramas = fotogramas;
    }

    public int getFotogramas() {
        return fotogramas;
    }

    @Override
    public String getCategoria() {
        return "Animacion";
    }

    public double calcularHorasFacturables() {
        double horasPorFotogramas = fotogramas * 0.02;
        return Math.max(getHorasEstimadas(), horasPorFotogramas);
    }
    @Override
    public double calcularCosto() {
        double tarifaBase = getHorario().equals("Diurno") ? 60 : 40;
        double horasCobradas = Math.ceil(calcularHorasFacturables());
        return tarifaBase + horasCobradas * 15;
    }
    @Override
    public int calcularTiempoEntrega() {
        if (getHorario().equals("Diurno")) {
            return 24;
        }
        return 36;
    }
    @Override
    public boolean puedeUsarNodo(Nodo nodo) {
        return true;
    }
    @Override
    public int getMaxIntentos() {
        return 3;
    }
    @Override
    public String toString() {
        return super.toString() + "Fotogramas: " + fotogramas;
    }
}
