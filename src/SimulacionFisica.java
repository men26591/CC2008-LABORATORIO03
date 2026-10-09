public class SimulacionFisica extends Trabajo {
    private double resolucionMalla;

    public SimulacionFisica(String codigo, String estudio, String proyecto,
            double horasEstimadas, String horario, double resolucionMalla) {
        super(codigo, estudio, proyecto, horasEstimadas, horario);

        if (!horario.equalsIgnoreCase("Nocturno")) {
            throw new IllegalArgumentException("Una simulacion fisica solo puede registrarse en horario nocturno.");
        }
        if (resolucionMalla < 1 || resolucionMalla > 50
                || Double.isNaN(resolucionMalla) || Double.isInfinite(resolucionMalla)) {
            throw new IllegalArgumentException("La resolucion de malla debe estar entre 1 y 50 millones.");
        }
        this.resolucionMalla = resolucionMalla;
    }

    public double getResolucionMalla() {
        return resolucionMalla;
    }
    @Override
    public String getCategoria() {
        return "Simulacion fisica";
    }
    @Override
    public double calcularCosto() {
        double tarifaBase = 40;
        return tarifaBase + getHorasEstimadas() * 22 + 65;
    }
    @Override
    public int calcularTiempoEntrega() {
        return 18;
    }
    @Override
    public boolean puedeUsarNodo(Nodo nodo) {
        return nodo.tieneGPU();
    }
    @Override
    public int getMaxIntentos() {
        return 1;
    }
    @Override
    public String toString() {
        return super.toString() + " | Malla: " + resolucionMalla + " millones";
    }
}
