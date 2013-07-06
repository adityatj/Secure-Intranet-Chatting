//package src;


import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 *
 * @author Mrc0d3r
 */
public class JDataEncrypter {

    public static byte[] bkey;
    /**
     * Contains the Half-MD5 key.
     */
    public static String key = null;
    /**
     * Instance of JMD5HashGenerator to generate hash.
     */
    public JMD5HashGenerator generateHash;
    /**
     * Default constructor for the class JDataEncrypter.Initializes the variables.
     */
    public JDataEncrypter(){
        generateHash = new JMD5HashGenerator();//key: SecretkeyforJIntranetChatcodedbyMrc0d3rd4g33k
        key = generateHash.getMD5Hash("6E773FD4CFF2AC16E6811E289602BD4CAC0CC7BE").toLowerCase();
    }
        /**
	 * Turns array of bytes into string
	 *
	 * @param buf	Array of bytes to convert to hex string
	 * @return	Generated hex string
	 */
    public static String asHex(byte buf[]) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }

            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }

        return strbuf.toString();
    }
    /**
     * This method encrypts the message using AES-128 encryption.
     * @param msg Contains plain-text message.
     * @return Returns the AES-128 encrypted message.
     */
    public String encryptMsg(String plainTextMsg){
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encryptedBytes = cipher.doFinal(plainTextMsg.getBytes());
            String packedStr = JStringByteConverter.convertByteToStr(encryptedBytes);
            return packedStr;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
    /**
     * This method decrypts the message using AES-128 decryption.
     * @param msg Contains encrypted-text message.
     * @return Returns the AES-128 decrypted message.
     */
    public String decryptMsg(byte[] encryptedMsg)
    {
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedMsg);
            return new String(decryptedBytes);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /*public static void main(String []args){
        String a = new JDataEncrypter().encryptMsg("fuck off");
        System.out.println(new JDataEncrypter().decryptMsg(JStringByteConverter.convertStrToByte(a)));
    }*/

}
