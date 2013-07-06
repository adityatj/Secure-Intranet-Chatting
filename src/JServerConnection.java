//package src;


import java.awt.Color;
import java.awt.List;
import java.awt.TextArea;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class JServerConnection implements Runnable {

    /**
     * Socket for handling the server incoming data.
     */
    public Socket socket;
    /**
     * Create input stream to receive data from server.
     */
    public BufferedReader in;
    /**
     * Create text area to display conversational information.
     */
    public TextArea textArea;
    /**
     * Create a list to display the active client's list.
     */
    public List list;
    /**
     * Create an instance for the class JDataEncrypter to decrypt messages.
     */
     public JDataEncrypter dataEncrypter;
    /**
     * Parameterized constructor for the class JServerConnection.
     * @param s The socket to receive server's data.
     * @param ta Client's TextArea to display updates and conversations.
     * @param l Client list, shows the current active users list.
     */
    public JServerConnection(Socket s, TextArea ta, List l) {
        this.socket = s;
        this.textArea = ta;
        this.list = l;
        dataEncrypter = new JDataEncrypter();
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * run() method which specifies the work to be performed when thread is invoked.
     */
    public void run() {
        String str;
        try {
            while ((str = in.readLine()) != null) {
                str = dataEncrypter.decryptMsg(JStringByteConverter.convertStrToByte(str));
                String[] szt = str.split(":");
                StringTokenizer st = new StringTokenizer(str, ":");
                String[] s = new String[st.countTokens()];
                if (st.nextToken().equals("list")) {
                    list.removeAll();
                    //for(int i = 1; i < s.length ; i++)
                    //list.add(s[i]);
                    while (st.hasMoreTokens()) {
                        list.add(st.nextToken());
                    }
                } else if (szt.length == 2) {
                    //textArea.setForeground(Color.RED);
                    //textArea.setBackground(Color.GREEN);
                    textArea.append("\n*Priv msg from " + szt[0] + "> " + szt[1]);
                   // textArea.setForeground(Color.BLACK);
                } else if (szt.length == 3) {
                    textArea.append("\n" + szt[1] + "> " + szt[2]);
                } else {
                    textArea.append("\n>>> " + str);
                }
            }
        } catch (Exception ex) {
            textArea.append("\nConnection lost...");
            ex.printStackTrace();
        }
    }
}
