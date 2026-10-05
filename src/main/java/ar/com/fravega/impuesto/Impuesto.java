package ar.com.fravega.impuesto;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Impuesto aplicable al precio base de un producto.
 * Cada tipo de producto compone la lista de impuestos que le corresponden,
 * por lo que agregar un impuesto nuevo es crear una nueva implementación.
 */
public interface Impuesto {

  /** Precisión usada en los cálculos intermedios (el redondeo a centavos se hace en el precio final). */
  MathContext PRECISION = MathContext.DECIMAL64;

  String nombre();

  BigDecimal calcular(BigDecimal precioBase);
}
