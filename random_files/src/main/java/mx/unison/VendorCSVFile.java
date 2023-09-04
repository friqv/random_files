package mx.unison;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VendorCSVFile {

    private List<Vendor> listaDeVendedores = new ArrayList<>();
    private String fileName;

    public VendorCSVFile(String fileName) {
        this.fileName = fileName;
        cargarVendedoresDesdeCSV();

            }

    public List<Vendor> getListaDeVendedores() {
        return listaDeVendedores;
    }

    private void cargarVendedoresDesdeCSV() {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String registro;
            boolean primeraLinea = true;
            while ((registro = in.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                Vendor vendedor = parseRecord(registro);
                if (vendedor != null) {
                    listaDeVendedores.add(vendedor);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de los vendedores.");
        }
    }


    public void write(Vendor v) { //WRITE EN FORMATO UTF-8 PARA PODER MANEJAR EL ARCHIVO CSV
        try { //OBTENER POR CADENAS DE TEXTO/ GUARDAR Y LEER DATROS ESTRUCTURADOS EN EL CSV
            PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"), true);
            SimpleDateFormat FormatoFecha = new SimpleDateFormat("MM/dd/yy"); //FORMATO DE FECHA
            String FD = FormatoFecha.format(v.getFecha());
            out.println(v.getCodigo() + "," + v.getNombre() + "," + FD + "," + v.getZona() + "," + v.getVentasMensuales());
            out.close();
            JOptionPane.showMessageDialog(null, "REGISTRADO, VUELVA PRONTO.");
        } catch (IOException ex) {
            Logger.getLogger(VendorCSVFile.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "NO JALO, ALGO FALLÓ");
        }
    }

    public void AgregarNuevoVendedor() {
        // VENDEDOR NUEVO INFORMACIÓN
        String codigoo = JOptionPane.showInputDialog("Codigo del vendedor:");
        int codigo = Integer.parseInt(codigoo);

        String nombre = JOptionPane.showInputDialog("Nombre:");
        String fechaa = JOptionPane.showInputDialog("Fecha (MM/dd/yyyy):");
        String zona = JOptionPane.showInputDialog("Zona:");


        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date fecha;
        try {
            fecha = dateFormat.parse(fechaa);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Debe ser de la forma MM/dd/yyyy.");
            return;
        }

        // VENTAS MENSUALES
        String ventasMensualesStr = JOptionPane.showInputDialog("Ventas mensuales:");
        double ventasMensuales = Double.parseDouble(ventasMensualesStr);

        // Crear el nuevo vendedor con ventas mensuales
        Vendor nuevoVendedor = new Vendor(codigo, nombre, fecha, zona);
        nuevoVendedor.setVentasMensuales(ventasMensuales);

        // ESCRIBIR AL NUEVO VENDEDOR
        write(nuevoVendedor);
    }

    public void eliminarVendedorPorCodigo() {
        // LEER EL CSV EN LISTA (cada lista representa una fila)
        List<List<String>> registros = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] campos = line.split(",");
                List<String> registro = new ArrayList<>();
                for (String campo : campos) {
                    registro.add(campo.trim());
                }
                registros.add(registro);
            }
        } catch (IOException e) {
            return;
        }

        // LISTA DE VENDEDORES
        String[] options = new String[registros.size() - 1];
        for (int i = 1; i < registros.size(); i++) {
            List<String> registro = registros.get(i);
            options[i - 1] = registro.get(1); //NOMBRE DEL VENDEDOR
        }
        //ESTE STRING ES PARA QUE NO NOS PIDA LA INFORMACIÓN DIRECTAMENTE, ES PARA PODER VER LAS OPCIONES EN LISTA
        String seleccion = (String) JOptionPane.showInputDialog(null, "Que vendedor quieres eliminar:", "Eliminar Vendedorrr", JOptionPane.DEFAULT_OPTION, null, options, options[0]);

        //POSICIONES
        int Eliminar = -1;
        for (int i = 1; i < registros.size(); i++) {
            List<String> registro = registros.get(i);
            if (registro.get(1).equals(seleccion)) {
                Eliminar = i;
                break;
            }
        }
        //IF
        if (Eliminar == -1) {
            JOptionPane.showMessageDialog(null, "Vendedor no encontrado.");
            return;
        }

        // Eliminaar
        registros.remove(Eliminar);

        // REESCRIBIR LA INFORMACIÓN EN EL ARCHIVO
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (List<String> registro : registros) {
                String linea = String.join(",", registro);
                writer.println(linea);
            }
            JOptionPane.showMessageDialog(null, "Se elimino el vendedor. GRACIAS");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "ERROR PAPI.");
        }
    }
    //BUSCAR
    public Vendor find(int codigo) {
        String buscar = String.valueOf(codigo);
        String record = null;
        Vendor x = null;
        try {       //AGREGAR UTF PARA EL CS
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            while ((record = in.readLine()) != null) {
                if (record.startsWith(buscar)) {
                    x = parseRecord(record);
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(VendorCSVFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return x;
    }

    private Date parseDOB(String d) throws ParseException {
        int len = d.length();
        Date date = null;
        if (len == 8) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
            date = dateFormat.parse(d);
        }
        if (len == 10) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            date = dateFormat.parse(d);
        }
        return date;
    }

    private Vendor parseRecord(String record) {
        StringTokenizer st = new StringTokenizer(record, ",");
        Vendor v = new Vendor();
        v.setCodigo(Integer.parseInt(st.nextToken()));
        v.setNombre(st.nextToken());
        String fecha = st.nextToken();
        Date d = null;
        try {
            d = parseDOB(fecha);
        } catch (ParseException e) {
            System.out.printf(e.getMessage());
        }
        v.setFecha(d);
        v.setZona(st.nextToken());

        // VENTAS MENSUALES
        if (st.hasMoreTokens()) {
            v.setVentasMensuales(Double.parseDouble(st.nextToken()));
        }

        return v;
    }

    public void guardarCambios() {
        try {
            String tempFileName = fileName + ".temp"; // Nombre del archivo temporal
            FileWriter csvWriter = new FileWriter(tempFileName);
            csvWriter.append("Codigo,Nombre,Fecha,Zona\n");

            // Itera a través de la lista de vendedores y escribe cada vendedor en el archivo CSV temporal
            SimpleDateFormat nuevoDateFormat = new SimpleDateFormat("MM/dd/yy");
            for (Vendor vendedor : listaDeVendedores) {
                csvWriter.append(String.valueOf(vendedor.getCodigo()));
                csvWriter.append(",");
                csvWriter.append(vendedor.getNombre());
                csvWriter.append(",");
                csvWriter.append(nuevoDateFormat.format(vendedor.getFecha()));
                csvWriter.append(",");
                csvWriter.append(vendedor.getZona());
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

            // Elimina el archivo CSV original y cambia el nombre del archivo temporal al original
            File originalFile = new File(fileName);
            File tempFile = new File(tempFileName);
            if (originalFile.exists()) {
                originalFile.delete();
            }
            tempFile.renameTo(originalFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Vendor> consultarVendedoresPorZona(String zona) {
        List<Vendor> vendedoresEnZona = new ArrayList<>();
        for (Vendor vendedor : listaDeVendedores) {
            if (vendedor.getZona().trim().equalsIgnoreCase(zona.trim())) {
                vendedoresEnZona.add(vendedor);
            }
        }
        return vendedoresEnZona;
    }

}
