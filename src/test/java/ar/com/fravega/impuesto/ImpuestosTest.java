package ar.com.fravega.impuesto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ImpuestosTest {

  private static final BigDecimal MIL = new BigDecimal("1000");

  @Test
  void ivaEsElVeintiunoPorCientoDelPrecioBase() {
    assertIgual("210", IVA.vigente().calcular(MIL));
  }

  @Test
  void ivaPuedeIncrementarSuAlicuota() {
    IVA iva = IVA.vigente();
    iva.actualizarAlicuota(new BigDecimal("0.27"));
    assertIgual("270", iva.calcular(MIL));
  }

  @Test
  void eoSeCalculaConGananciasDeCuatroPesos() {
    // (0,5 * 1000) / (4 * 4) = 31,25
    assertIgual("31.25", EO.vigente().calcular(MIL));
  }

  @Test
  void eiSeCalculaConGananciasDeTresCincuenta() {
    // 1000 / 4 + 0,3 * 3,50 = 251,05
    assertIgual("251.05", EI.vigente().calcular(MIL));
  }

  @Test
  void lasGananciasImpositivasPuedenCambiar() {
    EO eo = EO.vigente();
    eo.actualizarGananciasImpositivas(new BigDecimal("5"));
    assertIgual("25", eo.calcular(MIL));

    EI ei = EI.vigente();
    ei.actualizarGananciasImpositivas(new BigDecimal("2"));
    assertIgual("250.6", ei.calcular(MIL));
  }

  @Test
  void noSeAdmitenValoresInvalidos() {
    assertThrows(IllegalArgumentException.class, () -> new IVA(new BigDecimal("-0.01")));
    assertThrows(IllegalArgumentException.class, () -> new EO(BigDecimal.ZERO));
    assertThrows(IllegalArgumentException.class, () -> EI.vigente().actualizarGananciasImpositivas(new BigDecimal("-1")));
  }

  private static void assertIgual(String esperado, BigDecimal obtenido) {
    assertEquals(0, new BigDecimal(esperado).compareTo(obtenido), () -> "esperado " + esperado + " pero fue " + obtenido);
  }
}
