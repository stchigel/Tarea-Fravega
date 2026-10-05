package ar.com.fravega.producto;

import ar.com.fravega.impuesto.IVA;
import ar.com.fravega.impuesto.Impuesto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Categoría de producto (electrónico, hogar, ...) con los impuestos que se le aplican.
 * El IVA es obligatorio para todo tipo de producto, por eso se exige en el constructor
 * y no se puede quitar. El resto de los impuestos se componen libremente.
 */
public class TipoProducto {

  private final String nombre;
  private final List<Impuesto> impuestos = new ArrayList<>();

  public TipoProducto(String nombre, IVA iva, Impuesto... impuestosAdicionales) {
    this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
    this.impuestos.add(Objects.requireNonNull(iva, "Todo tipo de producto debe aplicar IVA"));
    for (Impuesto impuesto : impuestosAdicionales) {
      agregarImpuesto(impuesto);
    }
  }

  public void agregarImpuesto(Impuesto impuesto) {
    Objects.requireNonNull(impuesto, "El impuesto es obligatorio");
    if (!impuestos.contains(impuesto)) {
      impuestos.add(impuesto);
    }
  }

  public BigDecimal totalImpuestos(BigDecimal precioBase) {
    return impuestos.stream()
        .map(impuesto -> impuesto.calcular(precioBase))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public String getNombre() {
    return nombre;
  }

  public List<Impuesto> getImpuestos() {
    return List.copyOf(impuestos);
  }
}
