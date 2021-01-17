package martin;

public class FMSController {

    public FMSStation station = new FMSStation();

    /**
     * Změn stav automatu
     *
     * @param signal vstupní signál
     * @return true -> pokud byl zadán validní signál, jinak false
     */
    public boolean switchState(String signal) {

        switch (station.currentState) {
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
        }
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
