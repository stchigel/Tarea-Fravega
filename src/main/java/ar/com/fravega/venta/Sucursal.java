package ar.com.fravega.venta;

import ar.com.fravega.producto.Producto;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Sucursal de la cadena. Al vender un producto notifica a todos los interesados suscriptos.
 * Una falla de un interesado (por ejemplo, un sistema externo caído) no impide la venta
 * ni la notificación al resto.
 */
public class Sucursal {

  private static final Logger LOGGER = System.getLogger(Sucursal.class.getName());

  private final String nombre;
  private final Clock reloj;
  private final List<ObservadorDeVentas> observadores = new CopyOnWriteArrayList<>();

  public Sucursal(String nombre) {
    this(nombre, Clock.systemDefaultZone());
  }

  public Sucursal(String nombre, Clock reloj) {
    this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
    this.reloj = Objects.requireNonNull(reloj, "El reloj es obligatorio");
  }

  public void suscribir(ObservadorDeVentas observador) {
    observadores.add(Objects.requireNonNull(observador, "El observador es obligatorio"));
  }

  public void desuscribir(ObservadorDeVentas observador) {
    observadores.remove(observador);
  }

  public Venta vender(Producto producto) {
    Venta venta = new Venta(nombre, producto, producto.precioFinal(), LocalDateTime.now(reloj));
    notificar(venta);
    return venta;
  }

  private void notificar(Venta venta) {
    for (ObservadorDeVentas observador : observadores) {
      try {
        observador.ventaRealizada(venta);
      } catch (RuntimeException e) {
        LOGGER.log(Level.WARNING, "No se pudo notificar la venta a " + observador.getClass().getSimpleName(), e);
      }
    }
  }

  public String getNombre() {
    return nombre;
  }
}
