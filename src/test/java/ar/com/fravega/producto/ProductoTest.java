package ar.com.fravega.producto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.com.fravega.impuesto.Impuesto;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductoTest {

  private static final BigDecimal MIL = new BigDecimal("1000");

  private RegimenImpositivo regimen;

  @BeforeEach
  void setUp() {
    regimen = RegimenImpositivo.vigente();
  }

  @Test
  void electronicoAplicaIvaYEo() {
    // 1000 + 210 + 31,25
    Producto tv = new Producto("TV", "Televisor", MIL, regimen.electronico());
    assertEquals(new BigDecimal("1241.25"), tv.precioFinal());
  }

  @Test
  void hogarAplicaIvaYEi() {
    // 1000 + 210 + 251,05
    Producto sillon = new Producto("SI", "Sillón", MIL, regimen.hogar());
    assertEquals(new BigDecimal("1461.05"), sillon.precioFinal());
  }

  @Test
  void todoTipoNuevoAplicaIva() {
    TipoProducto jardin = regimen.nuevoTipo("Jardín");
    assertTrue(jardin.getImpuestos().contains(regimen.iva()));
    assertEquals(new BigDecimal("1210.00"), new Producto("PA", "Pala", MIL, jardin).precioFinal());
  }

  @Test
  void unTipoNoPuedeCrearseSinIva() {
    assertThrows(NullPointerException.class, () -> new TipoProducto("Sin IVA", null));
  }

  @Test
  void sePuedenAgregarImpuestosNuevosAUnTipo() {
    Impuesto tasaFija = new Impuesto() {
      @Override
      public String nombre() {
        return "Tasa fija";
      }

      @Override
      public BigDecimal calcular(BigDecimal precioBase) {
        return new BigDecimal("10");
      }
    };
    TipoProducto electronico = regimen.electronico();
    electronico.agregarImpuesto(tasaFija);

    assertEquals(new BigDecimal("1251.25"), new Producto("TV", "Televisor", MIL, electronico).precioFinal());
  }

  @Test
  void cambiarElRegimenImpactaEnTodosLosTipos() {
    Producto tv = new Producto("TV", "Televisor", MIL, regimen.electronico());
    Producto sillon = new Producto("SI", "Sillón", MIL, regimen.hogar());

    regimen.iva().actualizarAlicuota(new BigDecimal("0.25"));
    regimen.eo().actualizarGananciasImpositivas(new BigDecimal("5"));

    assertEquals(new BigDecimal("1275.00"), tv.precioFinal());    // 1000 + 250 + 25
    assertEquals(new BigDecimal("1501.05"), sillon.precioFinal()); // 1000 + 250 + 251,05
  }

  @Test
  void elPrecioFinalSeRedondeaACentavos() {
    // EO = 0,5 * 0,01 / 16 = 0,0003125 -> no llega a un centavo
    Producto p = new Producto("C", "Cable", new BigDecimal("0.01"), regimen.electronico());
    assertEquals(new BigDecimal("0.01"), p.precioFinal());
  }
}
