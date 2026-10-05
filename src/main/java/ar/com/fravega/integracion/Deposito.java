package ar.com.fravega.integracion;

import ar.com.fravega.venta.ObservadorDeVentas;
import ar.com.fravega.venta.Venta;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Adapter hacia el sistema del depósito (Web API):
 * por cada venta le indica qué producto preparar para entregar al cliente.
 */
public class Deposito implements ObservadorDeVentas {

  private final URI endpointPreparacion;
  private final ClienteHttp clienteHttp;

  public Deposito(URI endpointPreparacion, ClienteHttp clienteHttp) {
    this.endpointPreparacion = Objects.requireNonNull(endpointPreparacion);
    this.clienteHttp = Objects.requireNonNull(clienteHttp);
  }

  @Override
  public void ventaRealizada(Venta venta) {
    Map<String, Object> pedido = new LinkedHashMap<>();
    pedido.put("sucursal", venta.sucursal());
    pedido.put("codigoProducto", venta.producto().getCodigo());
    pedido.put("producto", venta.producto().getNombre());
    pedido.put("tipoProducto", venta.producto().getTipo().getNombre());
    pedido.put("fechaVenta", venta.fechaHora().toString());
    clienteHttp.postJson(endpointPreparacion, Json.objeto(pedido));
  }
}
