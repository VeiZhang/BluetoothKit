package com.excellence.bluetoothlibrary.exception;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2018/1/10
 *     desc   : 蓝牙搜索异常:扫描时关闭了蓝牙
 * </pre>
 */

public class BluetoothScanError extends BluetoothError {

    public BluetoothScanError() {
        super();
    }

    public BluetoothScanError(String message) {
        super(message);
    }

    public BluetoothScanError(String message, Throwable cause) {
        super(message, cause);
    }

    public BluetoothScanError(Throwable cause) {
        super(cause);
    }
}
