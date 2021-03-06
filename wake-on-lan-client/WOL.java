import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class WOL {
    /**
     * main方法，发送UDP广播，实现远程开机，目标计算机的MAC地址为：28D2443568A7
     */
    public static void main(String[] args) {
        String ip = "255.255.255.255";// 广播IP地址
        // String mac = "FCAA14D0C108";
        String mac = args[0];
        int port = 2304;// 端口号
        // 魔术包数据
        String magicPackage = "0xFFFFFFFFFFFF";
        for (int i = 0; i < 16; i++) {
            magicPackage += mac;
        }
        // 转换为2进制的魔术包数据
        byte[] command = hexToBinary(magicPackage);
        for (Byte b : command) {
            System.out.print(b.byteValue());
        }

        // 广播魔术包
        try {
            // 1.获取ip地址
            InetAddress address = InetAddress.getByName(ip);
            // 2.获取广播socket
            MulticastSocket socket = new MulticastSocket(port);
            // 3.封装数据包
            /*
             * public DatagramPacket(byte[] buf,int length ,InetAddress address ,int port)
             * buf：缓存的命令 length：每次发送的数据字节数，该值必须小于等于buf的大小 address：广播地址 port：广播端口
             */
            DatagramPacket packet = new DatagramPacket(command, command.length - 1, address, port);
            // 4.发送数据
            socket.send(packet);
            // 5.关闭socket
            socket.close();
        } catch (UnknownHostException e) {
            // Ip地址错误时候抛出的异常
            e.printStackTrace();
        } catch (IOException e) {
            // 获取socket失败时候抛出的异常
            e.printStackTrace();
        }
    }

    /**
     * 将16进制字符串转换为用byte数组表示的二进制形式
     * 
     * @param hexString：16进制字符串
     * @return：用byte数组表示的十六进制数
     */
    private static byte[] hexToBinary(String hexString) {
        // 1.定义变量：用于存储转换结果的数组
        byte[] result = new byte[hexString.length() / 2];

        // 2.去除字符串中的16进制标识"0X"并将所有字母转换为大写
        hexString = hexString.toUpperCase().replace("0X", "");

        // 3.开始转换
        // 3.1.定义两个临时存储数据的变量
        char tmp1 = '0';
        char tmp2 = '0';
        // 3.2.开始转换，将每两个十六进制数放进一个byte变量中
        for (int i = 0; i < hexString.length(); i += 2) {
            tmp1 = hexString.charAt(i);
            tmp2 = hexString.charAt(i + 1);
            result[i / 2] = (byte) ((hexToDec(tmp1) << 4) | (hexToDec(tmp2)));
        }
        return result;
    }

    /**
     * 用于将16进制的单个字符映射到10进制的方法
     * 
     * @param c：16进制数的一个字符
     * @return：对应的十进制数
     */
    private static byte hexToDec(char c) {
        int index = "0123456789ABCDEF".indexOf(c);
        return (byte) index;
    }
}