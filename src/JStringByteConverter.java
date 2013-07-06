//package src;


import java.util.StringTokenizer;


/**
 *
 * @author Mrc0d3r
 */
public class JStringByteConverter {

    public static byte[] convertStrToByte(String encMsg)
    {
        StringTokenizer st = new StringTokenizer(encMsg, ":");
        byte[] encBytes = new byte[st.countTokens()];
        for(int i = 0;st.hasMoreTokens() ; i++)
            encBytes[i] = Byte.parseByte(st.nextToken());
        return encBytes;
    }

    public static String convertByteToStr(byte[] encMsg)
    {
        String del = ":", packedStr = "";
        for(int i = 0; i < encMsg.length ; i++)
            packedStr = packedStr + encMsg[i] + del;
        return packedStr;
    }
}
