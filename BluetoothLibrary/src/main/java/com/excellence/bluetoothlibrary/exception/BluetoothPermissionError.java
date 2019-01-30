package com.excellence.bluetoothlibrary.exception;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/9
 *     desc   : 蓝牙权限异常
 * </pre>
 */

public class BluetoothPermissionError extends BluetoothError {

    public BluetoothPermissionError() {
        super();
    }

    public BluetoothPermissionError(String message) {
        super(message);
    }

    public BluetoothPermissionError(String message, Throwable cause) {
        super(message, cause);
    }

    public BluetoothPermissionError(Throwable cause) {
        super(cause);
    }
}
