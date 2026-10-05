package ar.com.fravega.producto;

import ar.com.fravega.impuesto.EI;
import ar.com.fravega.impuesto.EO;
import ar.com.fravega.impuesto.IVA;
import ar.com.fravega.impuesto.Impuesto;

/**
 * Concentra las instancias de impuestos vigentes y crea los tipos de producto a partir de ellas.
 * Como los tipos comparten las mismas instancias, actualizar la alícuota del IVA o las
 * ganancias impositivas en un único lugar impacta en todos los productos.
 */
public class RegimenImpositivo {

  private final IVA iva;
  private final EO eo;
  private final EI ei;

  public RegimenImpositivo(IVA iva, EO eo, EI ei) {
    this.iva = iva;
    this.eo = eo;
    this.ei = ei;
  }

  public static RegimenImpositivo vigente() {
    return new RegimenImpositivo(IVA.vigente(), EO.vigente(), EI.vigente());
  }

  public TipoProducto electronico() {
    return nuevoTipo("Electrónico", eo);
  }

  public TipoProducto hogar() {
    return nuevoTipo("Hogar", ei);
  }

  /** Crea un tipo de producto nuevo; el IVA se aplica siempre. */
  public TipoProducto nuevoTipo(String nombre, Impuesto... impuestosAdicionales) {
    return new TipoProducto(nombre, iva, impuestosAdicionales);
  }

  public IVA iva() {
    return iva;
  }

  public EO eo() {
    return eo;
  }

  public EI ei() {
    return ei;
  }
}
