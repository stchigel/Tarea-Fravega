package ar.com.fravega.venta;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Lleva la cuenta de la cantidad de ventas del día (1, 2, 3...). Al cambiar el día la cuenta
 * vuelve a empezar. Se usa para el control cruzado: arqueo de caja, stock del depósito y
 * libros contables.
 */
public class ContadorDeVentas implements ObservadorDeVentas {

  private final Clock reloj;
  private LocalDate dia;
  private int cantidad;

  public ContadorDeVentas() {
    this(Clock.systemDefaultZone());
  }

  public ContadorDeVentas(Clock reloj) {
    this.reloj = Objects.requireNonNull(reloj, "El reloj es obligatorio");
    this.dia = LocalDate.now(reloj);
  }

  @Override
  public synchronized void ventaRealizada(Venta venta) {
    reiniciarSiCambioElDia();
    cantidad++;
  }

  public synchronized int cantidadDelDia() {
    reiniciarSiCambioElDia();
    return cantidad;
  }

  private void reiniciarSiCambioElDia() {
    LocalDate hoy = LocalDate.now(reloj);
    if (!hoy.equals(dia)) {
      dia = hoy;
      cantidad = 0;
    }
  }
}
