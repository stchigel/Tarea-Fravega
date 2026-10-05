package ar.com.fravega.venta;

import ar.com.fravega.producto.Producto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registro inmutable de una venta. El importe se fija al momento de vender,
 * así un cambio posterior de impuestos no altera ventas ya realizadas.
 */
public record Venta(String sucursal, Producto producto, BigDecimal importe, LocalDateTime fechaHora) {

  public Venta {
    Objects.requireNonNull(sucursal, "La sucursal es obligatoria");
    Objects.requireNonNull(producto, "El producto es obligatorio");
    Objects.requireNonNull(importe, "El importe es obligatorio");
    Objects.requireNonNull(fechaHora, "La fecha es obligatoria");
  }
}
