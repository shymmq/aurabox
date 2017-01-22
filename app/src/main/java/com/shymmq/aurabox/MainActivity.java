package com.shymmq.aurabox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

public class MainActivity extends AppCompatActivity {
    private SmoothBluetooth smoothBluetooth;
    private String TAG = "aurabox-debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button sendButton = (Button) findViewById(R.id.button);
        SmoothBluetooth.Listener listener = new SmoothBluetooth.Listener() {
            @Override
            public void onBluetoothNotSupported() {

            }

            @Override
            public void onBluetoothNotEnabled() {

            }

            @Override
            public void onConnecting(Device device) {

            }

            @Override
            public void onConnected(Device device) {
                Log.d(TAG, device.getName() + " connected");
                sendButton.setText(device.getName());
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, smoothBluetooth.isConnected() ? "Connected" : "Not connected");
                        byte[] data = hexStringToByteArray("2134560721426375104234560721346375104263560721345675104263750721345607104263751021345607214263751042");
                        smoothBluetooth.send(
                                addGuards(escape(addChecksum(addPreamble(data)))),
                                false);
                    }
                });
            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onConnectionFailed(Device device) {

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
        };
        smoothBluetooth = new SmoothBluetooth(this, SmoothBluetooth.ConnectionTo.OTHER_DEVICE, SmoothBluetooth.Connection.INSECURE, listener);
        smoothBluetooth.tryConnection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smoothBluetooth.stop();
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
            if (b >= 0x01 && b < 0x03) {
                result[i] = 0x03;
                result[i + 1] = (byte) (0x01 + b);
                i++;
            } else {
                result[i] = b;
            }
            i++;
        }
        return result;
    }
}
