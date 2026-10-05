package ar.com.fravega.venta;

/** Interesado en enterarse de cada venta realizada en una sucursal. */
public interface ObservadorDeVentas {

  void ventaRealizada(Venta venta);
}
