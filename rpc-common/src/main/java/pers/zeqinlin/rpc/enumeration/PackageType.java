package pers.zeqinlin.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 标明这是一个调用请求还是调用响应
 */
@Getter
@AllArgsConstructor
public enum PackageType {

    REQUEST_PACKAGE(0),
    RESPONSE_PACKAGE(1);
    
    private final int code;
}
