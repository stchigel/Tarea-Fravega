package ar.com.fravega.integracion;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/** Implementación de {@link ClienteHttp} sobre el cliente HTTP del JDK. */
public class ClienteHttpJdk implements ClienteHttp {

  private static final Duration TIMEOUT = Duration.ofSeconds(5);

  private final HttpClient cliente;

  public ClienteHttpJdk() {
    this(HttpClient.newBuilder().connectTimeout(TIMEOUT).build());
  }

  public ClienteHttpJdk(HttpClient cliente) {
    this.cliente = cliente;
  }

  @Override
  public void postJson(URI destino, String cuerpoJson) {
    HttpRequest request = HttpRequest.newBuilder(destino)
        .timeout(TIMEOUT)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(cuerpoJson))
        .build();
    try {
      HttpResponse<String> respuesta = cliente.send(request, HttpResponse.BodyHandlers.ofString());
      if (respuesta.statusCode() / 100 != 2) {
        throw new IntegracionException(
            "POST " + destino + " respondió " + respuesta.statusCode() + ": " + respuesta.body());
      }
    } catch (IOException e) {
      throw new IntegracionException("Error de comunicación con " + destino, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IntegracionException("Comunicación interrumpida con " + destino, e);
    }
  }
}
