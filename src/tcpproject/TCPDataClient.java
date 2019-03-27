/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpproject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author Kamen
 */
public class TCPDataClient {

    public static void main(String[] args) {
        try {
            TCPDataClient obj = new TCPDataClient();
            Socket obj_client = new Socket(InetAddress.getByName("localhost"), 1027);
            DataInputStream din = new DataInputStream(obj_client.getInputStream());
            DataOutputStream dout = new DataOutputStream(obj_client.getOutputStream());
            /*byte[] buffer = obj.CreateDataPacket("je suis malade".getBytes("UTF8"));
            dout.write(buffer);*/
            JFileChooser jfc = new JFileChooser();
            int dialog_value = jfc.showOpenDialog(null);
            if (dialog_value == JFileChooser.APPROVE_OPTION) {
                File target_file = jfc.getSelectedFile();
                dout.write(obj.CreateDataPacket("124".getBytes(), target_file.getName().getBytes()));
                dout.flush();   
                RandomAccessFile rw = new RandomAccessFile(target_file, "rw");
                long current_file_pointer = 0;
                boolean loop_break = false;
                while (true) {
                    if (din.read() == 2) {
                        byte[] cmd_buff = new byte[3];
                        din.read(cmd_buff, 0, cmd_buff.length);
                        byte[] recv_buff = obj.ReadStream(din);
                        switch (Integer.parseInt(new String(recv_buff))) {
                            case 125:
                                current_file_pointer = Long.valueOf(new String(recv_buff));
                                int buff_len = (int) (rw.length() - current_file_pointer < 2003 ? rw.length() - current_file_pointer : 2030);
                                byte[] temp_buff = new byte[buff_len];
                                if (current_file_pointer != rw.length()) {
                                    rw.seek(current_file_pointer);
                                    rw.read(temp_buff, 0, temp_buff.length);
                                    dout.write(obj.CreateDataPacket("126".getBytes("UTF8"), temp_buff));
                                    dout.flush();
                                    System.out.println("Upload percentage "+ ((float)current_file_pointer/rw.length())*100+"%");
                                } else {
                                    loop_break = true;
                                }
                                break;
                        }
                    }
                    if (loop_break == true) {
                        System.out.println("Stop server informed");
                        dout.write(obj.CreateDataPacket("127".getBytes("UTF8"), "Close".getBytes("UTF8")));
                        dout.flush();
                        obj_client.close();
                        System.out.println("Client Socket Closed");
                        break;
                    }
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(TCPDataClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TCPDataClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private byte[] CreateDataPacket(byte[] cmd, byte[] data) {
        byte[] packet = null;
        try {
            byte[] initialize = new byte[1];
            initialize[0] = 2;
            byte[] seperator = new byte[1];
            seperator[0] = 4;
            byte[] data_length = String.valueOf(data.length).getBytes("UTF8");
            packet = new byte[initialize.length + cmd.length + seperator.length + data.length + data.length];

            System.arraycopy(initialize, 0, packet, 0, initialize.length);
            System.arraycopy(cmd, 0, packet, initialize.length, cmd.length);
            System.arraycopy(data_length, 0, packet, initialize.length + cmd.length, data_length.length);
            System.arraycopy(seperator, 0, packet, initialize.length + data_length.length + cmd.length, seperator.length);
            System.arraycopy(data, 0, packet, initialize.length + data_length.length + seperator.length + cmd.length, data.length);
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
            Logger.getLogger(TCPDataClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data_buff;
    }

}
