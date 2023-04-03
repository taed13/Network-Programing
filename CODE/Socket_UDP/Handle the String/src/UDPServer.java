import java.net.*;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        // tạo một socket dữ liệu với số hiệu cổng là 9876
        DatagramSocket serverSocket = new DatagramSocket(9876);

        // tạo hai mảng byte để lưu dữ liệu nhận và gửi
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        while (true) {
            // tạo một gói tin dữ liệu để nhận từ client
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            // chuyển doi du lieu nhan duoc thanh chuoi va hien thi no tren console
            String sentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Received: " + sentence);

            // lay dia chi ip va so hieu cong cua client
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            // xu ly du lieu nhan duoc de tao ra chuoi hoa, chuoi thuong va chuoi vua hoa
            // vua thuong
            String capitalizedSentence = sentence.toUpperCase();
            String lowercaseSentence = sentence.toLowerCase();
            StringBuilder sb = new StringBuilder(); // tao mot stringbuilder moi de xay dung chuoi moi

            for (int i = 0; i < sentence.length(); i++) { // duyet tung ky tu trong chuoi
                char c = sentence.charAt(i);
                if (Character.isUpperCase(c)) { // kiem tra xem ky tu co phai la HOA hay khong
                    sb.append(Character.toLowerCase(c)); // neu la HOA thi chuyen sang thuong va them vao chuoi moi
                } else {
                    sb.append(Character.toUpperCase(c)); // neu la thuong thi chuyen sang HOA va them vao chuoi moi
                }
            }
            String mixedCaseSentence = sb.toString();

            // tao chuoi phan hoi gui lai cho client
            String responseString = "UPPER CASE: " + capitalizedSentence + "\nLOWER CASE: " + lowercaseSentence
                    + "\nMIXED CASE: " + mixedCaseSentence;
            sendData = responseString.getBytes();

            // tao goi tin du lieu de gui lai cho client
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }
}