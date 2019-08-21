package me.galaxy.manager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    private static final String PRIVATEKEY = "U2FsdGVkX19TL3Rpx1upy8FE3daryiZ3";

    /**
     * 加密方法
     *
     * @param source
     * @param hashType
     * @return
     */
    public static String getHash(String source, String hashType) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        String restr = "";
        try {
            MessageDigest md = MessageDigest.getInstance(hashType);
            md.update(source.getBytes());
            byte[] encryptStr = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = encryptStr[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            restr = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return restr;
    }

    /**
     * <p>Description:[md5加密]</p>
     * Created on 2017年8月22日
     *
     * @param bytes byte数组
     * @return String 数据经过加密后生成的字符串
     */
    public final static String md5(byte[] bytes) {
        final char[] md5String = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            // 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            // MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(bytes);

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) { // i = 0
                byte byte0 = md[i]; // 95
                str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
                str[k++] = md5String[byte0 & 0xf]; // F
            }

            // 返回经过加密后的字符串
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  static String encode(String val) throws UnsupportedEncodingException {
        // 使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
        byte[] bytes = val.getBytes("utf-8");
        return md5(bytes);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        String token=MD5Util.getHash(P2pConfiguration.PRIVATEKEY+"1234567890","MD5");
        String token = MD5Util.encode(PRIVATEKEY + "1234567890");
        System.out.println(token);
    }
}
