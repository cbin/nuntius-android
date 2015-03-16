/*
 * Copyright (C) 2015 - Holy Lobster
 *
 * Nuntius is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Nuntius is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nuntius. If not, see <http://www.gnu.org/licenses/>.
 */

package org.holylobster.nuntius.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import org.holylobster.nuntius.NotificationListenerService;

import java.io.IOException;
import java.util.UUID;

public final class BluetoothServer extends AbstractServer {

    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";

    private static final String TAG = BluetoothServer.class.getSimpleName();

    private static final UUID uuidSpp = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothServerSocket serverSocket;
    private final BluetoothAdapter btAdapter;

    BluetoothServer(Context context) {
        super(context);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static boolean bluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter() != null && BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    public static boolean bluetoothAvailable() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    public String getStatusMessage() {
        if (bluetoothEnabled() && getNumberOfConnections() == 0 ) {
            return "pair";
        } else if (isAlive()) {
            return  "connection";
        } else if (!NotificationListenerService.isNotificationAccessEnabled()) {
            return "notification";
        } else if (!bluetoothEnabled()) {
            return "bluetooth";
        } else {
            return "...";
        }
    }

    @Override
    NuntiusSocket accept() throws IOException {
        BluetoothSocket socket = serverSocket.accept();
        BluetoothDevice remoteDevice = socket.getRemoteDevice();
        Log.d(TAG, ">>Accept Client Request from: " + remoteDevice.getName() + "(" + remoteDevice.getAddress() + ")");
        return new BluetoothSocketAdapter(socket);
    }

    @Override
    boolean periodicCheck() {
        return serverSocket != null && btAdapter.isEnabled();
    }

    void setupServerSocket() throws IOException {
        serverSocket = btAdapter.listenUsingInsecureRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM, uuidSpp);
    }

    boolean startupChecks() {
        if (!bluetoothEnabled()) {
            Log.i(TAG, "Bluetooth not available or enabled. Cannot start server thread.");
            return false;
        }
        return true;
    }

    void shutdownServerSocket() {
        if (serverSocket != null) {
            Log.i(TAG, "Closing server listening socket...");
            try {
                serverSocket.close();
                Log.i(TAG, "Server listening socket closed.");
            } catch (IOException e) {
                Log.e(TAG, "Unable to close server socket", e);
            } finally {
                serverSocket = null;
            }
        }
    }
}
