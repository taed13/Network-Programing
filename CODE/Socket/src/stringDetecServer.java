import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class stringDetecServer {
   public static void main(String[] args) throws IOException {
      ServerSocket server = new ServerSocket(7000);
      System.out.println("Server is listening on port 7000.");
      Socket socket = server.accept(); // ket noi duoc thiet lap
      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
      DataInputStream dis = new DataInputStream(socket.getInputStream());

      // nhap chuoi de gui den Client
      while (true) {
         String st = dis.readUTF();
         System.out.println("Client: " + st); // chuoi nhan duoc tu client

         char ch;
         String nstr = "";
         for (int i = 0; i < st.length(); i++) {
            ch = st.charAt(i); // extracts each character
            nstr = ch + nstr; // adds each character in front of the existing string
         }

         System.out.println("Server: ");
         dos.writeUTF("Server: " + nstr); // gui chuoi den client
         dos.flush();
      }
   }
}
