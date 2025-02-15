package frc.molib.dashboard;

import java.util.Vector;

import frc.molib.Console;

@SuppressWarnings({"rawtypes"})
public class DashboardManager {
    private static Vector<DashboardSelector> mSelectors = new Vector<DashboardSelector>();
    private static Thread mUpdateThread = new Thread() {
        //TODO: Determine if DashboardManager should automatically run in it's own thread
        @Override public void run() {
            Console.logMsg("Selector Manager: Started");
            while(true) DashboardManager.updateValues();
        }
    };

    public static void startInBackground() {
        if(!mUpdateThread.isAlive()) mUpdateThread.start();
    }

    public static void addSelector(DashboardSelector selector) { mSelectors.add(selector); }

    public static void removeSelector(DashboardSelector selector) { mSelectors.remove(selector); }

    public static void initSelectors() { for(DashboardSelector dshTemp : mSelectors) dshTemp.init(); }

    public static void removeAll() { mSelectors.clear(); }

    public static void updateValues() {
        for(DashboardSelector dshTemp : mSelectors) dshTemp.update();
    }
}
