package com.shymmq.aurabox;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

/**
 * Created by szyme on 22.01.2017.
 */

public class AuraboxService implements SmoothBluetooth.Listener {
    private static final String TAG = "aurabox-debug";
    Context context;
    SmoothBluetooth smoothBluetooth;
    Runnable onConnected;

    @Override
    public void onBluetoothNotSupported() {

    }

    @Override
    public void onBluetoothNotEnabled() {

    }

    @Override
    public void onConnecting(Device device) {
        Log.d(TAG, device.getName() + " connecting...");

    }

    @Override
    public void onConnected(Device device) {
        Log.d(TAG, device.getName() + " connected");
        onConnected.run();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(Device device) {
        Log.d(TAG, device.getName() + " cennection failed");
    }

    @Override
    public void onDiscoveryStarted() {

    }

    @Override
    public void onDiscoveryFinished() {

    }

    @Override
    public void onNoDevicesFound() {
        Log.d(TAG, "No devices paierd");
    }

    @Override
    public void onDevicesFound(final List<Device> deviceList, SmoothBluetooth.ConnectionCallback connectionCallback) {
        Log.d(TAG, "Paired devices: ");
        for (Device device : deviceList) {
            Log.d(TAG, "\t" + device.getName());
        }
        Device device = deviceList.get(0);
        connectionCallback.connectTo(device);

    }

    @Override
    public void onDataReceived(int data) {

    }

    public AuraboxService(Context context) {
        this.context = context;
    }

    public void connect(Runnable r) {
        this.onConnected = r;
        smoothBluetooth = new SmoothBluetooth(context, SmoothBluetooth.ConnectionTo.OTHER_DEVICE, SmoothBluetooth.Connection.INSECURE, this);
        smoothBluetooth.tryConnection();
//        sendButton.setText(device.getName());
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, smoothBluetooth.isConnected() ? "Connected" : "Not connected");
//                byte[] data = hexStringToByteArray("2134560721426375104234560721346375104263560721345675104263750721345607104263751021345607214263751042");
//                smoothBluetooth.send(
//                        addGuards(escape(addChecksum(addPreamble(data)))),
//                        false);
//            }
//        });
    }

    public void send(AuraboxBitmap bitmap) {
        Log.d(TAG,"Sending "+bitmap.toString());
        byte[] data = bitmap.toByteArray();
        data = addPreamble(data);
        data = addChecksum(data);
        data = escape(data);
        data = addGuards(data);
        smoothBluetooth.send(data);
    }

    private static byte[] addGuards(byte[] payload) {
        byte prefix = 0x01;
        byte suffix = 0x02;
        byte[] result = new byte[payload.length + 2];
        result[0] = prefix;
        result[result.length - 1] = suffix;
        System.arraycopy(payload, 0, result, 1, payload.length);
        return result;
    }

    private static byte[] addPreamble(byte[] payload) {
        byte[] preamble = hexStringToByteArray("390044000a0a04");
        byte[] res = new byte[preamble.length + payload.length];
        System.arraycopy(preamble, 0, res, 0, preamble.length);
        System.arraycopy(payload, 0, res, preamble.length, payload.length);
        return res;
    }

    private static byte[] addChecksum(byte[] payload) {
        byte[] result = Arrays.copyOf(payload, payload.length + 2);
        int sum = 0;
        for (byte b : payload) {
            sum += b;
        }
        result[result.length - 2] = (byte) (sum & 0xFF);
        result[result.length - 1] = (byte) ((sum >> 8) & 0xFF);
        return result;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static byte[] escape(byte[] payload) {
        int escapeCount = 0;
        for (byte b : payload) {
            if (b >= 0x01 && b <= 0x03) {
                escapeCount++;
            }
        }
        byte[] result = new byte[payload.length + escapeCount];
        int i = 0;
        for (byte b : payload) {
            if (b >= 0x01 && b <= 0x03) {
                result[i] = 0x03;
                result[i + 1] = (byte) (0x03 + b);
                i++;
            } else {
                result[i] = b;
            }
            i++;
        }
        return result;
    }
}
