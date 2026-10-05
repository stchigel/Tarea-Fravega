package ar.com.fravega.venta;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/** Reloj de test que se puede adelantar manualmente. */
class RelojAjustable extends Clock {

  private Instant ahora;

  RelojAjustable(Instant inicio) {
    this.ahora = inicio;
  }

  void avanzar(Duration duracion) {
    ahora = ahora.plus(duracion);
  }

  @Override
  public ZoneId getZone() {
    return ZoneOffset.UTC;
  }

  @Override
  public Clock withZone(ZoneId zone) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Instant instant() {
    return ahora;
  }
}
