package ar.com.fravega.venta;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.com.fravega.producto.Producto;
import ar.com.fravega.producto.RegimenImpositivo;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ContadorDeVentasTest {

  private final RelojAjustable reloj = new RelojAjustable(Instant.parse("2026-10-05T10:00:00Z"));
  private final ContadorDeVentas contador = new ContadorDeVentas(reloj);
  private final Sucursal sucursal = new Sucursal("Centro", reloj);
  private final Producto producto =
      new Producto("TV", "Televisor", new BigDecimal("1000"), RegimenImpositivo.vigente().electronico());

  @Test
  void cuentaLasVentasDelDia() {
    sucursal.suscribir(contador);
    assertEquals(0, contador.cantidadDelDia());

    sucursal.vender(producto);
    sucursal.vender(producto);
    sucursal.vender(producto);

    assertEquals(3, contador.cantidadDelDia());
  }

  @Test
  void reiniciaLaCuentaAlCambiarElDia() {
    sucursal.suscribir(contador);
    sucursal.vender(producto);
    sucursal.vender(producto);

    reloj.avanzar(Duration.ofDays(1));
    assertEquals(0, contador.cantidadDelDia());

    sucursal.vender(producto);
    assertEquals(1, contador.cantidadDelDia());
  }
}
