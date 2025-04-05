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

    public RBTree() {
        TNULL = new Node(0);
        TNULL.color = Node.BLACK;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }

    // Fix insert violations
    private void fixInsert(Node node) {
        while (node.parent != null && node.parent.color == Node.RED) {
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                if (uncle.color == Node.RED) {
                    node.parent.color = Node.BLACK;
                    uncle.color = Node.BLACK;
                    node.parent.parent.color = Node.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.right) {
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = Node.BLACK;
                    node.parent.parent.color = Node.RED;
                    rotateRight(node.parent.parent);
                }
            } else {
                Node uncle = node.parent.parent.left;
                if (uncle.color == Node.RED) {
                    node.parent.color = Node.BLACK;
                    uncle.color = Node.BLACK;
                    node.parent.parent.color = Node.RED;
                    node = node.parent.parent;
                } else {
                    if (node == node.parent.left) {
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = Node.BLACK;
                    node.parent.parent.color = Node.RED;
                    rotateLeft(node.parent.parent);
                }
            }
            if (node == root) {
                break;
            }
        }
        root.color = Node.BLACK;
    }

    // Rotate left
    private void rotateLeft(Node node) {
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

    // Rotate right
    private void rotateRight(Node node) {
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
    
    // Insert a key into the tree
    public void insert(int key) {
        Node node = new Node(key);
        node.parent = null;
        node.left = TNULL;
        node.right = TNULL;

        Node y = null;
        Node x = root;

        while (x != TNULL) {
            y = x;
            if (node.key < x.key) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;

        if (y == null) {
            root = node;
        } else if (node.key < y.key) {
            y.left = node;
        } else {
            y.right = node;
        }

        if (node.parent == null) {
            node.color = Node.BLACK;
            return;
        }

        if (node.parent.parent == null) {
            return;
        }

        fixInsert(node);
    }

    // Transplant subtrees
    private void transplant(Node u, Node v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    // Find minimum of subtree
    private Node minimum(Node node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }

    // Fix delete violations
    private void fixDelete(Node x) {
        while (x != root && x.color == Node.BLACK) {
            if (x == x.parent.left) {
                Node w = x.parent.right;
                if (w.color == Node.RED) {
                    w.color = Node.BLACK;
                    x.parent.color = Node.RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }

                if (w.left.color == Node.BLACK && w.right.color == Node.BLACK) {
                    w.color = Node.RED;
                    x = x.parent;
                } else {
                    if (w.right.color == Node.BLACK) {
                        w.left.color = Node.BLACK;
                        w.color = Node.RED;
                        rotateRight(w);
                        w = x.parent.right;
                    }

                    w.color = x.parent.color;
                    x.parent.color = Node.BLACK;
                    w.right.color = Node.BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.left;
                if (w.color == Node.RED) {
                    w.color = Node.BLACK;
                    x.parent.color = Node.RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }

                if (w.right.color == Node.BLACK && w.left.color == Node.BLACK) {
                    w.color = Node.RED;
                    x = x.parent;
                } else {
                    if (w.left.color == Node.BLACK) {
                        w.right.color = Node.BLACK;
                        w.color = Node.RED;
                        rotateLeft(w);
                        w = x.parent.left;
                    }

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

    // Delete a node
    public void delete(int key) {
        Node node = root;
        Node z = TNULL;

        while (node != TNULL) {
            if (node.key == key) {
                z = node;
                break;
            }
            if (key < node.key) {
                node = node.left;
            } else {
                node = node.right;
            }
        }

        if (z == TNULL) {
            System.out.println("Key not found in the tree.");
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

    // Draw the tree for visualization
    public void drawTree(Graphics g, int x, int y, int xOffset) {
        drawNode(g, root, x, y, xOffset);
    }

    private void drawNode(Graphics g, Node node, int x, int y, int xOffset) {
        if (node == TNULL) {
            return;
        }
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
