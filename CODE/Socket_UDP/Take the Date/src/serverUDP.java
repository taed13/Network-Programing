import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class serverUDP {
    public static void main(String[] args) throws Exception {
        // Gán cổng 9876 cho chương trình
        DatagramSocket serverDatagramSocket = new DatagramSocket(9876);
        // Tạo các mảng byte để chứa dữ liệu gửi và nhận
        System.out.println("Server is started");
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while (true) {
            // tao goi rong de nhan du lieu tu client
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            // nhan du lieu tu client
            serverDatagramSocket.receive(receivePacket);
            // lay dia chi ip cua client
            InetAddress ipAddress = receivePacket.getAddress();
            // lay port cua chuong trinh client
            int port = receivePacket.getPort();
            // lay ngay gio de gui nguoc lai client
            String requestString = new String(receivePacket.getData());
            System.out.println(requestString);

            if (requestString.trim().equals("getDate")) {
                sendData = new Date().toString().getBytes();
            } else {
                sendData = "Server not know what you want?".getBytes();
            }
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
            // gui du lieu lai cho client
            serverDatagramSocket.send(sendPacket);
        }

    }
}
