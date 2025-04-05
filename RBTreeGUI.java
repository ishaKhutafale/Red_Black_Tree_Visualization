import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RBTreeGUI extends JFrame {

    private JTextField inputField;
    private JButton insertButton;
    private JButton deleteButton;
    private JPanel treePanel;
    private JButton findButton;

    private RBTree tree = new RBTree();  // Red-Black Tree instance

    public RBTreeGUI() {
        setTitle("Red-Black Tree Visualizer");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Custom Font
        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        // Top panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBackground(new Color(240, 240, 240));

        inputField = new JTextField(10);
        inputField.setFont(font);

        insertButton = createStyledButton("Insert");
        deleteButton = createStyledButton("Delete");
        findButton = createStyledButton("Find");
        controlPanel.add(findButton);
    

        controlPanel.add(new JLabel("Enter Value: "));
        controlPanel.add(inputField);
        controlPanel.add(insertButton);
        controlPanel.add(deleteButton);

        add(controlPanel, BorderLayout.NORTH);

        // Drawing Panel
        treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                tree.drawTree(g2, getWidth() / 2, 40, getWidth() / 4);
            }
        };
        treePanel.setBackground(Color.WHITE);
        treePanel.setPreferredSize(new Dimension(800, 500));
        add(new JScrollPane(treePanel), BorderLayout.CENTER);

        // Button Listeners
        insertButton.addActionListener(e -> {
            handleInput(true);
        });

        deleteButton.addActionListener(e -> {
            handleInput(false);
        });

        findButton.addActionListener(e -> {
            String value = inputField.getText().trim();
            if (!value.isEmpty()) {
                try {
                    int key = Integer.parseInt(value);
                    boolean found = tree.contains(key);
                    if (found) {
                        JOptionPane.showMessageDialog(this, "Key " + key + " found in the tree.", "Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Key " + key + " not found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
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
                    System.out.println("Inserted: " + key);
                } else {
                    tree.delete(key);
                    System.out.println("Deleted: " + key);
                }
                treePanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RBTreeGUI().setVisible(true));
    }
}
