import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPTimeClient {
   /**
    * @param args
    * @throws UnknownHostException
    * @throws IOException
    */
   public static void main(String[] args) throws UnknownHostException, IOException {
      Socket socket = new Socket("localhost", 7788);
      DataInputStream dis = new DataInputStream(socket.getInputStream());
      String time = dis.readUTF();
      System.out.println(time);
      socket.close();
   }
}
