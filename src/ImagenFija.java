public class ImagenFija extends Trabajo {

    public ImagenFija(String codigo, String estudio, String proyecto,
            double horasEstimadas, String horario) {
        super(codigo, estudio, proyecto, horasEstimadas, horario);

        if (horasEstimadas > 2) {
            throw new IllegalArgumentException("Una imagen fija no puede superar 2 horas estimadas.");
        }
    }
    @Override
    public String getCategoria() {
        return "Imagen fija";
    }
    @Override
    public double calcularCosto() {
        if (getHorario().equals("Diurno")) {
            return 45;
        }
        return 30;
    }
    @Override
    public int calcularTiempoEntrega() {
        if (getHorario().equals("Diurno")) {
            return 4;
        }
        return 10;
    }
    @Override
    public boolean puedeUsarNodo(Nodo nodo) {
        return true;
    }
    @Override
    public int getMaxIntentos() {
        return 3;
    }
}
