package com.excellence.bluetoothlibrary.exception;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/9
 *     desc   : 蓝牙打开异常
 * </pre>
 */

public class BluetoothEnabledError extends BluetoothError {

    public BluetoothEnabledError() {
        super();
    }

    public BluetoothEnabledError(String message) {
        super(message);
    }

    public BluetoothEnabledError(String message, Throwable cause) {
        super(message, cause);
    }

    public BluetoothEnabledError(Throwable cause) {
        super(cause);
    }
}
