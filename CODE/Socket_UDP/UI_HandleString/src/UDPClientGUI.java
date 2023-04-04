import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class UDPClientGUI extends JFrame implements ActionListener {
    private JPanel panel;
    private JTextField textField;
    private JTextArea textArea;
    private JButton sendButton, clearButton;
    private DatagramSocket clientSocket;
    private InetAddress serverIPAddress;
    private byte[] sendData;
    private byte[] receiveData;

    public UDPClientGUI() {
        this.setTitle("UDP Client");
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create GUI components
        panel = new JPanel(new GridLayout(3, 1));
        textField = new JTextField(20);
        textArea = new JTextArea();
        textArea.setEditable(false);
        sendButton = new JButton("Send");
        clearButton = new JButton("Clear");

        // Add components to panel
        panel.add(textField);
        panel.add(sendButton);
        panel.add(clearButton);

        // Add panel to frame
        this.add(panel, BorderLayout.NORTH);
        this.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Set action listener for buttons
        sendButton.addActionListener(this);
        clearButton.addActionListener(this);

        // Initialize client socket and server address
        try {
            clientSocket = new DatagramSocket();
            serverIPAddress = InetAddress.getByName("localhost");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            try {
                String message = textField.getText();

                // Send message to server
                sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, 1234);
                clientSocket.send(sendPacket);

                // Receive response from server
                receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                textArea.append("Server response: " + response + "\n");

                // Clear text field
                textField.setText("");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == clearButton) {
            textArea.setText("");
        }
    }

    public static void main(String[] args) {
        UDPClientGUI client = new UDPClientGUI();
        client.setVisible(true);
    }
}
