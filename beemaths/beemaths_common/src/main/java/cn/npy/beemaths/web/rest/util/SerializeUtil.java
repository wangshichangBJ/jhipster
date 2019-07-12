package cn.npy.beemaths.web.rest.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
    //序列化
    public static String serializeToString(Object obj) throws Exception {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(obj);
        String str = byteOut.toString("ISO-8859-1");//此处只能是ISO-8859-1,
        return str;
    }

    //反序列化
    public static Object deserializeToObject(String str) throws Exception {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        Object obj = objIn.readObject();
        return obj;
    }
}
