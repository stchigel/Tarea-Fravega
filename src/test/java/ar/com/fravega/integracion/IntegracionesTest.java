package ar.com.fravega.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ar.com.fravega.producto.Producto;
import ar.com.fravega.producto.RegimenImpositivo;
import ar.com.fravega.venta.Venta;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegracionesTest {

  private final Producto sillon =
      new Producto("SI-03", "Sillón \"Confort\"", new BigDecimal("1000"), RegimenImpositivo.vigente().hogar());
  private final Venta venta =
      new Venta("Centro", sillon, sillon.precioFinal(), LocalDateTime.of(2026, 10, 5, 10, 30));

  private HttpServer servidor;
  private final List<String> cuerposRecibidos = new ArrayList<>();
  private int codigoRespuesta = 201;

  @BeforeEach
  void levantarServidor() throws IOException {
    servidor = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    servidor.createContext("/", intercambio -> {
      cuerposRecibidos.add(intercambio.getRequestMethod() + " " + intercambio.getRequestURI().getPath() + " "
          + new String(intercambio.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
      intercambio.sendResponseHeaders(codigoRespuesta, -1);
      intercambio.close();
    });
    servidor.start();
  }

  @AfterEach
  void bajarServidor() {
    servidor.stop(0);
  }

  @Test
  void laOficinaContableRegistraElValorDeLaVenta() {
    new OficinaContable(url("/api/ventas"), new ClienteHttpJdk()).ventaRealizada(venta);

    assertEquals(List.of("POST /api/ventas "
        + "{\"fecha\":\"2026-10-05T10:30\",\"sucursal\":\"Centro\",\"codigoProducto\":\"SI-03\",\"importe\":1461.05}"),
        cuerposRecibidos);
  }

  @Test
  void elDepositoRecibeElProductoAPreparar() {
    new Deposito(url("/api/entregas"), new ClienteHttpJdk()).ventaRealizada(venta);

    assertEquals(List.of("POST /api/entregas "
        + "{\"sucursal\":\"Centro\",\"codigoProducto\":\"SI-03\",\"producto\":\"Sillón \\\"Confort\\\"\","
        + "\"tipoProducto\":\"Hogar\",\"fechaVenta\":\"2026-10-05T10:30\"}"),
        cuerposRecibidos);
  }

  @Test
  void unaRespuestaDeErrorSeInformaComoFallaDeIntegracion() {
    codigoRespuesta = 500;
    OficinaContable oficina = new OficinaContable(url("/api/ventas"), new ClienteHttpJdk());

    assertThrows(IntegracionException.class, () -> oficina.ventaRealizada(venta));
  }

  @Test
  void unSistemaInaccesibleSeInformaComoFallaDeIntegracion() {
    servidor.stop(0);
    Deposito deposito = new Deposito(url("/api/entregas"), new ClienteHttpJdk());

    assertThrows(IntegracionException.class, () -> deposito.ventaRealizada(venta));
  }

  private URI url(String path) {
    return URI.create("http://127.0.0.1:" + servidor.getAddress().getPort() + path);
  }
}
