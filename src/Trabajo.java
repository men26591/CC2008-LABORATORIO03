public abstract class Trabajo {
    public static final String EN_COLA = "En cola";
    public static final String ASIGNADO = "Asignado";
    public static final String EJECUCION = "En ejecucion";
    public static final String COMPLETADO = "Completado";
    public static final String CANCELADO = "Cancelado";

    private String codigo;
    private String estudio;
    private String proyecto;
    private double horasEstimadas;
    private String horario;
    private String estado;
    private int intentos;
    private double ingresoRegistro;
    private double ingresoReintentos;

public Trabajo(String codigo, String estudio, String proyecto,
        double horasEstimadas, String horario) {

    if (codigo == null || codigo.equals("")) {
        throw new IllegalArgumentException("El codigo del trabajo no puede estar vacio.");
    }

    if (estudio == null || estudio.equals("")) {
        throw new IllegalArgumentException("El nombre del estudio no puede estar vacio.");
    }

    if (proyecto == null || proyecto.equals("")) {
        throw new IllegalArgumentException("El nombre del proyecto no puede estar vacio.");
    }

    if (horasEstimadas <= 0) {
        throw new IllegalArgumentException("Las horas estimadas deben ser mayores que cero.");
    }

    if (horario == null) {
        throw new IllegalArgumentException("El horario no puede estar vacio.");
    }

    if (!horario.equalsIgnoreCase("Diurno") &&
        !horario.equalsIgnoreCase("Nocturno")) {
        throw new IllegalArgumentException("El horario debe ser Diurno o Nocturno.");
    }

    this.codigo = codigo;
    this.estudio = estudio;
    this.proyecto = proyecto;
    this.horasEstimadas = horasEstimadas;

    if (horario.equalsIgnoreCase("Diurno")) {
        this.horario = "Diurno";
    } else {
        this.horario = "Nocturno";
    }

    this.estado = EN_COLA;
    this.intentos = 0;
    this.ingresoRegistro = 0;
    this.ingresoReintentos = 0;
}

    public String getCodigo() {
        return codigo;
    }

    public String getEstudio() {
        return estudio;
    }

    public String getProyecto() {
        return proyecto;
    }

    public double getHorasEstimadas() {
        return horasEstimadas;
    }

    public String getHorario() {
        return horario;
    }

    public String getEstado() {
        return estado;
    }

    public int getIntentos() {
        return intentos;
    }

    public double getIngresoRegistro() {
        return ingresoRegistro;
    }

    public double getIngresoReintentos() {
        return ingresoReintentos;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void aumentarIntentos() {
        intentos++;
    }

    public void registrarIngresoInicial() {
        ingresoRegistro = calcularCosto();
    }

    public void registrarCargoReintento() {
        ingresoReintentos += 25;
    }

    public abstract String getCategoria();
    public abstract double calcularCosto();
    public abstract int calcularTiempoEntrega();
    public abstract boolean puedeUsarNodo(Nodo nodo);
    public abstract int getMaxIntentos();
    @Override
    public String toString() {
        return "Codigo: " + codigo
                + "Categoria: " + getCategoria()
                + "Estudio: " + estudio
                + "Proyecto: " + proyecto
                + "Horas: " + horasEstimadas
                + "Horario: " + horario
                + "Estado: " + estado
                + "Intentos: " + intentos + "/" + getMaxIntentos()
                + "Costo: Q" + String.format("%.2f", calcularCosto())
                + "Entrega: " + calcularTiempoEntrega() + " horas";
    }
}
