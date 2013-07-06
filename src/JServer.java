//package src;

/** Imports regarding AWT Components */

import java.awt.Frame;
import java.awt.MenuBar;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.List;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
/** Imports regarding AWT Events and Listners */
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
/** Imports regarding Network Classes and I/O classes */
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class JServer extends Frame implements WindowListener, ActionListener, KeyListener {

    public static TextArea textArea;
    public TextField textField;
    public Button button;
    public static List clientList;
    public MenuBar menuBar;
    public Menu fileMenu, aboutMenu;
    public MenuItem exit, about, instr;
    public static final int port = 999;
    public static ServerSocket serverSocket;
    public static Socket clientSocket;
    public static BufferedReader in;
    public static PrintWriter out;
    public InetAddress thisIp;
    public JDataEncrypter dataEncrypter;

    /**
     * Default constructor, used to initialize AWT Frame Components.
     * Also, sets up events and listeners.
     */
    public JServer() {
        setSize(450, 340);
        try {
            thisIp = InetAddress.getLocalHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("JServer v1.0 @" + thisIp.getHostAddress());
        setResizable(false);
        setLayout(new FlowLayout());
        /* AWT Components Declaration */
        textArea = new TextArea("", 15, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textField = new TextField("", 40);
        button = new Button("Send");
        button.setPreferredSize(new Dimension(120, 23));
        clientList = new List(16);
        clientList.setMultipleMode(true);
        menuBar = new MenuBar();
        fileMenu = new Menu("File");
        aboutMenu = new Menu("About");
        exit = new MenuItem("Exit");
        about = new MenuItem("About the author");
        instr = new MenuItem("Instructions");
        dataEncrypter = new JDataEncrypter();
        /* Adding them to current frame */
        add(textArea);
        add(clientList);
        add(textField);
        add(button);
        setMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        fileMenu.add(exit);
        aboutMenu.add(about);
        aboutMenu.add(instr);
        /*Registering Listeners*/
        button.addActionListener(this);
        textField.addKeyListener(this);
        exit.addActionListener(this);
        about.addActionListener(this);
        instr.addActionListener(this);
        addWindowListener(this);
        setVisible(true);
    }
    /**
     * Triggered when a window event occurs.
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowActivated(WindowEvent we) {
    }
    /**
     * Triggered when a window event occurs.
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowClosed(WindowEvent we) {
    }
    /**
     * Triggered when a window event occurs.
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowDeactivated(WindowEvent we) {
    }
    /**
     * Triggered when a window event occurs.
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowDeiconified(WindowEvent we) {
    }
    /**
     * Triggered when a window event occurs.
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowIconified(WindowEvent we) {
    }
    /**
     * Triggered when a window event occurs.
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowOpened(WindowEvent we) {
    }
    /**
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowClosing(WindowEvent we) {
        System.exit(1);
    }
    /**
     * @param ae Represents the ActionEvent instance, used to get the information about event generated.
     */

    public void actionPerformed(ActionEvent ae) {
        String input;
        if (ae.getSource() == button) {
            String u;
            input = textField.getText();
            String[] selected = clientList.getSelectedItems();
            Enumeration e = JChatServerProtocol.users.keys();
            if (selected.length == 0) {
                while (e.hasMoreElements()) {
                    u = e.nextElement().toString();
                    JClientConnection conn = JChatServerProtocol.users.get(u);
                    conn.sendMsg(dataEncrypter.encryptMsg(input));
                }
            } else {
                while (e.hasMoreElements()) {
                    u = e.nextElement().toString();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i].equals(u)) {
                            JClientConnection conn = JChatServerProtocol.users.get(u);
                            conn.sendMsg(dataEncrypter.encryptMsg("Server:" + input));
                        }
                    }
                }
            }

            textArea.append("\nServer> " + input);
            textField.setText("");
        }
        if (ae.getSource() == exit) {
            System.exit(1);
        }
        if (ae.getSource() == about) {
            try {
                Process p = Runtime.getRuntime().exec("cmd /c notepad Aboutme.txt");

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        if (ae.getSource() == instr) {
            try {
                Process p = Runtime.getRuntime().exec("cmd /c notepad Instructions.txt");

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
    /**
     * @param ke Represents the KeyEvent instance, used to get the information about event generated.
     */

    public void keyTyped(KeyEvent ke) {
    }
    /**
     * @param ke Represents the KeyEvent instance, used to get the information about event generated.
     */

    public void keyReleased(KeyEvent ke) {
    }
    /**
     * @param ke Represents the KeyEvent instance, used to get the information about event generated.
     */

    public void keyPressed(KeyEvent ke) {
        String u;
        int key = ke.getKeyCode();
        String input;
        if (key == KeyEvent.VK_ENTER) {
            input = textField.getText();
            String[] selected = clientList.getSelectedItems();
            Enumeration e = JChatServerProtocol.users.keys();
            if (selected.length == 0) {
                while (e.hasMoreElements()) {
                    u = e.nextElement().toString();
                    JClientConnection conn = JChatServerProtocol.users.get(u);
                    conn.sendMsg(dataEncrypter.encryptMsg(input));
                }
            } else {
                while (e.hasMoreElements()) {
                    u = e.nextElement().toString();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i].equals(u)) {
                            JClientConnection conn = JChatServerProtocol.users.get(u);
                            conn.sendMsg(dataEncrypter.encryptMsg("Server:" + input));
                        }
                    }
                }
            }
            textArea.append("\n>>> " + input);
            textField.setText("");
        }
    }
    /**Main method for triggering the server.
     *
     * @param args args is is never used.
     */

    public static void main(String[] args) {
        JServer server = new JServer();
        String str;
        try {
            serverSocket = new ServerSocket(999);
            textArea.setText("Listening on port: " + port + "...");
            while (true) {
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                Thread t = new Thread(new JClientConnection(clientSocket, textArea, clientList));
                t.start();
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }
}
