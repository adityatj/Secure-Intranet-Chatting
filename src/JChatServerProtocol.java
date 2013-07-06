//package src;


import java.awt.TextArea;
import java.awt.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class JChatServerProtocol {

    /**
     *
     */
    public String user, pass;
    public JClientConnection con;
    public File passFile;
    public List list;
    public TextArea textArea;
    public JDataEncrypter dataEncrypter;
    PrintWriter out;

    public static Hashtable<String, JClientConnection> users = new Hashtable<String, JClientConnection>();
    /**
     * Parameterized constructor for the class JChatServerProtocol.
     * @param c Specifies the client connection object retrieved from the class JChatServerProtocol.
     * @param l Client List, which signifies the current active client list.
     * @param ta Specifies TextArea in order to display the notices, messages, conversations.
     */

    public JChatServerProtocol(JClientConnection c, List l, TextArea ta) {
        user = null;
        pass = null;
        con = c;
        this.list = l;
        this.textArea = ta;
        dataEncrypter = new JDataEncrypter();
        passFile = new File("passwd.txt");
        try {
            out = new PrintWriter(passFile);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Failed to open password file !");
        }
    }
    /**
     * This method if the user is authenticated with server.
     * @return It returns a boolean value.
     */

    public boolean isAuthenticated() {
        return !(user == null);
    }
    /**
     * This method signifies the addition of user to server's authenticated user's list.
     * @param user Specifies the user name to be added.
     * @param c Specifies the client connection object of the particular client.
     * @return It returns a boolean value.
     */

    public boolean addNick(String user, JClientConnection c) {
        if (users.containsKey(user)) {
            return false;
        } else {
            users.put(user, c);
            return true;
        }
    }
    /**
     * This method authenticates the user, in case of a new user.
     * @param msg Specifies the message received from client in order to carry forward authentication process.
     * @return It returns a response string.
     */

    public String authenticate(String msg) {
        String[] s = msg.split(":");
        if (s.length == 4 && (s[0].equals("user") && s[2].equals("pass"))) {
            if (addNick(s[1], this.con)) {
                this.user = s[1];
                list.add(s[1]);
                textArea.append("\n" + s[1] + " connected @" + this.con.clientSocket.getInetAddress() + " and Authenticated Successfully !");
                String u, str = "list", del = ":";
                String clist[] = list.getItems();
                Enumeration e;
                e = users.keys();
                for (int i = 0; i < clist.length - 1; i++) {
                    str = str + del + clist[i] + del;
                }
                str = str + clist[clist.length - 1];
                if (clist.length == 1) {
                    str = "list" + del + clist[clist.length - 1];
                }
                //System.out.println(str);
                while (e.hasMoreElements()) {
                    u = e.nextElement().toString();
                    JClientConnection conn = users.get(u);
                    conn.sendMsg(dataEncrypter.encryptMsg(str));
                }
                out.println(dataEncrypter.encryptMsg("user: " + s[1] + " pass: " + s[3]));
                out.flush();
                return "Authentication successfull!";
            } else {
                textArea.append("\n" + s[1] + " connected @" + this.con.clientSocket.getInetAddress() + " Authentication Failed !");
                return "Authentication Failed!";
            }
        }
        return "Authentication Failed!";
    }
    /**
     * This method verifies the existence of recipient user in active user's list, if so it sends the message.
     * @param reciever Specifies the recipient of message.
     * @param msg Specifies the message to be sent.
     */

    public void sendMsg(String reciever, String msg) {


        if (users.containsKey(reciever)) {
            JClientConnection c = users.get(reciever);
            c.sendMsg(dataEncrypter.encryptMsg(user + ":" + msg));
        }
    }
    /**
     * This method processes the data forwarded by the JClientConnection class which was in turn sent by client.
     * @param msg Specifies the data string which contains information.
     * @return It returns a string containing information about authentication of client.
     */

    public String processInput(String msg) {

        String m = dataEncrypter.decryptMsg(JStringByteConverter.convertStrToByte(msg));
        msg = m;
        if (!isAuthenticated()) {
            return authenticate(msg);
        }
        String[] s = msg.split(":");
        //if(s.length == 2)
        //{
        if (s[0].equals("server")) {
            String u;
            textArea.append("\n" + s[1] + "> " + s[2]);
            //String[] selected = list.getSelectedItems();
            Enumeration e = JChatServerProtocol.users.keys();
            //if(selected.length == 0){
            while (e.hasMoreElements()) {
                u = e.nextElement().toString();
                JClientConnection conn = users.get(u);
                if (!u.equals(s[1])) {
                    conn.sendMsg(dataEncrypter.encryptMsg("pub:" + s[1] + ":" + s[2]));
                }
            }
            //}
        } else if (s[0].equals("exit")) {
            textArea.append("\n"+s[1] + " has disconnected !");
            list.remove(s[1]);
            users.remove(s[1].toString());
            String u, str = "list", del = ":";
            String clist[] = list.getItems();
            Enumeration e;
            e = users.keys();
            for (int i = 0; i < clist.length - 1; i++) {
                str = str + del + clist[i] + del;
            }
            str = str + clist[clist.length - 1];
            if (clist.length == 1) {
                str = "list" + del + clist[clist.length - 1];
            }
            //System.out.println(str);
            while (e.hasMoreElements()) {
                u = e.nextElement().toString();
                JClientConnection conn = users.get(u);
                conn.sendMsg(dataEncrypter.encryptMsg(str));
            }
            
        } else {
            sendMsg(s[0], s[1]);
        }
        //return null; //"Message Sent !"
        //}
        return null;//"Message Sending Failed !"
    }
}
