package ar.com.fravega.producto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Producto {

  private final String codigo;
  private final String nombre;
  private final BigDecimal precioBase;
  private final TipoProducto tipo;

  public Producto(String codigo, String nombre, BigDecimal precioBase, TipoProducto tipo) {
    this.codigo = Objects.requireNonNull(codigo, "El código es obligatorio");
    this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
    this.precioBase = Objects.requireNonNull(precioBase, "El precio base es obligatorio");
    this.tipo = Objects.requireNonNull(tipo, "El tipo de producto es obligatorio");
    if (precioBase.signum() < 0) {
      throw new IllegalArgumentException("El precio base no puede ser negativo: " + precioBase);
    }
  }

  /** PrecioFinal = Σ ImpuestosAplicados + precioBase, redondeado a centavos. */
  public BigDecimal precioFinal() {
    return precioBase.add(tipo.totalImpuestos(precioBase)).setScale(2, RoundingMode.HALF_UP);
  }

  public String getCodigo() {
    return codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public BigDecimal getPrecioBase() {
    return precioBase;
  }

  public TipoProducto getTipo() {
    return tipo;
  }
}
