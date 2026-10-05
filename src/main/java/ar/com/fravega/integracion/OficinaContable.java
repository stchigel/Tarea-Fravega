package ar.com.fravega.integracion;

import ar.com.fravega.venta.ObservadorDeVentas;
import ar.com.fravega.venta.Venta;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Adapter hacia el sistema de la oficina contable (interfaz REST):
 * por cada venta registra en los libros contables el valor de la venta.
 */
public class OficinaContable implements ObservadorDeVentas {

  private final URI endpointRegistroVentas;
  private final ClienteHttp clienteHttp;

  public OficinaContable(URI endpointRegistroVentas, ClienteHttp clienteHttp) {
    this.endpointRegistroVentas = Objects.requireNonNull(endpointRegistroVentas);
    this.clienteHttp = Objects.requireNonNull(clienteHttp);
  }

  @Override
  public void ventaRealizada(Venta venta) {
    Map<String, Object> asiento = new LinkedHashMap<>();
    asiento.put("fecha", venta.fechaHora().toString());
    asiento.put("sucursal", venta.sucursal());
    asiento.put("codigoProducto", venta.producto().getCodigo());
    asiento.put("importe", venta.importe());
    clienteHttp.postJson(endpointRegistroVentas, Json.objeto(asiento));
  }
}
