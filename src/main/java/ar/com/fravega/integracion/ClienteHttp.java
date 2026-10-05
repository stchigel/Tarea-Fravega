package ar.com.fravega.integracion;

import java.net.URI;

/** Abstracción mínima del transporte HTTP, para poder reemplazarlo en tests. */
@FunctionalInterface
public interface ClienteHttp {

  /** Envía un POST con cuerpo JSON. Lanza {@link IntegracionException} si la respuesta no es 2xx. */
  void postJson(URI destino, String cuerpoJson);
}
