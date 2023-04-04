import javax.swing.*;
import java.awt.*;
import java.net.*;

public class UDPServerGUI extends JFrame {
    private JTextArea textArea;
    private DatagramSocket socket;

    public UDPServerGUI() {
        super("UDP Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Create GUI components
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create socket and start listening for connections
        try {
            socket = new DatagramSocket(1234);
            textArea.append("Server started on port 1234\n");
            new Thread(() -> listen()).start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error starting server: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void listen() {
        while (true) {
            try {
                // Receive packet from client
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // Process packet and send response
                String request = new String(packet.getData(), 0, packet.getLength());
                String response = processRequest(request);
                byte[] responseData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length,
                        packet.getAddress(), packet.getPort());
                socket.send(responsePacket);

                // Log request and response
                textArea.append("Request: " + request + "\n");
                textArea.append("Response: " + response + "\n");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error handling request: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String processRequest(String request) {
        String response;
        if (request.startsWith("UPPERCASE:")) {
            response = request.substring(10).toUpperCase();
        } else if (request.startsWith("LOWERCASE:")) {
            response = request.substring(10).toLowerCase();
        } else if (request.startsWith("WORDCOUNT:")) {
            String[] words = request.substring(10).split("\\s+");
            response = Integer.toString(words.length);
        } else {
            response = "ERROR: Unknown request";
        }
        return response;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UDPServerGUI().setVisible(true));
    }
}
