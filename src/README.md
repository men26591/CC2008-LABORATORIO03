# Laboratorio 3: Herencia y Polimorfismo - RenderGT

## Descripción
Este proyecto es una aplicación de consola desarrollada en Java para la gestión de trabajos de renderizado en la empresa **RenderGT**. El sistema administra cuatro categorías de trabajos (Imagen Fija, Animación, Animación con Postproducción y Simulación Física), calculando costos, tiempos de entrega, compatibilidad con nodos de cómputo (GPU) y límites de reintentos mediante herencia y polimorfismo. 

## Archivos Java

- `Trabajo.java`: clase abstracta con la informacion comun, estados, intentos e ingresos.
- `ImagenFija.java`: costo fijo y entrega segun horario.
- `Animacion.java`: cobro por horas estimadas o por fotogramas, usando el valor mayor.
- `AnimacionPostproduccion.java`: agrega composicion y recargo por recursos licenciados.
- `SimulacionFisica.java`: horario nocturno, cobro por horas y restriccion de GPU.
- `Nodo.java`: capacidad maxima y disponibilidad de GPU.
- `Lote.java`: asignaciones, retiros, ejecuciones, resultados y reintentos.
- `RenderGT.java`: registro, busquedas, creacion de lotes y reportes.
- `Principal.java`: menu y validacion de entradas.

## Requisitos Previos
* **Java Development Kit (JDK):** Versión 11 o superior instalada.
* **Terminal/Línea de Comandos:** Bash, PowerShell o CMD.

## Instrucciones de Compilación y Ejecución

Desde la carpeta del proyecto:

```bash
-d bin src/*.java
-cp bin Principal
```

El programa inicia con ocho trabajos (dos por categoria) y tres nodos. Estos datos iniciales no generan ingresos.

## Opciones del menu

1. Cotizar sin registrar.
2. Cotizar y registrar si el estudio acepta.
3. Registrar nodos.
4. Crear lotes.
5. Asignar trabajos.
6. Retirar trabajos.
7. Iniciar lotes.
8. Registrar resultados.
9. Consultar trabajos.
10. Consultar nodos.
11. Generar reportes.
0. Salir.

Los lotes se cierran automaticamente cuando todos sus trabajos tienen un resultado registrado.

## Aclaracion de la tarifa de simulacion

El enunciado dice que la simulacion utiliza la tarifa base segun el horario. Se aplico la tarifa base ya indicada de Q60 diurna y Q40 nocturna. Como una simulacion diurna es rechazada, la tarifa base utilizada en la practica es Q40.

## Ajustes que deben aparecer en el UML

Para que el diagrama quede completamente consistente con los ingresos y reportes solicitados, se agregaron estos elementos:

### Trabajo

- `ingresoRegistro : double`
- `ingresoReintentos : double`
- `registrarIngresoInicial() : void`
- `registrarCargoReintento() : void`
- `getIngresoRegistro() : double`
- `getIngresoReintentos() : double`
- `getCategoria() : String {abstract}`

### Lote

- `resultadosRegistrados : ArrayList<Trabajo>`
- `calcularCapacidadDisponible() : double`

### RenderGT

- `generarReporteTrabajos() : String`
- `generarReporteIngresos() : String`
- `generarReporteLotesActivos() : String`

## Autor
* **Nombre:** Elisa Menéndez
* **Carné:** 26591
* **Curso:** CC2008 - Programación Orientada a Objetos
* **Sección:** 60
* **Universidad:** Universidad del Valle de Guatemala