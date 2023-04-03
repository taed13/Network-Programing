import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

public class Server {
    private List<ClientHandler> clients = new ArrayList<>();
    private JTextArea chatArea;
    private JTextField portField;
    private JButton startButton;
    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        new Server().start();
    }

    public void start() {
        // tạo JFrame để hiển thị giao diện đồ họa
        JFrame frame = new JFrame("Chat Room - Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // tạo JTextArea để hiển thị tin nhắn
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // tạo JTextField để nhập cổng
        portField = new JTextField("12345");
        frame.add(portField, BorderLayout.NORTH);

        // tạo JButton để bắt đầu server
        startButton = new JButton("Start Server");
        startButton.addActionListener(new StartButtonListener());
        frame.add(startButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private class StartButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int port = Integer.parseInt(portField.getText());

            // tạo server socket để lắng nghe các kết nối từ client
            try {
                serverSocket = new ServerSocket(port);
                chatArea.append("Server is listening on port " + port + "\n");

                // chấp nhận các kết nối từ client và tạo client handler cho mỗi client
                while (true) {
                    Socket socket = serverSocket.accept();
                    chatArea.append("New client connected: " + socket.getInetAddress().getHostName() + "\n");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    String clientName = reader.readLine();
                    ClientHandler clientHandler = new ClientHandler(socket, clientName, reader, writer);
                    clients.add(clientHandler);
                    clientHandler.start();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // định nghĩa class ClientHandler để xử lý các kết nối từ client
    private class ClientHandler extends Thread {
        private Socket socket;
        private String clientName;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket socket, String clientName, BufferedReader reader, PrintWriter writer) {
            this.socket = socket;
            this.clientName = clientName;
            this.reader = reader;
            this.writer = writer;
        }

        @Override
        public void run() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    chatArea.append(clientName + ": " + message + "\n");
                    for (ClientHandler client : clients) {
                        if (client != this) {
                            client.writer.println(clientName + ": " + message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                chatArea.append(clientName + " disconnected\n");
                clients.remove(this);
                for (ClientHandler client : clients) {
                    client.writer.println(clientName + " disconnected");
                }
            }
        }
    }
}