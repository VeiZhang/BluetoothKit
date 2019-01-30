package com.excellence.bluetoothlibrary.exception;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/2/9
 *     desc   : 蓝牙支持异常：经典蓝牙不支持、低功耗蓝牙不支持
 * </pre>
 */

public class BluetoothSupportError extends BluetoothError {

    public BluetoothSupportError() {
        super();
    }

    public BluetoothSupportError(String message) {
        super(message);
    }

    public BluetoothSupportError(String message, Throwable cause) {
        super(message, cause);
    }

    public BluetoothSupportError(Throwable cause) {
        super(cause);
    }
}
