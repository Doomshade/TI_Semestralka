package martin;

public class FMSController {

    public FMSStation station = new FMSStation();

    /**
     * Změn stav automatu
     *
     * @param signal vstupní signál
     * @return true -> pokud byl zadán validní signál, jinak false
     */
    public boolean switchState(FMSSignals signal) {

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

    /**
     * Prázdné nádraží nebo počáteční stav
     *
     * @param signal vstupní signál
     * @return true, pokud byl zadán validní vstupní signál, jinak false
     */
    private boolean switchEmpty(FMSSignals signal) {
        switch (signal) {
            case S1:
                station.doS1();
                break;
            case S2:
                station.doS2();
                break;
            case S3:
                station.doS3();
                break;
            case V1KL:
                station.doV1KL();
                station.currentState = FMSStates.PK_ZL;
                break;
            case V1KPS2:
                station.doV1KPS2();
                station.currentState = FMSStates.PK_ZP;
                break;
            case V1KPS3:
                station.doV1KPS3();
                station.currentState = FMSStates.PK_ZP;
                break;
            default:
                return false;
        }

        return true;
    }

    /**
     * 1. kolej obsazená zleva
     *
     * @param signal vstupní signál
     * @return true, pokud byl zadán validní vstupní signál, jinak false
     */
    private boolean switchPKZL(FMSSignals signal) {
        switch (signal) {
            case S1:
                station.doS1();
                break;
            case S2:
                station.doS2();
                break;
            case S3:
                station.doS3();
                break;
            case CH1KL:
                station.doCH1KL();
                break;
            case O1KL:
                station.doO1KL();
                station.currentState = FMSStates.EMPTY;
                break;
            case V2KL:
                station.doV2KL();
                station.currentState = FMSStates.PK_ZL_DK_ZL;
                break;
            case V2KPS2:
                station.doV2KPS2();
                station.currentState = FMSStates.PK_ZL_DK_ZP;
                break;
            case V2KPS3:
                station.doV2KPS3();
                station.currentState = FMSStates.PK_ZL_DK_ZP;
                break;
            default:
                return false;
        }

        return true;
    }

    /**
     * 1. kolej obsazená zleva 2. kolej obsazená zleva
     *
     * @param signal vstupní signál
     * @return true, pokud byl zadán validní vstupní signál, jinak false
     */
    private boolean switchPKZLDKZL(FMSSignals signal) {
        switch (signal) {
            case S1:
                station.doS1();
                break;
            case S2:
                station.doS2();
                break;
            case S3:
                station.doS3();
                break;
            case CH1KL:
                station.doCH1KL();
                break;
            case CH2KL:
                station.doCH2KL();
                break;
            case O2KL:
                station.doO2KL();
                station.currentState = FMSStates.PK_ZL;
                break;
            case O1KL:
                station.doO1KL();
                station.currentState = FMSStates.DK_ZL;
                break;
            default:
                return false;
        }

        return true;
    }

    /**
     * Obsazená 2. kolej zleva
     *
     * @param signal vstupní signál
     * @return true, pokud byl zadán validní vstupní signál, jinak false
     */
    private boolean switchDKZL(FMSSignals signal) {
        switch (signal) {
            case S1:
                station.doS1();
                break;
            case S2:
                station.doS2();
                break;
            case S3:
                station.doS3();
                break;
            case CH2KL:
                station.doCH2KL();
                break;
            case V1KLP:
                station.doV1KLP();
                station.currentState = FMSStates.PK_ZL_DK_ZL;
                break;
            case O2KLP:
                station.doO2KLP();
                station.currentState = FMSStates.EMPTY;
            case V1KPS2P:
                station.doV1KPS2P();
                station.currentState = FMSStates.PK_ZP_DK_ZL;
            case V1KPS3P:
                station.doV1KPS3P();
                station.currentState = FMSStates.PK_ZP_DK_ZL;
            default:
                return false;
        }
        return true;
    }

    /**
     * Obsazená 1. kolej zprava 2. kolej zleva
     *
     * @param signal vstupní signál
     * @return true, pokud byl zadán validní vstupní signál, jinak false
     */
    private boolean switchPKZPDKZL(FMSSignals signal) {
        switch (signal) {
            case S1:
                station.doS1();
                break;
            case S2:
                station.doS2();
                break;
            case S3:
                station.doS3();
                break;
            case CH2KL:
                station.doCH2KL();
                break;
            case CH1KP:
                station.doCH1KP();
                break;
            case O2KL:
                station.doO2KLP();
                station.currentState = FMSStates.PK_ZP;
                break;
            case O1KP:
                station.doO1KP();
                station.currentState = FMSStates.DK_ZL;
            default:
                return false;
        }

        return true;
    }

    /**
     * Obsazená 1. kolej zleva 2. kolej zprava
     *
     * @param signal vstupní signál
     * @return true, pokud byl zadán validní vstupní signál, jinak false
     */
    private boolean switchPKZLDKZP(FMSSignals signal) {
        switch (signal) {
            case S1:
                station.doS1();
                break;
            case S2:
                station.doS2();
                break;
            case S3:
                station.doS3();
                break;
            case CH1KL:
                station.doCH1KL();
                break;
            case CH2KP:
                station.doCH2KP();
                break;
            case O2KP:
                station.doO2KP();
                station.currentState = FMSStates.PK_ZL;
                break;
            case O1KL:
                station.doO1KL();
                station.currentState = FMSStates.DK_ZP;
            default:
                return false;
        }

        return true;
    }

    /**
     * Obsazená 2. kolej zprava
     *
     * @param signal vstupní signál
     * @return true, pokud byl zadán validní vstupní signál, jinak false
     */
    private boolean switchDKZP(FMSSignals signal) {
        switch (signal) {
            case S1:
                station.doS1();
                break;
            case S2:
                station.doS2();
                break;
            case S3:
                station.doS3();
                break;
            case CH2KP:
                station.doCH2KP();
                break;
            case V1KLP:
                station.doV1KLP();
                station.currentState = FMSStates.PK_ZL_DK_ZP;
                break;
            case O2KPP:
                station.doO2KPP();
                station.currentState = FMSStates.EMPTY;
                break;
            case V1KPS2P:
                station.doV1KPS2P();
                station.currentState = FMSStates.PK_ZP_DK_ZP;
                break;
            case V1KPS3P:
                station.doV1KPS3P();
                station.currentState = FMSStates.PK_ZP_DK_ZP;
                break;
            default:
                return false;
        }

        return true;
    }

    /**
     * Obsazená 1. kolej zprava a 2. kolej zprava
     *
     * @param signal vstupní signál
     * @return true, pokud byl zadán validní vstupní signál, jinak false
     */
    private boolean switchPKZPDKZP(FMSSignals signal) {
        switch (signal) {
            case S2:
                station.doS2();
                break;
            case S3:
                station.doS3();
                break;
            case CH1KP:
                station.doCH1KP();
                break;
            case CH2KP:
                station.doCH2KP();
                break;
            case O1KP:
                station.doO1KP();
                station.currentState = FMSStates.DK_ZP;
                break;
            case O2KP:
                station.doO2KP();
                station.currentState = FMSStates.PK_ZP;
                break;
            default:
                return false;
        }

        return true;
    }

    /**
     * Obsazená 1. kolej zprava
     *
     * @param signal vstupní signál
     * @return true, pokud byl zadán validní vstupní signál, jinak false
     */
    private boolean switchPKZP(FMSSignals signal) {
        switch (signal) {
            case S1:
                station.doS1();
                break;
            case S2:
                station.doS2();
                break;
            case S3:
                station.doS3();
                break;
            case CH1KP:
                station.doCH1KP();
                break;
            case O1KP:
                station.doO1KP();
                station.currentState = FMSStates.EMPTY;
                break;
            case V2KL:
                station.doV2KL();
                station.currentState = FMSStates.PK_ZP_DK_ZL;
                break;
            case V2KPS2:
                station.doV2KPS2();
                station.currentState = FMSStates.PK_ZP_DK_ZP;
                break;
            case V2KPS3:
                station.doV2KPS3();
                station.currentState = FMSStates.PK_ZP_DK_ZP;
                break;
            default:
                return false;
        }

        return true;
    }
}
