package ar.com.fravega.impuesto;

import java.math.BigDecimal;
import java.util.Objects;

/** IVA = alicuota * precioBase. La alícuota es modificable porque se prevé que aumente. */
public class IVA implements Impuesto {

  public static final BigDecimal ALICUOTA_VIGENTE = new BigDecimal("0.21");

  private BigDecimal alicuota;

  public IVA(BigDecimal alicuota) {
    actualizarAlicuota(alicuota);
  }

  public static IVA vigente() {
    return new IVA(ALICUOTA_VIGENTE);
  }

  public void actualizarAlicuota(BigDecimal nuevaAlicuota) {
    Objects.requireNonNull(nuevaAlicuota, "La alícuota es obligatoria");
    if (nuevaAlicuota.signum() < 0) {
      throw new IllegalArgumentException("La alícuota no puede ser negativa: " + nuevaAlicuota);
    }
    this.alicuota = nuevaAlicuota;
  }

  public BigDecimal getAlicuota() {
    return alicuota;
  }

  @Override
  public String nombre() {
    return "IVA";
  }

  @Override
  public BigDecimal calcular(BigDecimal precioBase) {
    return alicuota.multiply(precioBase, PRECISION);
  }
}
