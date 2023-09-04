package mx.unison;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VerDatos {
    private JFrame frame;
    private JTable table;

    public VerDatos() {

        //JFARME PARA VISUALIZAR LOS DATOS DEL .CSV
        frame = new JFrame("DATOSSS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Crear una tabla vacía
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        ajustarTamanoVentana();
    }

    private void ajustarTamanoVentana() {
        frame.pack(); // Ajustar automáticamente el tamaño de la ventana al contenido
        frame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        frame.setVisible(true);
    }

    public void mostrarDatosDesdeCSV(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String[]> data = new ArrayList<>();
            String line;

            // Leer y procesar líneas del archivo CSV
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Suponiendo que el CSV está delimitado por comas
                data.add(parts);
            }

            // Crear un modelo de tabla
            DefaultTableModel tableModel = new DefaultTableModel();

            // Agregar columnas al modelo de tabla
            for (int i = 0; i < data.get(0).length; i++) {
                tableModel.addColumn(data.get(0)[i]);
            }

            // Agregar filas al modelo de tabla (excluyendo la primera fila, que son los encabezados)
            for (int i = 1; i < data.size(); i++) {
                tableModel.addRow(data.get(i));
            }

            // Asignar el modelo de tabla a la tabla existente
            table.setModel(tableModel);

            // Llamar a esta función para ajustar el tamaño de la ventana después de agregar la tabla
            ajustarTamanoVentana();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al leer el archivo CSV.");
        }
    }


    }

