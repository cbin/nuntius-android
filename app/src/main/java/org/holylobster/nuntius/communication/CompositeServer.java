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
import android.service.notification.StatusBarNotification;

public class CompositeServer implements Server {
    final BluetoothServer bluetoothServer;
    final NetworkServer networkServer;

    public CompositeServer(Context context) {
        bluetoothServer = new BluetoothServer(context);
        networkServer = new NetworkServer(context);
    }


    @Override
    public void start() {
        bluetoothServer.start();
        networkServer.start();
    }

    @Override
    public void stop() {
        bluetoothServer.stop();
        networkServer.stop();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        bluetoothServer.onNotificationPosted(sbn);
        networkServer.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        bluetoothServer.onNotificationRemoved(sbn);
        networkServer.onNotificationRemoved(sbn);
    }

    @Override
    public String getStatusMessage() {
        return bluetoothServer.getStatusMessage();
    }

    @Override
    public int getNumberOfConnections() {
        return bluetoothServer.getNumberOfConnections() + networkServer.getNumberOfConnections();
    }
}
