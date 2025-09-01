// Save this file as: BankingSystemGUI.java

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;

public class BankingSystemGUI extends JFrame implements ActionListener {

    CardLayout cardLayout;
    JPanel mainPanel, loginPanel, menuPanel;
    JTextField accField;
    JPasswordField pinField;

    int[] accountNumbers = {12345, 54321};
    int[] pins = {1111, 2222};
    double[] balances = {1000.0, 2000.0};
    ArrayList<TransactionEntry>[] transactions = new ArrayList[2];
    int currentAccount = -1;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    JButton loginBtn, checkBalanceBtn, depositBtn, withdrawBtn, transferBtn, historyBtn, logoutBtn;

    class TransactionEntry {

        String message;
        LocalDateTime timestamp;

        TransactionEntry(String message, LocalDateTime timestamp) {
            this.message = message;
            this.timestamp = timestamp;
        }
    }

    public BankingSystemGUI() {
        // UI customizations
        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("PasswordField.font", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 14));
        UIManager.put("OptionPane.messageFont", new Font("SansSerif", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("SansSerif", Font.BOLD, 13));
        UIManager.put("OptionPane.background", Color.white);
        UIManager.put("Panel.background", Color.white);

        for (int i = 0; i < transactions.length; i++) {
            transactions[i] = new ArrayList<>();
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // LOGIN PANEL
        loginPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(58, 123, 213), 0, getHeight(), new Color(0, 210, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        loginPanel.setLayout(new GridBagLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("ATM Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        formPanel.add(title, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel accLabel = new JLabel("Account Number:");
        accLabel.setForeground(Color.WHITE);
        formPanel.add(accLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        accField = new JTextField(15);
        formPanel.add(accField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setForeground(Color.WHITE);
        formPanel.add(pinLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        pinField = new JPasswordField(15);
        formPanel.add(pinField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(120, 35));
        loginBtn.setBackground(new Color(0, 150, 136));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(this);
        formPanel.add(loginBtn, gbc);

        loginPanel.add(formPanel);
        mainPanel.add(loginPanel, "Login");

        // MENU PANEL
        menuPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255), 0, getHeight(), new Color(200, 230, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbcMenu = new GridBagConstraints();
        gbcMenu.insets = new Insets(10, 10, 10, 10);
        gbcMenu.gridx = 0;

        checkBalanceBtn = createMenuButton("Check Balance");
        depositBtn = createMenuButton("Deposit");
        withdrawBtn = createMenuButton("Withdraw");
        transferBtn = createMenuButton("Transfer");
        historyBtn = createMenuButton("Transaction History");
        logoutBtn = createMenuButton("Logout");

        JButton[] menuButtons = {checkBalanceBtn, depositBtn, withdrawBtn, transferBtn, historyBtn, logoutBtn};

        for (int i = 0; i < menuButtons.length; i++) {
            gbcMenu.gridy = i;
            menuPanel.add(menuButtons[i], gbcMenu);
        }

        mainPanel.add(menuPanel, "Menu");

        add(mainPanel);
        setTitle("ATM Banking System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cardLayout.show(mainPanel, "Login");
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(0, 150, 136));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            try {
                int acc = Integer.parseInt(accField.getText());
                int pin = Integer.parseInt(new String(pinField.getPassword()));
                for (int i = 0; i < accountNumbers.length; i++) {
                    if (accountNumbers[i] == acc && pins[i] == pin) {
                        currentAccount = i;
                        JOptionPane.showMessageDialog(this, "Login successful!");
                        cardLayout.show(mainPanel, "Menu");
                        return;
                    }
                }
                JOptionPane.showMessageDialog(this, "Invalid Login!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric credentials.");
            }
        } else if (e.getSource() == checkBalanceBtn) {
            JOptionPane.showMessageDialog(this, "Balance: ₹" + balances[currentAccount]);
        } else if (e.getSource() == depositBtn) {
            String amtStr = JOptionPane.showInputDialog("Enter amount to deposit:");
            if (amtStr != null) {
                try {
                    double amt = Double.parseDouble(amtStr);
                    if (amt > 0) {
                        balances[currentAccount] += amt;
                        LocalDateTime now = LocalDateTime.now();
                        String msg = dtf.format(now) + " - Deposited ₹" + amt;
                        transactions[currentAccount].add(new TransactionEntry(msg, now));
                        JOptionPane.showMessageDialog(this, "Deposit successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Enter a positive amount.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount entered.");
                }
            }
        } else if (e.getSource() == withdrawBtn) {
            String amtStr = JOptionPane.showInputDialog("Enter amount to withdraw:");
            if (amtStr != null) {
                try {
                    double amt = Double.parseDouble(amtStr);
                    if (amt > 0 && amt <= balances[currentAccount]) {
                        balances[currentAccount] -= amt;
                        LocalDateTime now = LocalDateTime.now();
                        String msg = dtf.format(now) + " - Withdrew ₹" + amt;
                        transactions[currentAccount].add(new TransactionEntry(msg, now));
                        JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid or insufficient amount.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount entered.");
                }
            }
        } else if (e.getSource() == transferBtn) {
            String toAccStr = JOptionPane.showInputDialog("Enter account number to transfer:");
            String amtStr = JOptionPane.showInputDialog("Enter amount to transfer:");
            if (toAccStr != null && amtStr != null) {
                try {
                    int toAcc = Integer.parseInt(toAccStr);
                    double amt = Double.parseDouble(amtStr);
                    int targetIndex = -1;
                    for (int i = 0; i < accountNumbers.length; i++) {
                        if (accountNumbers[i] == toAcc) {
                            targetIndex = i;
                            break;
                        }
                    }
                    if (targetIndex == -1) {
                        JOptionPane.showMessageDialog(this, "Invalid target account!");
                    } else if (amt > 0 && amt <= balances[currentAccount]) {
                        balances[currentAccount] -= amt;
                        balances[targetIndex] += amt;
                        LocalDateTime now = LocalDateTime.now();
                        String sentMsg = dtf.format(now) + " - Transferred ₹" + amt + " to " + toAcc;
                        String receivedMsg = dtf.format(now) + " - Received ₹" + amt + " from " + accountNumbers[currentAccount];
                        transactions[currentAccount].add(new TransactionEntry(sentMsg, now));
                        transactions[targetIndex].add(new TransactionEntry(receivedMsg, now));
                        JOptionPane.showMessageDialog(this, "Transfer successful!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid or insufficient amount.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input.");
                }
            }
        } else if (e.getSource() == historyBtn) {
            StringBuilder sb = new StringBuilder("Transaction History (last 2 days):\n");
            LocalDateTime now = LocalDateTime.now();
            transactions[currentAccount].removeIf(entry -> entry.timestamp.isBefore(now.minusDays(2)));
            if (transactions[currentAccount].isEmpty()) {
                sb.append("No recent transactions.");
            } else {
                for (TransactionEntry entry : transactions[currentAccount]) {
                    sb.append(entry.message).append("\n");
                }
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        } else if (e.getSource() == logoutBtn) {
            currentAccount = -1;
            accField.setText("");
            pinField.setText("");
            cardLayout.show(mainPanel, "Login");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BankingSystemGUI().setVisible(true);
        });
    }
}
