package pers.zeqinlin.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字节流中识别序列化与反序列化器
 */
@Getter
@AllArgsConstructor
public enum SerializerCode {

    JSON(1);

    private final int code;
}
