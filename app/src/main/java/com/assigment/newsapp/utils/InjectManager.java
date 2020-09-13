package com.assigment.newsapp.utils;

import android.util.Log;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class InjectManager {

    private static final String TAG = "InjectManager";
    public static final int LAUNCH_DETAIL_SCREEN = -1;

    public interface InjectedEventNotifier {

        void onReceiveEvent(int eventType, Object object);
    }

    private static InjectManager mgr;

    private final ConcurrentHashMap<Integer, Vector<InjectedEventNotifier>> mapOfListenersBasedOnEventType = new ConcurrentHashMap<>();


    private InjectManager() {
        Log.e(TAG, "NEventsInjectManager initializing");
    }


    public static synchronized InjectManager getInstance() {
        if (mgr == null) {
            mgr = new InjectManager();
        }
        return mgr;
    }


    public void addListener(int type, InjectedEventNotifier listener) {
        if (listener == null) {
            Log.e(TAG, "Listener is null , so returning");
            return;
        }
        Vector<InjectedEventNotifier> listOfListeners = mapOfListenersBasedOnEventType.get(type);
        if (listOfListeners == null) {
            listOfListeners = new Vector<>();
            mapOfListenersBasedOnEventType.put(type, listOfListeners);
        }
        listOfListeners.add(listener);
    }

    public void removeListener(int type, InjectedEventNotifier listener) {
        if (listener == null) {
            Log.e(TAG, "Listener is null , so returning");
            return;
        }
        Vector<InjectedEventNotifier> listOfListeners = mapOfListenersBasedOnEventType.get(type);
        if (listOfListeners == null) {
            Log.e(TAG, "list of listeners is null so no need to worry, simply return");
            return;
        }
        listOfListeners.remove(listener);
    }


    public void inject(int type, Object object) {
        Log.e(TAG, "inject: " + type);
        Vector<InjectedEventNotifier> listOfListeners = mapOfListenersBasedOnEventType.get(type);
        if (listOfListeners == null) {
            Log.e(TAG, "No one is interested in injected Event : " + type);
            return;
        }
        for (InjectedEventNotifier listener : listOfListeners) {
            listener.onReceiveEvent(type, object);
        }
    }
}
