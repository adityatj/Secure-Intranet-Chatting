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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;

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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class JClient extends Frame implements WindowListener, ActionListener, KeyListener {

    public static TextArea textArea;
    public TextField textField, userNameT, passwordT, serverNameT, portT;
    public Button button, buttonD;
    public MenuBar menuBar;
    public Menu fileMenu, aboutMenu;
    public MenuItem connect, exit, about, instr;
    public Dialog connectDialog;
    public Label userNameL, passwordL, serverNameL, portL;
    public static List clientListC;
    public static int port = 999;
    public String clientName = "";
    public static String host = "localhost";
    public static ServerSocket serverSocket;
    public static Socket clientSocket;
    public static BufferedReader in;
    public static PrintWriter out;
    public JDataEncrypter dataEncrypter;
    /**
     * Default constructor for the JClient class which initializes the components, their properties and listeners.
     */

    public JClient() {
        setSize(450, 340);
        setTitle("JClient v1.0 @IDLE");
        setResizable(false);
        setLayout(new FlowLayout());
        /** AWT Components Declaration */
        textArea = new TextArea("", 15, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
        textArea.setEditable(false);
        textArea.setBackground(Color.WHITE);
        textField = new TextField("", 40);
        userNameT = new TextField("", 20);
        passwordT = new TextField("", 20);
        passwordT.setEchoChar('*');
        serverNameT = new TextField("", 20);
        portT = new TextField("", 20);
        button = new Button("Send");
        button.setPreferredSize(new Dimension(120, 23));
        buttonD = new Button("Connect");
        menuBar = new MenuBar();
        fileMenu = new Menu("File");
        aboutMenu = new Menu("About");
        connect = new MenuItem("Connect");
        exit = new MenuItem("Exit");
        about = new MenuItem("About the author");
        instr = new MenuItem("Instructions");
        connectDialog = new Dialog(this, "Connect...");
        userNameL = new Label("Username:");
        passwordL = new Label("Password:");
        serverNameL = new Label("Server IP:  ");
        portL = new Label("Port:           ");
        clientListC = new List(16);
        clientListC.setMultipleMode(true);
        dataEncrypter = new JDataEncrypter();
        /* Adding them to current frame */
        add(textArea);
        add(clientListC);
        add(textField);
        add(button);
        setMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        fileMenu.add(connect);
        fileMenu.add(exit);
        aboutMenu.add(about);
        aboutMenu.add(instr);
        /*Registering Listeners*/
        button.addActionListener(this);
        textField.addKeyListener(this);
        addWindowListener(this);
        connect.addActionListener(this);
        connectDialog.addWindowListener(this);
        buttonD.addActionListener(this);
        exit.addActionListener(this);
        about.addActionListener(this);
        instr.addActionListener(this);
        setVisible(true);
    }

    /**
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */
    public void windowActivated(WindowEvent we) {
    }
    /**
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowClosed(WindowEvent we) {
    }
    /**
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowDeactivated(WindowEvent we) {
    }
    /**
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowDeiconified(WindowEvent we) {
    }
    /**
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowIconified(WindowEvent we) {
    }
    /**
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowOpened(WindowEvent we) {
    }
    /**
     * @param we Represents the WindowEvent instance, used to get the information about event generated.
     */

    public void windowClosing(WindowEvent we) {
        if (we.getSource() == connectDialog) {
            connectDialog.dispose();
        } else {
            if (out != null) {
                out.println(dataEncrypter.encryptMsg("exit:" + clientName));
            }
            System.exit(1);
        }
    }

    /**
     * @param ae Represents the ActionEvent instance, used to get the information about event generated.
     */
    public void actionPerformed(ActionEvent ae) {
        String input;
        if (ae.getSource() == button) {
            input = textField.getText();
            String[] selected = clientListC.getSelectedItems();
            if (selected.length == 0) {
                out.println(dataEncrypter.encryptMsg("server:" + clientName + ":" + input));
            }
            for (int i = 0; i < selected.length; i++) {
                out.println(dataEncrypter.encryptMsg(selected[i] + ":" + input));
            }
            //out.println(input);
            textArea.append("\n" + clientName + "> " + input);
            textField.setText("");
        }
        if (ae.getSource() == exit) {
            if (out != null) {
                out.println(dataEncrypter.encryptMsg("exit:" + clientName));
            }
            System.exit(1);
        }
        if (ae.getSource() == connect) {
            connectDialog.setSize(270, 190);
            connectDialog.setResizable(false);
            connectDialog.setLayout(new FlowLayout());
            connectDialog.add(serverNameL);
            connectDialog.add(serverNameT);
            connectDialog.add(portL);
            connectDialog.add(portT);
            connectDialog.add(userNameL);
            connectDialog.add(userNameT);
            connectDialog.add(passwordL);
            connectDialog.add(passwordT);
            connectDialog.add(buttonD);
            connectDialog.setVisible(true);
        }
        if (ae.getSource() == buttonD) {
            try {
                host = serverNameT.getText();
                port = Integer.parseInt(portT.getText());
                clientSocket = new Socket(host, port);
                textArea.append("\nServer connection established...");
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(dataEncrypter.encryptMsg("user:" + userNameT.getText() + ":pass:" + passwordT.getText()));
                setTitle("JClient v1.0 " + userNameT.getText() + "@" + host);
                clientName = userNameT.getText();
                Thread t = new Thread(new JServerConnection(clientSocket, textArea, clientListC));
                t.start();
                connectDialog.dispose();
            } catch (Exception ex) {
                textArea.append("\nConnection lost...");
                ex.printStackTrace();
            }
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
        int key = ke.getKeyCode();
        String input, u;
        if (key == KeyEvent.VK_ENTER) {
            input = textField.getText();
            textArea.append("\n" + clientName + "> " + input);
            textField.setText("");
            String[] selected = clientListC.getSelectedItems();
            if (selected.length == 0) {
                out.println(dataEncrypter.encryptMsg("server:" + clientName + ":" + input));
            }
            for (int i = 0; i < selected.length; i++) {
                out.println(dataEncrypter.encryptMsg(selected[i] + ":" + input));
            }

            //out.println(input);
            
        }
    }

    /**
     *Main method for triggering the server.
     * @param args args is is never used.
     */
    public static void main(String[] args) {
        JClient client = new JClient();
        String str;
        textArea.setText("ChatClient Idle...");
        try {
            /*clientSocket = new Socket(host, port);
            textArea.append("Server connection established...");
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            while((str = in.readLine()) != null)
            {
            textArea.append("\nServer> " + str);
            }*/
        } catch (Exception ex) {
            textArea.append("\nConnection lost...");
            //ex.printStackTrace();
        }
    }
}
