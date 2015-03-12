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

package org.holylobster.nuntius;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Handler;

public class NotificationListenerService extends android.service.notification.NotificationListenerService {

    private final String TAG = this.getClass().getSimpleName();

    private static boolean isNotificationAccessEnabled = false;

    public static Server server;

    @Override
    public IBinder onBind(Intent mIntent) {
        IBinder mIBinder = super.onBind(mIntent);
        Log.i(TAG, "onBind");
        isNotificationAccessEnabled = true;
        server = new Server(this);
        server.start();
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent mIntent) {
        boolean mOnUnbind = super.onUnbind(mIntent);
        Log.i(TAG, "onUnbind");
        isNotificationAccessEnabled = false;
        server.stop();
        server = null;
        return mOnUnbind;
    }

    public static boolean isNotificationAccessEnabled() {
        return isNotificationAccessEnabled;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        Log.i(TAG, "Notification from " + sbn.getPackageName() + ", prio=" + notification.priority);
        server.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "Notification from " + sbn.getPackageName() + " removed");
        server.onNotificationRemoved(sbn);
    }
}
