//package src;


import java.awt.TextArea;
import java.awt.List;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class JClientConnection implements Runnable {

    public Socket clientSocket;
    public BufferedReader in;
    public PrintWriter out;
    public TextArea textArea;
    public List list;
    public JDataEncrypter dataEncrypter;
    /**
     * Parameterized constructor for the class JClientConnection.
     * @param clientSocket Specifies the client socket to send and receive data.
     * @param ta TextArea for server in order to display the information regarding client and conversations.
     * @param l Client list for server, displays active client list.
     */

    public JClientConnection(Socket clientSocket, TextArea ta, List l) {
        this.clientSocket = clientSocket;
        this.textArea = ta;
        this.list = l;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        dataEncrypter = new JDataEncrypter();
    }
    /**
     * run() method which specifies the work to be performed when thread is invoked.
     */

    public void run() {
        String str, response;
        JChatServerProtocol protocol = new JChatServerProtocol(this, list, textArea);
        try {
            while ((str = in.readLine()) != null) {
                response = protocol.processInput(str);
                if (response != null) {
                    out.println(dataEncrypter.encryptMsg(response));
                }
                //textArea.append("\nServer> " + response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * This method send message to a particular client.
     * @param msg Contains message string.
     */

    public void sendMsg(String msg) {
        out.println(msg);
    }
}
