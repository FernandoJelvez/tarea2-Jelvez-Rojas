package io.github.FernandoJelvez.tarea2_jelvez_rojas.util;

import io.github.FernandoJelvez.tarea2_jelvez_rojas.model.Compra;

import java.io.*;
import java.util.List;

public class CsvParser {
    public Compra parse(Long id) throws FileNotFoundException {
        File f = new File("compras.csv");
        BufferedReader fr = new BufferedReader(new FileReader(f));
        List<String> lines = fr.lines().toList();
        String[] data = null;
        for (int i = 1; i < lines.size(); i++) {
            String[] line = lines.get(i).split(",");
            if (Integer.parseInt(line[0]) == id) {
                data = line;
                break;
            }
        }
        if (data!=null) {
            String[] splitFecha = data[4].split("-");
            data[4] = splitFecha[2]+"-"+splitFecha[1]+"-"+splitFecha[0];
            return new Compra(Long.parseLong(data[0]),data[1],Double.parseDouble(data[2]),data[3],data[4]);
        }
        return null;
    }
}
