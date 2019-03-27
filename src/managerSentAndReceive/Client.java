/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerSentAndReceive;

import controlleurs.AccueilController;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import solution.TCPDataClient;

/**
 *
 * @author Noumodong
 */
public class Client extends Thread {

    String adresseIp;
    int portDeConnexion;
    File chemin = null;
    public static Socket client_socket;

    public Client(String adresseIp, int portDeConnexion, File chemin) {
        this.adresseIp = adresseIp;
        this.portDeConnexion = portDeConnexion;
        this.chemin = chemin;
    }

    @Override
    public void run() {
        try {
            TCPDataClient obj = new TCPDataClient();
            client_socket = new Socket(InetAddress.getByName(this.adresseIp), this.portDeConnexion);
            DataInputStream din = new DataInputStream(client_socket.getInputStream());
            DataOutputStream dout = new DataOutputStream(client_socket.getOutputStream());

            if (this.chemin != null) {
                dout.write(obj.CreateDataPacket("124".getBytes("UTF8"), this.chemin.getName().getBytes("UTF8")));
                dout.flush();
                RandomAccessFile rw = new RandomAccessFile(this.chemin, "r");
                long current_file_pointer = 0;
                boolean loop_break = false;
                while (true) {
                    if (din.read() == 2) {
                        byte[] cmd_buff = new byte[3];
                        din.read(cmd_buff, 0, cmd_buff.length);
                        byte[] recv_buff = obj.ReadStream(din);
                        switch (Integer.parseInt(new String(cmd_buff))) {
                            case 125:
                                current_file_pointer = Long.valueOf(new String(recv_buff));
                                int buff_len = (int) (rw.length() - current_file_pointer < 20000 ? rw.length() - current_file_pointer : 20000);
                                byte[] temp_buff = new byte[buff_len];
                                if (current_file_pointer != rw.length()) {
                                    rw.seek(current_file_pointer);
                                    rw.read(temp_buff, 0, temp_buff.length);
                                    dout.write(obj.CreateDataPacket("126".getBytes("UTF8"), temp_buff));
                                    dout.flush();
                                    System.out.println("Upload percentage: " + ((float) current_file_pointer / rw.length()) * 100 + "%");
                                    AccueilController.progressEnvoyerr.setProgress(((float) current_file_pointer / rw.length()));
                               
                                } else {
                                    loop_break = true;
                                }
                                break;
                        }
                    }
                    if (loop_break == true) {
                        System.out.println("Stop Server informed");
                        dout.write(obj.CreateDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
                        dout.flush();
                        client_socket.close();
                        System.out.println("Client Socket Closed");
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
             AccueilController.progressEnvoyerr.setProgress(1);
                               
        }

    }

    private byte[] CreateDataPacket(byte[] cmd, byte[] data) {
        byte[] packet = null;
        try {
            byte[] initialize = new byte[1];
            initialize[0] = 2;
            byte[] separator = new byte[1];
            separator[0] = 4;
            byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
            packet = new byte[initialize.length + cmd.length + separator.length + data_length.length + data.length];

            System.arraycopy(initialize, 0, packet, 0, initialize.length);
            System.arraycopy(cmd, 0, packet, initialize.length, cmd.length);
            System.arraycopy(data_length, 0, packet, initialize.length + cmd.length, data_length.length);
            System.arraycopy(separator, 0, packet, initialize.length + cmd.length + data_length.length, separator.length);
            System.arraycopy(data, 0, packet, initialize.length + cmd.length + data_length.length + separator.length, data.length);

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return packet;
    }

    private byte[] ReadStream(DataInputStream din) {
        byte[] data_buff = null;
        try {
            int b = 0;
            String buff_length = "";
            while ((b = din.read()) != 4) {
                buff_length += (char) b;
            }
            int data_length = Integer.parseInt(buff_length);
            data_buff = new byte[Integer.parseInt(buff_length)];
            int byte_read = 0;
            int byte_offset = 0;
            while (byte_offset < data_length) {
                byte_read = din.read(data_buff, byte_offset, data_length - byte_offset);
                byte_offset += byte_read;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return data_buff;
    }

}
