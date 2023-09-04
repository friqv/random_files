package mx.unison;

import javax.swing.*;
import java.io.*;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RandomVendorFile {
    private String fileName;

    public RandomVendorFile(String fileName) {
        this.fileName = fileName;
    }







    public Vendor read(long position) {
        RandomAccessFile out = null;
        long bytes = 0;
        byte buffer[] = null;
        Vendor vendor = null;
        try {
            out = new RandomAccessFile(fileName, "rws");
            out.seek(position);

            int codigo = out.readInt();

            byte[] nameBytes = new byte[Vendor.MAX_NAME];
            out.read(nameBytes);

            long dateBytes = out.readLong();

            byte[] zonaBytes = new byte[Vendor.MAX_ZONE];
            out.read(zonaBytes);

            vendor = new Vendor(codigo, new String(nameBytes), new Date(dateBytes),
                    new String(zonaBytes));
            out.close();

        } catch (IOException ex) {
            Logger.getLogger(RandomVendorFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return vendor;
    }

    public void read(Vendor vendors[]) {
        RandomAccessFile out = null;
        long bytes = 0;
        byte buffer[] = null;
        Vendor vendor = null;
        try {
            out = new RandomAccessFile(fileName, "rws");
            for (int i = 0; i < vendors.length; i++) {

                int codigo = out.readInt();

                byte[] nameBytes = new byte[Vendor.MAX_NAME];
                out.read(nameBytes);

                long dateBytes = out.readLong();

                byte[] zonaBytes = new byte[Vendor.MAX_ZONE];
                out.read(zonaBytes);

                vendors[i] = new Vendor(codigo, new String(nameBytes), new Date(dateBytes),
                        new String(zonaBytes));
            }
            out.close();

        } catch (IOException ex) {
            Logger.getLogger(RandomVendorFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    public static void main(String[] args) {

        final String dataPath = "C:\\Users\\Rafael Arce Gaxiola\\Desktop\\DSIII\\random_files\\vendors-data.dat";

        RandomVendorFile randomFile = new RandomVendorFile(dataPath);

        Scanner input = new Scanner(System.in);

        System.out.println("Numero de registro:");

        int n = input.nextInt();

        int pos = (n * Vendor.RECORD_LEN) - Vendor.RECORD_LEN;

        long t1 = System.currentTimeMillis();
        Vendor p = randomFile.read(pos);
        long t2 = System.currentTimeMillis();
        System.out.println(p);
        System.out.println(t2 - t1);

    }

}
