package ar.com.fravega.impuesto;

import java.math.BigDecimal;

/** EO = (0,5 * precioBase) / (4 * gananciasImpositivas). */
public class EO extends ImpuestoSobreGanancias {

  public static final BigDecimal GANANCIAS_VIGENTES = new BigDecimal("4");

  private static final BigDecimal MEDIO = new BigDecimal("0.5");
  private static final BigDecimal CUATRO = new BigDecimal("4");

  public EO(BigDecimal gananciasImpositivas) {
    super(gananciasImpositivas);
  }

  public static EO vigente() {
    return new EO(GANANCIAS_VIGENTES);
  }

  @Override
  public String nombre() {
    return "EO";
  }

  @Override
  public BigDecimal calcular(BigDecimal precioBase) {
    BigDecimal divisor = CUATRO.multiply(getGananciasImpositivas(), PRECISION);
    return MEDIO.multiply(precioBase, PRECISION).divide(divisor, PRECISION);
  }
}
