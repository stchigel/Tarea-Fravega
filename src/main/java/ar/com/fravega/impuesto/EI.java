package ar.com.fravega.impuesto;

import java.math.BigDecimal;

/** EI = (precioBase / 4) + 0,3 * gananciasImpositivas. */
public class EI extends ImpuestoSobreGanancias {

  public static final BigDecimal GANANCIAS_VIGENTES = new BigDecimal("3.50");

  private static final BigDecimal CUATRO = new BigDecimal("4");
  private static final BigDecimal TRES_DECIMOS = new BigDecimal("0.3");

  public EI(BigDecimal gananciasImpositivas) {
    super(gananciasImpositivas);
  }

  public static EI vigente() {
    return new EI(GANANCIAS_VIGENTES);
  }

  @Override
  public String nombre() {
    return "EI";
  }

  @Override
  public BigDecimal calcular(BigDecimal precioBase) {
    return precioBase.divide(CUATRO, PRECISION)
        .add(TRES_DECIMOS.multiply(getGananciasImpositivas(), PRECISION), PRECISION);
  }
}
