package martin;

import java.util.HashMap;

public class FMSController {

    private static final HashMap<FMSStates, FMSState> FMS_STATES = new HashMap<>() {
        {
            put(FMSStates.EMPTY, signal -> {
                // SOME LOGIC
                return true;
            });
            put(FMSStates.PK_ZP, signal -> true);
            put(FMSStates.PK_ZL, signal -> true);
            put(FMSStates.PK_ZL_DK_ZL, signal -> true);
            put(FMSStates.DK_ZL, signal -> true);
            put(FMSStates.PK_ZP_DK_ZL, signal -> true);
            put(FMSStates.PK_ZL_DK_ZP, signal -> true);
            put(FMSStates.DK_ZP, signal -> true);
            put(FMSStates.PK_ZP_DK_ZP, signal -> true);
        }
    };

    public FMSStation station = new FMSStation();

    /**
     * Změn stav automatu
     *
     * @param signal vstupní signál
     * @return true -> pokud byl zadán validní signál, jinak false
     */
    public boolean switchState(String signal) {

        return FMS_STATES.get(station.currentState).getState(signal);
        /*switch (station.currentState) {
            case EMPTY:
                return switchEmpty(signal);
            case PK_ZP:
                return switchPKZP(signal);
            case PK_ZL:
                return switchPKZL(signal);
            case PK_ZL_DK_ZL:
                return switchPKZLDKZL(signal);
            case DK_ZL:
                return switchDKZL(signal);
            case PK_ZP_DK_ZL:
                return switchPKZPDKZL(signal);
            case PK_ZL_DK_ZP:
                return switchPKZLDKZP(signal);
            case DK_ZP:
                return switchDKZP(signal);
            case PK_ZP_DK_ZP:
                return switchPKZPDKZP(signal);
            default:
                return false;
        }*/
    }

    private boolean switchEmpty(String signal) {

        return true;
    }

    private boolean switchPKZL(String signal) {

        return true;
    }

    private boolean switchPKZLDKZL(String signal) {

        return true;
    }

    private boolean switchDKZL(String signal) {

        return true;
    }

    private boolean switchPKZPDKZL(String signal) {

        return true;
    }

    private boolean switchPKZLDKZP(String signal) {

        return true;
    }

    private boolean switchDKZP(String signal) {

        return true;
    }

    private boolean switchPKZPDKZP(String signal) {

        return true;
    }

    private boolean switchPKZP(String signal) {

        return true;
    }
}
