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

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class NetworkServer extends AbstractServer {

    private static final String TAG = NetworkServer.class.getSimpleName();

    private ServerSocket serverSocket;

    NetworkServer(Context context) {
        super(context);
    }

    void setupServerSocket() throws IOException {
        serverSocket = new ServerSocket(12233);
        Log.d(TAG, "Server socket created");
    }

    NuntiusSocket accept() throws IOException {
        Socket socket = serverSocket.accept();
        Log.d(TAG, ">>Accept Client Request from: " + socket.getInetAddress().getHostAddress());
        return new NetworkSocketAdapter(socket);
    }

    boolean periodicCheck() {
        return serverSocket != null && serverSocket.isBound() && !serverSocket.isClosed();
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

    @Override
    public String getStatusMessage() {
        return "...";
    }
}
