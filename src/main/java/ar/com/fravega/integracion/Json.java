package ar.com.fravega.integracion;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

/** Serializador JSON mínimo para objetos planos (strings y números). */
final class Json {

  private Json() {
  }

  static String objeto(Map<String, ?> campos) {
    return campos.entrySet().stream()
        .map(campo -> texto(campo.getKey()) + ":" + valor(campo.getValue()))
        .collect(Collectors.joining(",", "{", "}"));
  }

  private static String valor(Object valor) {
    if (valor == null) {
      return "null";
    }
    if (valor instanceof BigDecimal decimal) {
      return decimal.toPlainString();
    }
    if (valor instanceof Number numero) {
      return numero.toString();
    }
    return texto(valor.toString());
  }

  private static String texto(String s) {
    StringBuilder sb = new StringBuilder("\"");
    for (char c : s.toCharArray()) {
      switch (c) {
        case '"' -> sb.append("\\\"");
        case '\\' -> sb.append("\\\\");
        case '\n' -> sb.append("\\n");
        case '\r' -> sb.append("\\r");
        case '\t' -> sb.append("\\t");
        default -> {
          if (c < 0x20) {
            sb.append(String.format("\\u%04x", (int) c));
          } else {
            sb.append(c);
          }
        }
      }
    }
    return sb.append('"').toString();
  }
}
