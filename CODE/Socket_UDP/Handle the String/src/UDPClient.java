import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) throws Exception {
        // tao mot doi tuong BufferedReader de doc du lieu tu ban phim
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // tao mot socket du lieu
        DatagramSocket clientSocket = new DatagramSocket();

        // lay dia chi ip cua server
        InetAddress IPAddress = InetAddress.getByName("localhost");

        // tao hai mang byte de luu du lieu nhan va gui
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        // hien thi thong bao va yeu cau nguoi dung nhap chuoi tu ban phim
        System.out.println("Enter a string: ");
        String sentence = inFromUser.readLine();

        // chuyen doi du lieu thanh mang byte va gui du lieu den server
        sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);

        // tao goi tin du lieu de nhan phan hoi tu server
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        // chuyen doi du lieu nhan duoc thanh chuoi va hien thi no tren console
        String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("FROM SERVER:\n" + modifiedSentence);

        // dong socket
        clientSocket.close();
    }
}
