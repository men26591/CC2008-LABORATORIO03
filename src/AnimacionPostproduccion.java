public class AnimacionPostproduccion extends Animacion {
    private double valorRecursos;

    public AnimacionPostproduccion(String codigo, String estudio, String proyecto,
            double horasEstimadas, String horario, int fotogramas, double valorRecursos) {
        super(codigo, estudio, proyecto, horasEstimadas, horario, fotogramas);

        if (valorRecursos <= 0 || Double.isNaN(valorRecursos)
                || Double.isInfinite(valorRecursos)) {
            throw new IllegalArgumentException("El valor de los recursos debe ser positivo.");
        }
        this.valorRecursos = valorRecursos;
    }

    public double getValorRecursos() {
        return valorRecursos;
    }
    @Override
    public String getCategoria() {
        return "Animacion con postproduccion";
    }
    @Override
    public double calcularCosto() {
        double costoAnimacion = super.calcularCosto();
        double recargoRecursos = valorRecursos * 0.05;
        return costoAnimacion + 80 + recargoRecursos;
    }
    @Override
    public int calcularTiempoEntrega() {
        return super.calcularTiempoEntrega() + 12;
    }
    @Override
    public int getMaxIntentos() {
        return 2;
    }
    @Override
    public String toString() {
        return super.toString() + "Recursos: Q" + String.format("%.2f", valorRecursos);
    }
}
