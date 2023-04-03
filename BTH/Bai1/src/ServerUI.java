import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;

public class ServerUI extends JFrame implements ActionListener {
    public static void main(String[] args) throws Exception {
        new ServerUI();
    }

    private JLabel portLabel;
    private JTextField portField;
    private JTextArea receiverArea;
    private JButton startButton;

    ServerSocket server;
    Socket socket;
    int port;
    DataInputStream din;
    DataOutputStream dos;
    Scanner kb = new Scanner(System.in);

    public ServerUI() {
        setTitle("Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tạo thành phần trong giao diện
        portLabel = new JLabel("Port: ");
        portField = new JTextField();
        startButton = new JButton("Start");
        startButton.addActionListener(this);

        receiverArea = new JTextArea();
        receiverArea.setEditable(false);

        // Thêm thành phần vào giao diện
        JPanel portPanel = new JPanel(new BorderLayout());
        portPanel.add(portLabel, BorderLayout.WEST);
        portPanel.add(portField, BorderLayout.CENTER);
        portPanel.add(startButton, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(receiverArea);

        add(portPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Hiển thị giao diện
        setVisible(true);
    }

    public static String reverseString(String str) {
        StringBuffer stringBuffer = new StringBuffer(str);
        return stringBuffer.reverse().toString();
    }

    public static int countWord(String str) {
        int count = 0;
        int size = str.length();
        boolean notCounted = true;
        for (int i = 0; i < size; i++) {
            if (str.charAt(i) != ' ' && str.charAt(i) != '\t'
                    && str.charAt(i) != '\n') {
                if (notCounted) {
                    count++;
                    notCounted = false;
                }
            } else {
                notCounted = true;
            }
        }
        return count;
    }

    public static int countNgyuyenAm(String str, char c) {
        int count = 0;
        int size = str.length();
        for (int i = 0; i < size; i++) {
            if (str.charAt(i) == c || str.charAt(i) == Character.toUpperCase(c)) {
                count++;
            }
        }
        return count;
    }

    public static String toUpperAndLower(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] >= 97 && charArray[i] <= 122) {
                charArray[i] -= 32;
            } else if (charArray[i] >= 65 && charArray[i] <= 90) {
                charArray[i] += 32;
            }
        }
        str = String.valueOf(charArray);
        return str;
    }

    private class Receiver implements Runnable {
        public void run() {
            try {
                while (true) {
                    String st = din.readUTF();
                    receiverArea.append("Chuoi nhan tu Client: " + st + "\n");
                    dos.writeUTF("Chuoi in hoa: " + st.toUpperCase());
                    dos.flush();
                    dos.writeUTF("Chuoi thuong: " + st.toLowerCase());
                    dos.flush();
                    dos.writeUTF("Chuoi dao nguoc: " + reverseString(st));
                    dos.flush();
                    dos.writeUTF("Chuoi vua hoa vua thuong: " + toUpperAndLower(st));
                    dos.flush();
                    int count = countWord(st);
                    dos.writeUTF("So tu trong chuoi: " + Integer.toString(count));
                    dos.flush();
                    int countA = countNgyuyenAm(st, 'a');
                    dos.writeUTF("So lan xuat hien ky tu 'a' trong chuoi: " + Integer.toString(countA));
                    dos.flush();
                    int countI = countNgyuyenAm(st, 'i');
                    dos.writeUTF("So lan xuat hien ky tu 'i' trong chuoi: " + Integer.toString(countI));
                    dos.flush();
                    int countU = countNgyuyenAm(st, 'u');
                    dos.writeUTF("So lan xuat hien ky tu 'u' trong chuoi: " + Integer.toString(countU));
                    dos.flush();
                    int countE = countNgyuyenAm(st, 'e');
                    dos.writeUTF("So lan xuat hien ky tu 'e' trong chuoi: " + Integer.toString(countE));
                    dos.flush();
                    int countO = countNgyuyenAm(st, 'o');
                    dos.writeUTF("So lan xuat hien ky tu 'o' trong chuoi: " + Integer.toString(countO));
                    dos.flush();
                    kb = kb.reset();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            try {
                port = Integer.parseInt(portField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid port number");
                return;
            }
            try {
                server = new ServerSocket(port);
                System.out.println("Server is started");
                JOptionPane.showMessageDialog(this, "Server (Port:" + port + ") is started");
                socket = server.accept();
                dos = new DataOutputStream(socket.getOutputStream());
                din = new DataInputStream(socket.getInputStream());
                // Bắt đầu luồng nhận tin nhắn
                Thread t = new Thread(new Receiver());
                t.start();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not connect to server");
                return;
            }
        }
    }
}
