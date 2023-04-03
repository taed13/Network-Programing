import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

public class Client {
    private String host; // địa chỉ IP hoặc tên miền của server
    private int port; // cổng của server
    private String clientName; // tên của client
    private BufferedReader reader; // luồng đọc từ server
    private PrintWriter writer; // luồng ghi tới server

    public Client(String host, int port, String clientName) {
        this.host = host;
        this.port = port;
        this.clientName = clientName;
    }

    public void start() {
        try {
            // kết nối tới server
            Socket socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(clientName); // gửi tên của client tới server

            // tạo JFrame để hiển thị giao diện đồ họa
            JFrame frame = new JFrame("Chat Room - Client");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);

            // tạo JTextArea để hiển thị tin nhắn
            JTextArea chatArea = new JTextArea();
            chatArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(chatArea);
            frame.add(scrollPane);

            // tạo JTextField để nhập tin nhắn
            JTextField messageField = new JTextField();
            messageField.addActionListener(e -> {
                String message = messageField.getText();
                writer.println(message);
                messageField.setText("");
            });
            frame.add(messageField, "South");

            frame.setVisible(true);

            // đọc tin nhắn từ server và hiển thị lên JTextArea
            String message;
            while ((message = reader.readLine()) != null) {
                chatArea.append(message + "\n");
            }

            // đóng kết nối
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // hiển thị dialog để yêu cầu người dùng nhập thông tin kết nối
        String host = JOptionPane.showInputDialog("Enter server IP or hostname:");
        int port = Integer.parseInt(JOptionPane.showInputDialog("Enter server port:"));
        String clientName = JOptionPane.showInputDialog("Enter your name:");

        // tạo client và kết nối tới server
        Client client = new Client(host, port, clientName);
        client.start();
    }
}
