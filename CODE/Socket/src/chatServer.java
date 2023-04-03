import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class chatServer {
   /**
    * @param args
    * @throws IOException
    */
   public static void main(String[] args) throws IOException {
      ServerSocket server = new ServerSocket(7000);
      System.out.println("Server is listening on port 7000.");
      Socket socket = server.accept(); // ket noi duoc thiet lap
      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
      DataInputStream dis = new DataInputStream(socket.getInputStream());

      // nhap chuoi de gui den Client
      Scanner sc = new Scanner(System.in);
      while (true) {
         // nhan du lieu tu Client
         String st = dis.readUTF();
         System.out.println("Client: " + st); // chuoi nhan duoc tu client
         System.out.print("Server: ");
         String str = sc.nextLine();
         dos.writeUTF("Server" + str); // gui chuoi den client
         dos.flush();
         sc = sc.reset();
      }
   }
}
