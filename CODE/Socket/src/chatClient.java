import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class chatClient {
   public static void main(String[] args) throws UnknownHostException, IOException {
      Socket socket = new Socket("localhost", 7000);
      DataInputStream dis = new DataInputStream(socket.getInputStream());
      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

      // nhap chuoi gui den Server
      Scanner kb = new Scanner(System.in);
      while (true) {
         System.out.print("Client: ");
         String msg = kb.nextLine(); // nhap chuoi tu ban phim gui len server
         dos.writeUTF("Client: " + msg);
         dos.flush();

         // client nhan du lieu tu server va gui lai
         String st = dis.readUTF();
         System.out.println(st);
         kb = kb.reset();
      }
   }
}
