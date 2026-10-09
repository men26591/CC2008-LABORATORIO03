import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Principal {
    private static Scanner teclado = new Scanner(System.in);
    private static DecimalFormat dinero = new DecimalFormat("0.00");

    public static void main(String[] args) {
        RenderGT sistema = new RenderGT();
        boolean continuar = true;

        sistema.cargarDatosIniciales();
        System.out.println("Bienvenido a RenderGT");
        System.out.println("Se cargaron 8 trabajos y 3 nodos sin ingresos registrados.");

        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opcion: ");

            try {
                switch (opcion) {
                    case 1:
                        cotizarTrabajo(sistema);
                        break;
                    case 2:
                        cotizarYRegistrarTrabajo(sistema);
                        break;
                    case 3:
                        registrarNodo(sistema);
                        break;
                    case 4:
                        crearLote(sistema);
                        break;
                    case 5:
                        asignarTrabajo(sistema);
                        break;
                    case 6:
                        retirarTrabajo(sistema);
                        break;
                    case 7:
                        iniciarLote(sistema);
                        break;
                    case 8:
                        registrarResultado(sistema);
                        break;
                    case 9:
                        consultarTrabajos(sistema);
                        break;
                    case 10:
                        consultarNodos(sistema);
                        break;
                    case 11:
                        mostrarReportes(sistema);
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("Programa finalizado.");
                        break;
                    default:
                        System.out.println("Opcion invalida. Intente nuevamente.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Operacion rechazada: " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("Operacion rechazada: " + e.getMessage());
            }
        }

        teclado.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n-- Menú RENDERGT --");
        System.out.println("1. Cotizar trabajo");
        System.out.println("2. Cotizar y registrar trabajo");
        System.out.println("3. Registrar nodo");
        System.out.println("4. Crear lote");
        System.out.println("5. Asignar trabajo a lote");
        System.out.println("6. Retirar trabajo de lote");
        System.out.println("7. Iniciar lote");
        System.out.println("8. Registrar resultado");
        System.out.println("9. Consultar trabajos");
        System.out.println("10. Consultar nodos");
        System.out.println("11. Generar reportes");
        System.out.println("0. Salir");
    }

    private static void cotizarTrabajo(RenderGT sistema) {
        Trabajo trabajo = crearTrabajoDesdeEntrada();

        mostrarCotizacion(sistema, trabajo);
        System.out.println("La cotizacion no fue registrada.");
    }
    private static void cotizarYRegistrarTrabajo(RenderGT sistema) {
        Trabajo trabajo = crearTrabajoDesdeEntrada();
        mostrarCotizacion(sistema, trabajo);
        boolean acepta = leerSiNo("¿El estudio acepta registrar y pagar el trabajo? (S/N): ");

        if (acepta) {
            sistema.registrarTrabajo(trabajo);
            System.out.println("Trabajo registrado. Ingreso: Q" + dinero.format(trabajo.getIngresoRegistro()));
        } else {
            System.out.println("Registro cancelado. No se genero ningun ingreso.");
        }
    }

    private static Trabajo crearTrabajoDesdeEntrada() {
        System.out.println("\nCategorias:");
        System.out.println("1. Imagen fija");
        System.out.println("2. Animacion");
        System.out.println("3. Animacion con postproduccion");
        System.out.println("4. Simulacion fisica");

        int categoria = leerEnteroEnRango("Seleccione la categoria: ", 1, 4);
        String codigo = leerTexto("Codigo: ");
        String estudio = leerTexto("Estudio: ");
        String proyecto = leerTexto("Proyecto: ");
        double horas = leerDoublePositivo("Horas estimadas: ");
        String horario = leerHorario();

        switch (categoria) {
            case 1:
                return new ImagenFija(codigo, estudio, proyecto, horas, horario);
            case 2:
                int fotogramas = leerEnteroPositivo("Cantidad de fotogramas: ");
                return new Animacion(codigo, estudio, proyecto, horas, horario, fotogramas);
            case 3:
                int fotogramasPost = leerEnteroPositivo("Cantidad de fotogramas: ");
                double recursos = leerDoublePositivo("Valor de recursos licenciados: Q");
                return new AnimacionPostproduccion(codigo, estudio, proyecto, horas,
                        horario, fotogramasPost, recursos);
            default:
                double malla = leerDoubleEnRango("Resolucion de malla (1 a 50 millones): ", 1, 50);
                return new SimulacionFisica(codigo, estudio, proyecto, horas, horario, malla);
        }
    }

    private static void mostrarCotizacion(RenderGT sistema, Trabajo trabajo) {
        System.out.println("\n--- COTIZACION ---");
        System.out.println("Categoria: " + trabajo.getCategoria());
        System.out.println("Costo: Q" + dinero.format(sistema.cotizar(trabajo)));
        System.out.println("Tiempo estimado: "
                + sistema.consultarTiempoEntrega(trabajo) + " horas");
    }

    private static void registrarNodo(RenderGT sistema) {
        String codigo = leerTexto("Codigo del nodo: ");
        double capacidad = leerDoublePositivo("Capacidad maxima por lote: ");
        boolean gpu = leerSiNo("¿El nodo tiene GPU? (S/N): ");

        Nodo nodo = new Nodo(codigo, capacidad, gpu);
        sistema.registrarNodo(nodo);
        System.out.println("Nodo registrado correctamente.");
    }

    private static void crearLote(RenderGT sistema) {
        consultarNodos(sistema);
        String codigoNodo = leerTexto("Codigo del nodo para el lote: ");
        Nodo nodo = sistema.buscarNodo(codigoNodo);

        Lote lote = sistema.crearLote(nodo);
        System.out.println("Lote creado en el nodo " + ((Nodo) lote.getNodo()).getCodigo() + ".");
    }

    private static void asignarTrabajo(RenderGT sistema) {
        Lote lote = seleccionarLote(sistema);
        String codigoTrabajo = leerTexto("Codigo del trabajo que desea asignar: ");
        Trabajo trabajo = sistema.buscarTrabajo(codigoTrabajo);

        lote.agregarTrabajo(trabajo);
        System.out.println("Trabajo asignado correctamente.");
    }

    private static void retirarTrabajo(RenderGT sistema) {
        Lote lote = seleccionarLote(sistema);
        mostrarTrabajosDelLote(lote);
        String codigoTrabajo = leerTexto("Codigo del trabajo que desea retirar: ");
        Trabajo trabajo = sistema.buscarTrabajo(codigoTrabajo);

        lote.retirarTrabajo(trabajo);
        System.out.println("Trabajo retirado. Volvio al estado En cola.");
    }

    private static void iniciarLote(RenderGT sistema) {
        Lote lote = seleccionarLote(sistema);
        lote.iniciarLote();
        System.out.println("Lote iniciado. Sus trabajos estan En ejecucion.");
    }

    private static void registrarResultado(RenderGT sistema) {
        Lote lote = seleccionarLote(sistema);
        mostrarTrabajosDelLote(lote);
        String codigoTrabajo = leerTexto("Codigo del trabajo con resultado: ");
        Trabajo trabajo = sistema.buscarTrabajo(codigoTrabajo);
        boolean completado = leerSiNo("¿El trabajo fue completado? (S/N): ");
        lote.registrarResultado(trabajo, completado);
        System.out.println("Resultado registrado. Estado actual: " + trabajo.getEstado());
        if (lote.getEstado().equals(Lote.CERRADO)) {
            System.out.println("Todos los resultados fueron registrados. El lote quedo cerrado.");
        }
    }
    private static void consultarTrabajos(RenderGT sistema) {
        System.out.println("\n--- TRABAJOS ---");

        if (sistema.getTrabajos().isEmpty()) {
            System.out.println("No hay trabajos registrados.");
            return;
        }
        for (Trabajo trabajo : sistema.getTrabajos()) {
            System.out.println(trabajo);
        }
    }
    private static void consultarNodos(RenderGT sistema) {
        System.out.println("\n--- NODOS ---");

        if (sistema.getNodos().isEmpty()) {
            System.out.println("No hay nodos registrados.");
            return;
        }
        for (Nodo nodo : sistema.getNodos()) {
            System.out.println(nodo);
        }
    }
    private static void mostrarReportes(RenderGT sistema) {
        System.out.println("\n1. Trabajos por categoria y estado");
        System.out.println("2. Ingresos");
        System.out.println("3. Lotes no cerrados");
        int opcion = leerEnteroEnRango("Seleccione el reporte: ", 1, 3);

        if (opcion == 1) {
            System.out.println(sistema.generarReporteTrabajos());
        } else if (opcion == 2) {
            System.out.println(sistema.generarReporteIngresos());
        } else {
            System.out.println(sistema.generarReporteLotesActivos());
        }
    }

    private static Lote seleccionarLote(RenderGT sistema) {
        ArrayList<Lote> lotes = sistema.getLotes();

        if (lotes.isEmpty()) {
            throw new IllegalStateException("No hay lotes creados.");
        }

        System.out.println("\n--- LOTES ---");
        for (int i = 0; i < lotes.size(); i++) {
            System.out.println((i + 1) + ". " + lotes.get(i));
        }
        int numero = leerEnteroEnRango("Seleccione el numero de lote: ", 1, lotes.size());
        return lotes.get(numero - 1);
    }

    private static void mostrarTrabajosDelLote(Lote lote) {
        System.out.println("\nTrabajos del lote:");

        if (lote.getTrabajos().isEmpty()) {
            System.out.println("El lote esta vacio.");
            return;
        }

        for (Trabajo trabajo : lote.getTrabajos()) {
            System.out.println("- " + trabajo.getCodigo() + " | " + trabajo.getEstado());
        }
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("La entrada no puede estar vacia.");
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private static int leerEnteroEnRango(String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("Ingrese un valor entre " + minimo + " y " + maximo + ".");
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);

            if (valor > 0) {
                return valor;
            }
            System.out.println("El valor debe ser mayor que cero.");
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        }
    }

    private static double leerDoublePositivo(String mensaje) {
        while (true) {
            double valor = leerDouble(mensaje);

            if (valor > 0 && !Double.isInfinite(valor) && !Double.isNaN(valor)) {
                return valor;
            }
            System.out.println("El valor debe ser un numero positivo.");
        }
    }

    private static double leerDoubleEnRango(String mensaje, double minimo, double maximo) {
        while (true) {
            double valor = leerDouble(mensaje);

            if (valor >= minimo && valor <= maximo
                    && !Double.isInfinite(valor) && !Double.isNaN(valor)) {
                return valor;
            }
            System.out.println("Ingrese un valor entre " + minimo + " y " + maximo + ".");
        }
    }

    private static String leerHorario() {
        while (true) {
            String horario = leerTexto("Horario (Diurno/Nocturno): ");

            if (horario.equalsIgnoreCase("Diurno")) {
                return "Diurno";
            }
            if (horario.equalsIgnoreCase("Nocturno")) {
                return "Nocturno";
            }
            System.out.println("El horario debe ser Diurno o Nocturno.");
        }
    }
    private static boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje);
            if (respuesta.equalsIgnoreCase("S") || respuesta.equalsIgnoreCase("Si")) {
                return true;
            }
            if (respuesta.equalsIgnoreCase("N") || respuesta.equalsIgnoreCase("No")) {
                return false;
            }
            System.out.println("Responda S o N.");
        }
    }
}
