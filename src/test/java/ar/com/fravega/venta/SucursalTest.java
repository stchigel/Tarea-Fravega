package ar.com.fravega.venta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.com.fravega.producto.Producto;
import ar.com.fravega.producto.RegimenImpositivo;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class SucursalTest {

  private final RelojAjustable reloj = new RelojAjustable(Instant.parse("2026-10-05T10:00:00Z"));
  private final RegimenImpositivo regimen = RegimenImpositivo.vigente();
  private final Sucursal sucursal = new Sucursal("Centro", reloj);
  private final Producto tv = new Producto("TV", "Televisor", new BigDecimal("1000"), regimen.electronico());

  @Test
  void laVentaRegistraSucursalImporteYFecha() {
    Venta venta = sucursal.vender(tv);

    assertEquals("Centro", venta.sucursal());
    assertEquals(tv, venta.producto());
    assertEquals(new BigDecimal("1241.25"), venta.importe());
    assertEquals(LocalDateTime.of(2026, 10, 5, 10, 0), venta.fechaHora());
  }

  @Test
  void notificaATodosLosInteresados() {
    List<Venta> recibidasA = new ArrayList<>();
    List<Venta> recibidasB = new ArrayList<>();
    sucursal.suscribir(recibidasA::add);
    sucursal.suscribir(recibidasB::add);

    Venta venta = sucursal.vender(tv);

    assertEquals(List.of(venta), recibidasA);
    assertEquals(List.of(venta), recibidasB);
  }

  @Test
  void unInteresadoQueFallaNoAfectaALosDemas() {
    List<Venta> recibidas = new ArrayList<>();
    sucursal.suscribir(venta -> {
      throw new IllegalStateException("sistema caído");
    });
    sucursal.suscribir(recibidas::add);

    sucursal.vender(tv);

    assertEquals(1, recibidas.size());
  }

  @Test
  void unInteresadoDesuscriptoNoRecibeVentas() {
    List<Venta> recibidas = new ArrayList<>();
    ObservadorDeVentas observador = recibidas::add;
    sucursal.suscribir(observador);
    sucursal.desuscribir(observador);

    sucursal.vender(tv);

    assertTrue(recibidas.isEmpty());
  }

  @Test
  void elImporteDeUnaVentaNoCambiaSiLuegoCambianLosImpuestos() {
    Venta venta = sucursal.vender(tv);
    regimen.iva().actualizarAlicuota(new BigDecimal("0.30"));

    assertEquals(new BigDecimal("1241.25"), venta.importe());
    assertEquals(new BigDecimal("1331.25"), tv.precioFinal());
  }
}
