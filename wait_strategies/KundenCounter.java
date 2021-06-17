import java.util.ArrayList;

public class KundenCounter {

    public int[] kundenCounterList;
    private static KundenCounter instance = null;

    private KundenCounter(){
        kundenCounterList = new int[WaitStrategiesExperiments.SIMULATION_TIME];
    }

    public static KundenCounter getInstance(){
        if(instance == null)
            instance = new KundenCounter();

        return instance;
    }

    public static void resetInstance(){
        instance = null;
    }
}
