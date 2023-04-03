import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.awt.*;

public class ClientUI extends JFrame implements ActionListener {
    public static void main(String[] args) throws Exception {
        new ClientUI();
    }

    private JLabel portLabel, label;
    private JTextField inputField, portField;
    private JTextArea resultArea;
    private JButton sendButton, connectButton;

    ServerSocket server;
    Socket socket;
    int port;
    DataInputStream din;
    DataOutputStream dos;
    Scanner kb = new Scanner(System.in);

    public ClientUI() {
        setTitle("Client");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tạo thành phần trong giao diện
        portLabel = new JLabel("Port: ");
        portField = new JTextField();
        connectButton = new JButton("Connect");
        connectButton.addActionListener(this);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        inputField = new JTextField();
        label = new JLabel("Input: ");
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);

        // Thêm thành phần vào giao diện
        JPanel portPanel = new JPanel(new BorderLayout());
        portPanel.add(portLabel, BorderLayout.WEST);
        portPanel.add(portField, BorderLayout.CENTER);
        portPanel.add(connectButton, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.WEST);
        panel.add(inputField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(portPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        // Hiển thị giao diện
        setVisible(true);
    }

    private class ResultReceiver implements Runnable {
        public void run() {
            try {
                while (true) {
                    for (int i = 0; i < 10; i++) {
                        String result = din.readUTF();
                        resultArea.append(result + "\n");
                    }
                    resultArea.append("-------------------------------------------------\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String str = inputField.getText();
            resultArea.append("Chuoi: " + str + "\n");
            // Gửi biểu thưc tới server
            try {
                dos.writeUTF(str);
                dos.flush();
                kb = kb.reset();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            inputField.setText("");
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
                Thread t = new Thread(new ResultReceiver());
                t.start();

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Could not connect to server");
                return;
            }
        }
    }
}
