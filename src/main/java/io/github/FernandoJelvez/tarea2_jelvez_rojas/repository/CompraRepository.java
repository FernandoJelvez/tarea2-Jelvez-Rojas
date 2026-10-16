package io.github.FernandoJelvez.tarea2_jelvez_rojas.repository;

import io.github.FernandoJelvez.tarea2_jelvez_rojas.exception.ArchivoComprasException;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.model.Compra;
import io.github.FernandoJelvez.tarea2_jelvez_rojas.model.Unidad;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Lee las compras desde el CSV indicado en CSV_PATH.
 * <p>
 * El archivo se lee en cada consulta, asi la empresa puede reemplazarlo
 * (volumen montado en Docker) sin reconstruir ni reiniciar la aplicacion.
 * Se respeta el orden de las filas del CSV.
 */
@Repository
public class CompraRepository {

    private static final List<String> COLUMNAS = List.of("id", "producto", "monto", "unidad", "fecha");

    private final Path csvPath;

    public CompraRepository(@Value("${app.csv-path}") String csvPath) {
        this.csvPath = Path.of(csvPath);
    }

    public List<Compra> findAll() {
        if (!Files.isReadable(csvPath)) {
            throw new ArchivoComprasException(
                    "No se puede leer el archivo de compras en " + csvPath.toAbsolutePath()
                            + ". Revise la variable de entorno CSV_PATH y el volumen montado.");
        }
        try {
            return parsear(Files.readAllLines(csvPath, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new ArchivoComprasException("Error leyendo " + csvPath.toAbsolutePath(), e);
        }
    }

    /** Convierte las lineas del CSV (con encabezado) en compras, en el mismo orden. */
    static List<Compra> parsear(List<String> lineas) {
        List<Compra> compras = new ArrayList<>();
        Map<String, Integer> indice = null;

        for (int n = 0; n < lineas.size(); n++) {
            String linea = lineas.get(n).replace("﻿", "").strip(); // quita BOM y espacios / \r
            if (linea.isEmpty()) {
                continue;
            }
            String[] campos = linea.split(",", -1);

            if (indice == null) { // primera linea no vacia = encabezado
                indice = indiceColumnas(campos);
                continue;
            }

            int numeroLinea = n + 1;
            try {
                compras.add(new Compra(
                        Integer.parseInt(campo(campos, indice, "id")),
                        campo(campos, indice, "producto"),
                        Double.parseDouble(campo(campos, indice, "monto")),
                        Unidad.desdeCsv(campo(campos, indice, "unidad")),
                        LocalDate.parse(campo(campos, indice, "fecha"))));
            } catch (NumberFormatException | DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                throw new ArchivoComprasException("Fila " + numeroLinea + " del CSV mal formada: '" + linea + "'", e);
            } catch (IllegalArgumentException e) {
                throw new ArchivoComprasException("Fila " + numeroLinea + " del CSV: " + e.getMessage(), e);
            }
        }
        return compras;
    }

    private static Map<String, Integer> indiceColumnas(String[] encabezado) {
        Map<String, Integer> indice = new HashMap<>();
        for (int i = 0; i < encabezado.length; i++) {
            indice.put(encabezado[i].strip().toLowerCase(Locale.ROOT), i);
        }
        for (String columna : COLUMNAS) {
            if (!indice.containsKey(columna)) {
                throw new ArchivoComprasException("Al CSV le falta la columna '" + columna + "'");
            }
        }
        return indice;
    }

    private static String campo(String[] campos, Map<String, Integer> indice, String columna) {
        return campos[indice.get(columna)].strip();
    }
}

