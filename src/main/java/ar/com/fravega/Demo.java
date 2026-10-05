package ar.com.fravega;

import ar.com.fravega.integracion.ClienteHttp;
import ar.com.fravega.integracion.Deposito;
import ar.com.fravega.integracion.OficinaContable;
import ar.com.fravega.producto.Producto;
import ar.com.fravega.producto.RegimenImpositivo;
import ar.com.fravega.venta.ContadorDeVentas;
import ar.com.fravega.venta.Sucursal;
import java.math.BigDecimal;
import java.net.URI;

/** Ejemplo de uso. Los sistemas externos se simulan imprimiendo lo que se les enviaría. */
public class Demo {

  public static void main(String[] args) {
    ClienteHttp clienteSimulado = (destino, json) -> System.out.println("  POST " + destino + " " + json);

    RegimenImpositivo regimen = RegimenImpositivo.vigente();
    Producto televisor = new Producto("TV-55", "Smart TV 55", new BigDecimal("1000"), regimen.electronico());
    Producto sillon = new Producto("SI-03", "Sillón 3 cuerpos", new BigDecimal("1000"), regimen.hogar());

    ContadorDeVentas contador = new ContadorDeVentas();
    Sucursal sucursal = new Sucursal("Caballito");
    sucursal.suscribir(new OficinaContable(URI.create("http://contable.local/api/ventas"), clienteSimulado));
    sucursal.suscribir(new Deposito(URI.create("http://deposito.local/api/entregas"), clienteSimulado));
    sucursal.suscribir(contador);

    System.out.println("Precio final TV: " + televisor.precioFinal());
    System.out.println("Precio final sillón: " + sillon.precioFinal());

    System.out.println("Vendiendo TV...");
    sucursal.vender(televisor);
    System.out.println("Vendiendo sillón...");
    sucursal.vender(sillon);
    System.out.println("Ventas del día: " + contador.cantidadDelDia());

    regimen.iva().actualizarAlicuota(new BigDecimal("0.27"));
    System.out.println("Precio final TV con IVA al 27%: " + televisor.precioFinal());
  }
}
