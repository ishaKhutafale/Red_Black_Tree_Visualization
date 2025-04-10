import java.awt.*;

class Node {
    int key;
    Node parent;
    Node left;
    Node right;
    int color;

    public static final int RED = 0;
    public static final int BLACK = 1;

    public Node(int key) {
        this.key = key;
        color = RED;
        parent = null;
        left = null;
        right = null;
    }
}

public class RBTree {

    private Node root;
    private Node TNULL;
    private StringBuilder operationLog;

    public RBTree() {
        TNULL = new Node(0);
        TNULL.color = Node.BLACK;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
        operationLog = new StringBuilder();
    }

    public String getLastOperationLog() {
        return operationLog.toString();
    }

    public void clearOperationLog() {
        operationLog.setLength(0);
    }

    private void fixInsert(Node node) {
        while (node.parent != null && node.parent.color == Node.RED) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle.color == Node.RED) {
                    operationLog.append("Recoloring because parent and siblingOfParent are RED.\n");
                    node.parent.color = Node.BLACK;
                    uncle.color = Node.BLACK;
                    node.parent.parent.color = Node.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        operationLog.append("Left rotation needed (Triangle case: Right child of Left parent).\n");
                        node = node.parent;
                        rotateLeft(node);
                    }
                    operationLog.append("Right rotation needed (Line case: Left child of Left parent).\n");
                    node.parent.color = Node.BLACK;
                    node.parent.parent.color = Node.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                Node uncle = node.parent.parent.left;
                if (uncle.color == Node.RED) {
                    operationLog.append("Recoloring because parent and siblingOfParent are RED.\n");
                    node.parent.color = Node.BLACK;
                    uncle.color = Node.BLACK;
                    node.parent.parent.color = Node.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        operationLog.append("Right rotation needed (Triangle case: Left child of Right parent).\n");
                        node = node.parent;
                        rotateRight(node);
                    }
                    operationLog.append("Left rotation needed (Line case: Right child of Right parent).\n");
                    node.parent.color = Node.BLACK;
                    node.parent.parent.color = Node.RED;
                    rotateLeft(node.parent.parent);
                }
            }
            if (node == root) break;
        }
        root.color = Node.BLACK;
    }

    private void rotateLeft(Node node) {
        operationLog.append("Performing left rotation on node ").append(node.key).append(".\n");

        Node rightChild = node.right;
        node.right = rightChild.left;
        if (rightChild.left != TNULL) {
            rightChild.left.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(Node node) {
        operationLog.append("Performing right rotation on node ").append(node.key).append(".\n");

        Node leftChild = node.left;
        node.left = leftChild.right;
        if (leftChild.right != TNULL) {
            leftChild.right.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.right) {
            node.parent.right = leftChild;
        } else {
            node.parent.left = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    public boolean contains(int key) {
        Node current = root;
        while (current != TNULL) {
            if (key == current.key) {
                return true;
            } else if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return false;
    }

    public void insert(int key) {
        clearOperationLog();
        Node node = new Node(key);
        node.left = TNULL;
        node.right = TNULL;

        Node y = null;
        Node x = root;

        while (x != TNULL) {
            y = x;
            if (node.key < x.key) x = x.left;
            else x = x.right;
        }

        node.parent = y;

        if (y == null) root = node;
        else if (node.key < y.key) y.left = node;
        else y.right = node;

        if (node.parent == null) {
            node.color = Node.BLACK;
            return;
        }

        if (node.parent.parent == null) return;

        fixInsert(node);
    }

    private void transplant(Node u, Node v) {
        if (u.parent == null) root = v;
        else if (u == u.parent.left) u.parent.left = v;
        else u.parent.right = v;
        v.parent = u.parent;
    }

    private Node minimum(Node node) {
        while (node.left != TNULL) node = node.left;
        return node;
    }

    private void fixDelete(Node x) {
        while (x != root && x.color == Node.BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color == Node.RED) {
                    operationLog.append("Fixing double black: sibling is red, rotating left.\n");
                    w.color = Node.BLACK;
                    x.parent.color = Node.RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == Node.BLACK && w.right.color == Node.BLACK) {
                    operationLog.append("Fixing double black: sibling's children black, recoloring sibling.\n");
                    w.color = Node.RED;
                    x = x.parent;
                } else {
                    if (w.right.color == Node.BLACK) {
                        operationLog.append("Fixing double black: sibling's far child is black, rotating right.\n");
                        w.left.color = Node.BLACK;
                        w.color = Node.RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }

                    operationLog.append("Fixing double black: sibling's far child is red, rotating left.\n");
                    w.color = x.parent.color;
                    x.parent.color = Node.BLACK;
                    w.right.color = Node.BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color == Node.RED) {
                    operationLog.append("Fixing double black: sibling is red, rotating right.\n");
                    w.color = Node.BLACK;
                    x.parent.color = Node.RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == Node.BLACK && w.left.color == Node.BLACK) {
                    operationLog.append("Fixing double black: sibling's children black, recoloring sibling.\n");
                    w.color = Node.RED;
                    x = x.parent;
                } else {
                    if (w.left.color == Node.BLACK) {
                        operationLog.append("Fixing double black: sibling's far child is black, rotating left.\n");
                        w.right.color = Node.BLACK;
                        w.color = Node.RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }

                    operationLog.append("Fixing double black: sibling's far child is red, rotating right.\n");
                    w.color = x.parent.color;
                    x.parent.color = Node.BLACK;
                    w.left.color = Node.BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = Node.BLACK;
    }

    public void delete(int key) {
        clearOperationLog();

        Node node = root;
        Node z = TNULL;
        while (node != TNULL) {
            if (node.key == key) {
                z = node;
                break;
            }
            if (key < node.key) node = node.left;
            else node = node.right;
        }

        if (z == TNULL) {
            operationLog.append("Key not found.\n");
            return;
        }

        Node y = z;
        int yOriginalColor = y.color;
        Node x;

        if (z.left == TNULL) {
            x = z.right;
            transplant(z, z.right);
        } else if (z.right == TNULL) {
            x = z.left;
            transplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;

            if (y.parent == z) {
                x.parent = y;
            } else {
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }

            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }

        if (yOriginalColor == Node.BLACK) {
            fixDelete(x);
        }
    }

    public void drawTree(Graphics g, int x, int y, int xOffset) {
        drawNode(g, root, x, y, xOffset);
    }

    private void drawNode(Graphics g, Node node, int x, int y, int xOffset) {
        if (node == TNULL) return;

        g.setColor(node.color == Node.RED ? Color.RED : Color.BLACK);
        g.fillOval(x - 15, y - 15, 30, 30);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(node.key), x - 7, y + 5);

        if (node.left != TNULL) {
            g.setColor(Color.BLACK);
            g.drawLine(x, y, x - xOffset, y + 50);
            drawNode(g, node.left, x - xOffset, y + 50, xOffset / 2);
        }
        if (node.right != TNULL) {
            g.setColor(Color.BLACK);
            g.drawLine(x, y, x + xOffset, y + 50);
            drawNode(g, node.right, x + xOffset, y + 50, xOffset / 2);
        }
    }
}
