import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;

public class ChatClientUI extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new ChatClientUI();
    }
    private JLabel portLabel;
    private JTextField messageField, portField;
    private JTextArea chatArea;
    private JButton sendButton, connectButton;

    ServerSocket server;
    Socket socket;
    int port;
    DataInputStream din;
    DataOutputStream dos;
    Scanner kb = new Scanner(System.in);

    public ChatClientUI() {
        // Thiết lập giao diện
        setTitle("Chat App - Client");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tạo thành phần trong giao diện
        portLabel = new JLabel("Port: ");
        portField = new JTextField();
        connectButton = new JButton("Connect");
        connectButton.addActionListener(this);

        messageField = new JTextField();
        chatArea = new JTextArea();
        chatArea.setEditable(false);

        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        // Thêm thành phần vào giao diện
        JPanel portPanel = new JPanel(new BorderLayout());
        portPanel.add(portLabel, BorderLayout.WEST);
        portPanel.add(portField, BorderLayout.CENTER);
        portPanel.add(connectButton, BorderLayout.EAST);

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
            chatArea.append("Client: " + message + "\n");
            // Gửi tin nhắn tới server
            try {
                dos.writeUTF("Client: " + message);
                dos.flush();
                kb = kb.reset();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            messageField.setText("");
        }
        if (e.getSource() == connectButton) {
            try {
                port = Integer.parseInt(portField.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid port number");
                return;
            }
            try {
                // Kết nối tới server
                socket = new Socket("localhost", port);
                dos = new DataOutputStream(socket.getOutputStream());
                din = new DataInputStream(socket.getInputStream());
                JOptionPane.showMessageDialog(this, "Connected to server");
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
