package pers.zeqinlin.rpc.transport.socket.util;

import pers.zeqinlin.rpc.entity.RpcRequest;
import pers.zeqinlin.rpc.enumeration.PackageType;
import pers.zeqinlin.rpc.serializer.CommonSerializer;


import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author ziyang
 */
public class ObjectWriter {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    public static void writeObject(OutputStream outputStream, Object object, CommonSerializer serializer) throws IOException {

        outputStream.write(intToBytes(MAGIC_NUMBER));
        if (object instanceof RpcRequest) {
            outputStream.write(intToBytes(PackageType.REQUEST_PACKAGE.getCode()));
        } else {
            outputStream.write(intToBytes(PackageType.RESPONSE_PACKAGE.getCode()));
        }
        outputStream.write(intToBytes(serializer.getCode()));
        byte[] bytes = serializer.serialize(object);
        outputStream.write(intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();

    }

    private static byte[] intToBytes(int value) {
        byte[] des = new byte[4];
        des[3] =  (byte) ((value>>24) & 0xFF);
        des[2] =  (byte) ((value>>16) & 0xFF);
        des[1] =  (byte) ((value>>8) & 0xFF);
        des[0] =  (byte) (value & 0xFF);
        return des;
    }
}