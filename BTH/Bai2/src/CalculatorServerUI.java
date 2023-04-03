import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;
import java.awt.*;

public class CalculatorServerUI extends JFrame implements ActionListener {
    public static void main(String[] args) {
        new CalculatorServerUI();
    }

    private JLabel portLabel;
    private JTextField portField;
    private JTextArea resultArea;
    private JButton startButton;

    ServerSocket server;
    Socket socket;
    int port;
    DataInputStream din;
    DataOutputStream dos;
    Scanner kb = new Scanner(System.in);

    public CalculatorServerUI() {
        // Thiết lập giao diện
        setTitle("Calculator Server");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tạo thành phần trong giao diện
        portLabel = new JLabel("Port: ");
        portField = new JTextField();
        startButton = new JButton("Start");
        startButton.addActionListener(this);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        // Thêm thành phần vào giao diện
        JPanel portPanel = new JPanel(new BorderLayout());
        portPanel.add(portLabel, BorderLayout.WEST);
        portPanel.add(portField, BorderLayout.CENTER);
        portPanel.add(startButton, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(portPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Hiển thị giao diện
        setVisible(true);
    }

    private class Receiver implements Runnable {
        public void run() {
            try {
                while (true) {
                    String st = din.readUTF();
                    resultArea.append(st + "\n");
                    dos.writeUTF(st + " = " + Double.toString(evaluate(st)));
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

    public static double evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Double> values = new Stack<Double>();
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;
            if (tokens[i] >= '0' &&
                    tokens[i] <= '9' ||
                    tokens[i] == '.') {
                StringBuffer sbuf = new StringBuffer();
                while (i < tokens.length) {
                    if (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.')
                        sbuf.append(tokens[i++]);
                    else
                        break;
                }
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (tokens[i] == '(')
                ops.push(tokens[i]);

            else if (tokens[i] == ')') {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));
                ops.pop();
            } else if (tokens[i] == '+' ||
                    tokens[i] == '-' ||
                    tokens[i] == '*' ||
                    tokens[i] == '/') {
                while (!ops.empty() &&
                        hasPrecedence(tokens[i],
                                ops.peek()))
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));

                // Push current token to 'ops'.
                ops.push(tokens[i]);
            }
        }

        // Entire expression has been
        // parsed at this point, apply remaining
        // ops to remaining values
        while (!ops.empty())
            values.push(applyOp(ops.pop(),
                    values.pop(),
                    values.pop()));

        return values.pop();
    }

    public static boolean hasPrecedence(
            char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') &&
                (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    public static double applyOp(char op,
            double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException(
                            "Cannot divide by zero");
                return a / b;
        }
        return 0;
    }
}
