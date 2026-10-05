package ar.com.fravega.impuesto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Impuesto cuyo cálculo depende de un valor de "ganancias impositivas"
 * que puede cambiar en el futuro (aumentar o disminuir).
 */
public abstract class ImpuestoSobreGanancias implements Impuesto {

  private BigDecimal gananciasImpositivas;

  protected ImpuestoSobreGanancias(BigDecimal gananciasImpositivas) {
    actualizarGananciasImpositivas(gananciasImpositivas);
  }

  public void actualizarGananciasImpositivas(BigDecimal nuevasGanancias) {
    Objects.requireNonNull(nuevasGanancias, "Las ganancias impositivas son obligatorias");
    if (nuevasGanancias.signum() <= 0) {
      throw new IllegalArgumentException("Las ganancias impositivas deben ser positivas: " + nuevasGanancias);
    }
    this.gananciasImpositivas = nuevasGanancias;
  }

  public BigDecimal getGananciasImpositivas() {
    return gananciasImpositivas;
  }
}
