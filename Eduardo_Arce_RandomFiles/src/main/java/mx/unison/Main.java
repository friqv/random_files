package mx.unison;

import javax.swing.JOptionPane;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main { // EJECUTAR EL PROGRAMA
    public static void main(String[] args) throws ParseException {
        final String fileName = "C:\\Users\\Rafael Arce Gaxiola\\Desktop\\DSIII\\random_files\\src\\main\\java\\vendors.csv";
        // final String fileName = "D:\\data\\vendors-data.csv";

        VendorCSVFile csvFile = new VendorCSVFile(fileName);
        Vendor vendedorExistente = null;
        boolean CerrarMenu = false; // Variable para controlar si se debe cerrar la ventana de opciones

        while (true) {


            if (CerrarMenu) {
                CerrarMenu = false; // Reiniciar la variable
                break; // Salir del bucle para cerrar la ventana de opciones
            }

            //MENU DE OPCIONES

            String[] opciones = {"Buscar vendedor ", " Nuevo vendedor", "Modificar vendedor", "Visualizar datos CSV", "Consultar por zona","Eliminar vendedor", "EXIT"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "ES MOMENTO DE SELECCIONAR UNA OPCION:",
                    "MENU",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            switch (choice) {
                case 0:

                    String codigoBuscar = JOptionPane.showInputDialog("Dame el codigo del vendedor");
                    int codigoBuscarEmpleado = Integer.parseInt(codigoBuscar);
                    Vendor encontrado = csvFile.find(codigoBuscarEmpleado);
                    if (encontrado != null) {
                        JOptionPane.showMessageDialog(null, encontrado);
                    } else {
                        JOptionPane.showMessageDialog(null, "No encontre al vendedor.");
                    }
                    break;
                case 1: // AGREGAR UN NUEVO VENDEDOR
                    csvFile.AgregarNuevoVendedor();
                    break;
                case 2:
                    String codigoModificarStr = JOptionPane.showInputDialog("Ingresa el codigo del vendedor que deseas modificar:");
                    int codigoModificar = Integer.parseInt(codigoModificarStr);
                    vendedorExistente = csvFile.find(codigoModificar);

                    if (vendedorExistente != null) {
                        SimpleDateFormat nuevoDateFormat = new SimpleDateFormat("MM/dd/yy");

                        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre (anterior: " + vendedorExistente.getNombre() + "):");
                        String nuevaFechaStr = JOptionPane.showInputDialog("Nueva fecha (MM/dd/yy) (anterior: " + nuevoDateFormat.format(vendedorExistente.getFecha()) + "):");
                        Date nuevaFecha = nuevoDateFormat.parse(nuevaFechaStr);
                        String nuevaZona = JOptionPane.showInputDialog("Nueva zona (anterior: " + vendedorExistente.getZona() + "):");

                        vendedorExistente.setNombre(nuevoNombre);
                        vendedorExistente.setFecha(nuevaFecha);
                        vendedorExistente.setZona(nuevaZona);

                        csvFile.guardarCambios(); // método para guardar los cambios en el archivo CSV

                        JOptionPane.showMessageDialog(null, "Datos del vendedor modificados correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontro al vendedor con el codigo proporcionado.");
                    }
                    break;
                case 3:
                    // INSTANCIA
                    VerDatos verDatos = new VerDatos();
                    // Llama al método mostrarDatosDesdeCSV después de crear la instancia
                    verDatos.mostrarDatosDesdeCSV(fileName);

                    // Configura la variable para cerrar la ventana de opciones
                    CerrarMenu = true;

                    break;
                case 4:

                    String zonaConsulta = JOptionPane.showInputDialog("Ingresa la zona a consultar:");
                    List<Vendor> vendedoresEnZona = csvFile.consultarVendedoresPorZona(zonaConsulta);
                    System.out.println("Zona ingresada: " + zonaConsulta);

                    if (!vendedoresEnZona.isEmpty()) {
                        System.out.println("Vendedores encontrados en la zona " + zonaConsulta + ":");
                        for (Vendor vendedor : vendedoresEnZona) {
                            System.out.println(vendedor.toString());
                        }
                        JOptionPane.showMessageDialog(null, "Vendedores en la zona " + zonaConsulta + " encontrados.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontraron vendedores en la zona " + zonaConsulta + ".");
                    }
                    break;
                case 5:
                    csvFile.eliminarVendedorPorCodigo();
                    break;
                case 6:
                    System.exit(0);
                default:
                    JOptionPane.showMessageDialog(null, "Opcion no valida.");

                    System.out.println("Contenido de la lista antes de guardar cambios:");
                    for (Vendor vendedor : csvFile.getListaDeVendedores()) {
                        System.out.println(vendedor);
                    }

                    csvFile.guardarCambios(); // Método para guardar los cambios en el archivo CSV

                    System.out.println("Contenido de la lista después de guardar cambios:");
                    for (Vendor vendedor : csvFile.getListaDeVendedores()) {
                        System.out.println(vendedor);
                    }
            }
        }
    }
}
