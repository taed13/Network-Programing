import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;

public class ChatServerUI extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new ChatServerUI();
    }

    private JLabel portLabel;
    private JTextField messageField, portField;
    private JTextArea chatArea;
    private JButton sendButton, startButton;

    ServerSocket server;
    Socket socket;
    int port;
    DataInputStream din;
    DataOutputStream dos;
    Scanner kb = new Scanner(System.in);

    public ChatServerUI() {
        // Thiết lập giao diện
        setTitle("Chat App - Server");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tạo thành phần trong giao diện
        portLabel = new JLabel("Port: ");
        portField = new JTextField();
        startButton = new JButton("Start");
        startButton.addActionListener(this);

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        messageField = new JTextField();
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        // Thêm thành phần vào giao diện
        JPanel portPanel = new JPanel(new BorderLayout());
        portPanel.add(portLabel, BorderLayout.WEST);
        portPanel.add(portField, BorderLayout.CENTER);
        portPanel.add(startButton, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(messageField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(chatArea);

        add(portPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // Hiển thị giao diện
        setVisible(true);
    }

    private class MessageReceiver implements Runnable {
        public void run() {
            try {
                while (true) {
                    String message = din.readUTF();
                    chatArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String message = messageField.getText();
            chatArea.append("Server: " + message + "\n");
            // Gửi tin nhắn tới Client
            try {
                dos.writeUTF("Server: " + message);
                dos.flush();
                kb = kb.reset();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            messageField.setText("");
        }
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
                Thread t = new Thread(new MessageReceiver());
                t.start();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not connect to server");
                return;
            }
        }
    }

}
