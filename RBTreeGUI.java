import javax.swing.*;
import java.awt.*;

public class RBTreeGUI extends JFrame {

    private JTextField inputField;
    private JButton insertButton;
    private JButton deleteButton;
    private JButton findButton;
    private JPanel treePanel;
    private JTextArea logArea;

    private RBTree tree = new RBTree();

    public RBTreeGUI() {
        setTitle("Red-Black Tree Visualizer");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBackground(new Color(240, 240, 240));

        inputField = new JTextField(10);
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        insertButton = createStyledButton("Insert");
        deleteButton = createStyledButton("Delete");
        findButton = createStyledButton("Find");

        controlPanel.add(new JLabel("Enter Value: "));
        controlPanel.add(inputField);
        controlPanel.add(insertButton);
        controlPanel.add(deleteButton);
        controlPanel.add(findButton);

        add(controlPanel, BorderLayout.NORTH);

        treePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                tree.drawTree(g, getWidth() / 2, 40, getWidth() / 4);
            }
        };
        treePanel.setBackground(Color.WHITE);
        treePanel.setPreferredSize(new Dimension(800, 500));

        JScrollPane treeScroll = new JScrollPane(treePanel);
        add(treeScroll, BorderLayout.CENTER);

        // Log area
        logArea = new JTextArea(6, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.BOLD, 15));
        logArea.setBackground(new Color(250, 250, 250));
        logArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Operation Log"),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        insertButton.addActionListener(e -> handleInput(true));
        deleteButton.addActionListener(e -> handleInput(false));
        findButton.addActionListener(e -> {
            String value = inputField.getText().trim();
            if (!value.isEmpty()) {
                try {
                    int key = Integer.parseInt(value);
                    boolean found = tree.contains(key);
                    logArea.setText(found ? "Key " + key + " was found in the tree.\n"
                            : "Key " + key + " was NOT found in the tree.\n");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number.");
                }
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 30));
        button.setBorder(BorderFactory.createLineBorder(new Color(60, 120, 170)));
        return button;
    }

    private void handleInput(boolean isInsert) {
        String value = inputField.getText().trim();
        if (!value.isEmpty()) {
            try {
                int key = Integer.parseInt(value);
                if (isInsert) {
                    tree.insert(key);
                } else {
                    tree.delete(key);
                }
                logArea.setText(tree.getLastOperationLog());
                treePanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RBTreeGUI().setVisible(true));
    }
}
