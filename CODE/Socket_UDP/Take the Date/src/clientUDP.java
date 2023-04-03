import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class clientUDP {
    public static void main(String[] args) throws Exception {
        DatagramSocket clientDatagramSocket = new DatagramSocket(); // gan cong
        InetAddress ipAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        sendData = "getDate".getBytes();
        // tao datagram co noi dung yeu cau loai du lieu de gui cho server
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, 9876);
        clientDatagramSocket.send(sendPacket); // gui du lieu cho server
        // tao datagram rong de nhan du lieu
        DatagramPacket receiveDatagramPacket = new DatagramPacket(receiveData, receiveData.length);
        // nhan du lieu tu server
        clientDatagramSocket.receive(receiveDatagramPacket);
        // lay du lieu tu packet nhan duoc
        String string = new String(receiveDatagramPacket.getData());
        System.out.println(string);
        clientDatagramSocket.close();
    }
}
