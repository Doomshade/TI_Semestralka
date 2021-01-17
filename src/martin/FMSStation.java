package martin;

public class FMSStation {

    final int VAR_COUNT = 15;
    /**
     * pole pomocných stavových proměnných
     * pořadí proměnných:
     * Seg1-Seg3 [0-2], S1-S7[3-9], V1-V5[10-14]
     */
    public boolean[] variables;

    /**
     * Stav ve kterém se automat nachází
     */
    public FMSStates currentState = FMSStates.EMPTY;

    public FMSStation() {
        initFMSVar();
    }

    /* Segmenty */

    /**
     * Obsaď Seg1
     */
    public void doS1() {
        variables[0] = false;
    }

    /**
     * Obsaď Seg2
     */
    public void doS2() {
        variables[1] = false;
        variables[12] = true;
        variables[11] = !variables[2];
    }

    /**
     * Obsaď Seg3
     */
    public void doS3() {
        variables[2] = false;
        variables[11] = true;
        variables[12] = !variables[1];
    }

    /* kolej 1 vlak zleva */

    /**
     * Vlak vjede zleva na 1. kolej (2. kolej je prázdná)
     */
    public void doV1KL() {
        variables[0] = true;
        variables[10] = false;
        variables[13] = false;
        variables[14] = false;
    }

    /**
     * Vlak vjede zleva na 1. kolej (2. kolej je obsazená)
     */
    public void doV1KLP() {
        variables[0] = true;
        variables[10] = true;
        variables[13] = false;
        turnOffSegLights();
    }

    /**
     * Vlak, který přijel zleva na 1. kolej chce odjet
     */
    public void doCH1KL() {
        variables[11] = variables[1];
        variables[4] = variables[1] || variables[2];
        variables[5] = false;
        variables[6] = false;
        variables[7] = false;
        variables[1] = false;
        variables[2] = variables[2] && variables[11];
    }

    /**
     * Vlak odjede doprava z nádraží
     */
    public void doO1KL() {
        variables[1] = variables[11];
        variables[2] = variables[2] && variables[11];
        variables[4] = false;
        variables[10] = true;
        variables[14] = true;
        turnOnSegLights();
    }

    /* kolej 2 vlak zleva */

    /**
     * Vlak vjede na 2. kolej zleva
     */
    public void doV2KL() {
        variables[0] = true;
        variables[10] = true;
        variables[13] = true;
        variables[14] = false;
        variables[3] = false;
        variables[6] = false;
        variables[7] = false;
    }

    /**
     * Vlak chce odjet z 2. koleje doprava
     */
    public void doCH2KL() {
        variables[12] = variables[2];
        variables[5] = variables[1] || variables[2];
        variables[4] = false;
        variables[6] = false;
        variables[7] = false;
        variables[2] = false;
        variables[1] = variables[1] && variables[12];
    }

    /**
     * Vlak odjede z 2. koleje doprava
     */
    public void doO2KL() {
        variables[2] = variables[12];
        variables[1] = variables[1] && variables[12];
        variables[5] = false;
        variables[3] = true;
        variables[5] = true;
        variables[6] = true;
        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
    }

    /**
     * Vlak odjede z 2. koleje doprava a nádraží bude prázdné
     */
    public void doO2KLP() {
        variables[2] = variables[12];
        variables[1] = variables[1] && variables[12];
        variables[5] = false;
        variables[10] = true;
        variables[13] = true;
        variables[14] = false;
        turnOnSegLights();
    }

    /* kolej 1 vlak doprava */

    /**
     * Vlak přijede na 1. kolej zprava ze segmentu 2
     */
    public void doV1KPS2() {
        variables[1] = true;

        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
        variables[12] = false;
        variables[11] = true;
    }

    /**
     * Vlak přijede na 1. kolej zprava ze segmentu 3
     */
    public void doV1KPS3() {
        variables[2] = true;

        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
        variables[12] = true;
        variables[11] = false;
    }

    /**
     * Vlak přijede na 1. kolej zprava ze segmentu 2 a nádraží je plné
     */
    public void doV1KPS2P() {
        variables[1] = true;

        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
        variables[12] = false;
        variables[11] = true;
        turnOffSegLights();
    }

    /**
     * Vlak přijede na 1. kolej zprava ze segmentu 3 a nádraží je plné
     */
    public void doV1KPS3P() {
        variables[2] = true;

        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
        variables[12] = true;
        variables[11] = false;
        turnOffSegLights();
    }

    /**
     * Vlak, který přijel na 1.kolej zprava chce odjet z nádraží
     */
    public void doCH1KP() {
        variables[10] = variables[0];
        variables[8] = variables[0];
        variables[0] = false;
        variables[9] = false;
        variables[3] = false;
    }

    /**
     * Vlak odjel z 1. koleje doleva
     */
    public void doO1KP() {
        variables[0] = true;
        variables[8] = false;
        variables[10] = true;
        variables[13] = true;
        variables[14] = false;
        turnOnSegLights();
    }

    /* kolej 2 vlak doprava */

    /**
     * Vlak přijel na 2. kolej zprava ze segmentu 2
     */
    public void doV2KPS2() {
        variables[1] = true;
        variables[13] = true;
        variables[14] = false;
        variables[10] = true;
        turnOffSegLights();
    }

    /**
     * Vlak přijel na 2. kolej zprava ze segmentu 3
     */
    public void doV2KPS3() {
        variables[2] = true;
        variables[13] = true;
        variables[14] = false;
        variables[10] = true;
        turnOffSegLights();
    }

    /**
     * Vlak chce odjet z 2. koleje doleva
     */
    public void doCH2KP() {
        variables[10] = !(variables[0]);
        variables[9] = variables[0];
        variables[0] = false;
        variables[8] = false;
        variables[3] = false;
    }

    /**
     * Vlak odjel z 2. koleje doleva
     */
    public void doO2KP() {
        variables[0] = true;
        variables[9] = false;
        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
        turnOnSegLights();
    }

    /**
     * Vlak odjel z 2. koleje doleva a nádraží je prázdné
     */
    public void doO2KPP() {
        variables[0] = true;
        variables[8] = true;
        variables[10] = true;
        variables[13] = true;
        variables[14] = false;
        turnOnSegLights();
    }

    /* pomocné */

    /**
     * Vypne příjezdová světla
     */
    private void turnOffSegLights() {
        variables[3] = false;
        variables[6] = false;
        variables[7] = false;
    }

    /**
     * Zapne příjezdová světla
     */
    private void turnOnSegLights() {
        variables[3] = true;
        variables[6] = true;
        variables[7] = true;
    }

    /* nainicializuj pomocné stavové proměnné*/
    private void initFMSVar() {
        variables = new boolean[VAR_COUNT];
        // Seg1 - Seg3 == true
        variables[0] = true;
        variables[1] = true;
        variables[2] = true;
        // S1, S4, S5 == true
        variables[3] = true;
        variables[6] = true;
        variables[7] = true;
        // V1 - V4 == true
        variables[13] = true;
        variables[12] = true;
        variables[11] = true;
        variables[10] = true;
    }
}
