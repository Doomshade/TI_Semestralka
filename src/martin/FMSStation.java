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
        variables[0] = false; // Seg1
    }

    /**
     * Obsaď Seg2
     */
    public void doS2() {
        variables[1] = false; // Seg1
        variables[12] = true; // V3
        variables[11] = !variables[2]; // V2
    }

    /**
     * Obsaď Seg3
     */
    public void doS3() {
        variables[2] = false; // Seg3
        variables[11] = true; // V2
        variables[12] = !variables[1]; // V3
    }

    /* kolej 1 vlak zleva */

    /**
     * Vlak vjede zleva na 1. kolej (2. kolej je prázdná)
     */
    public void doV1KL() {
        variables[0] = true; // Seg1
        variables[10] = false; // V1
        variables[13] = false; // V4
        variables[14] = true; // V5
    }

    /**
     * Vlak vjede zleva na 1. kolej (2. kolej je obsazená)
     */
    public void doV1KLP() {
        variables[0] = true; // Seg1
        variables[10] = true; // V1
        variables[13] = true; // V4
        variables[14] = false; // V5
        turnOffSegLights();
    }

    /**
     * Vlak, který přijel zleva na 1. kolej chce odjet
     */
    public void doCH1KL() {
        variables[11] = variables[1]; // V2

        variables[12] = variables[2]; // změna V3

        variables[4] = variables[1] || variables[2]; // S2
        variables[5] = false; // S3

        /*
        variables[6] = false; // S4
        variables[7] = false; // S5
         */

        variables[6] = !variables[4] && variables[3]; // S4
        variables[7] = !variables[4] && variables[3]; // S5

        variables[1] = false; // Seg2
        variables[2] = variables[2] && variables[11]; // Seg3
    }

    /**
     * Vlak odjede doprava z nádraží
     */
    public void doO1KL() {
        variables[1] = variables[11]; // Seg2
        // variables[2] = variables[2] && variables[11]; // Seg3
        variables[2] = variables[12];
        variables[4] = false; // S2
        variables[10] = true; // V1
        variables[13] = true; // V4
        variables[14] = false; // V5
        turnOnSegLights();
    }

    /* kolej 2 vlak zleva */

    /**
     * Vlak vjede na 2. kolej zleva
     */
    public void doV2KL() {
        variables[0] = true; // Seg1
        variables[10] = true; // V1
        variables[13] = true; // V4
        variables[14] = false; // V5
        turnOffSegLights();
    }

    /**
     * Vlak chce odjet z 2. koleje doprava
     */
    public void doCH2KL() {
        variables[12] = variables[2]; // V3

        variables[11] = variables[1]; // změna V2

        variables[5] = variables[1] || variables[2]; // S3
        variables[4] = false; // S2

        /*
        variables[6] = false; // S4 -- ZMĚNA
        variables[7] = false; // S5
         */

        variables[6] = !variables[5] && variables[3];
        ; // S4 -- ZMĚNA
        variables[7] = !variables[5] && variables[3];
        ; // S5

        variables[2] = false; // Seg3
        variables[1] = variables[1] && variables[12]; // Seg2
    }

    /**
     * Vlak odjede z 2. koleje doprava
     */
    public void doO2KL() {
        variables[2] = variables[12]; // Seg3
        // variables[1] = variables[1] && variables[12]; // Seg2
        variables[1] = variables[11]; // změna
        variables[5] = false; // S3
        variables[3] = true; // S1
        variables[6] = true; // S4
        variables[7] = true; // S5
        variables[10] = false; // V1
        variables[13] = false; // V4
        variables[14] = true; // V5
    }

    /**
     * Vlak odjede z 2. koleje doprava a nádraží bude prázdné
     */
    public void doO2KLP() {
        variables[2] = variables[12]; // Seg3
        variables[1] = variables[11]; // Seg2
        variables[5] = false; // S3
        variables[10] = true; // V1
        variables[13] = true; // V4
        variables[14] = false; // V5
        turnOnSegLights();
    }

    /* kolej 1 vlak doprava */

    /**
     * Vlak přijede na 1. kolej zprava ze segmentu 2
     */
    public void doV1KPS2() {
        variables[1] = true; // Seg2

        variables[10] = false; // V1
        variables[13] = false; // V4
        variables[14] = true; // V5
        variables[12] = false; // V3
        variables[11] = true; // V2
    }

    /**
     * Vlak přijede na 1. kolej zprava ze segmentu 3
     */
    public void doV1KPS3() {
        variables[2] = true; // Seg3

        variables[10] = false; // V1
        variables[13] = false; // V4
        variables[14] = true; // V5
        variables[12] = true; // V3
        variables[11] = false; // V2
    }

    /**
     * Vlak přijede na 1. kolej zprava ze segmentu 2 a nádraží je plné
     */
    public void doV1KPS2P() {
        variables[1] = true; // Seg2

        variables[10] = true; // V1
        variables[13] = true; // V4
        variables[14] = false; // V5
        variables[12] = false; // V3
        variables[11] = true; // V2
        turnOffSegLights();
    }

    /**
     * Vlak přijede na 1. kolej zprava ze segmentu 3 a nádraží je plné
     */
    public void doV1KPS3P() {
        variables[2] = true; // Seg3

        variables[10] = true; // V1
        variables[13] = true; // V4
        variables[14] = false; // V5
        variables[12] = true; // V3
        variables[11] = false; // V2
        turnOffSegLights();
    }

    /**
     * Vlak, který přijel na 1.kolej zprava chce odjet z nádraží
     */
    public void doCH1KP() {
        variables[10] = variables[0]; // V1
        variables[8] = variables[0]; // S6
        variables[0] = false; // Seg1
        // variables[9] = false; // S7
        variables[9] = false; // S7
        variables[3] = !variables[8] && variables[6]; // S1
    }

    /**
     * Vlak odjel z 1. koleje doleva
     */
    public void doO1KP() {
        variables[0] = true; // Seg1
        variables[8] = false; // S6
        variables[10] = true; // V1
        variables[13] = true; // V4
        variables[14] = false; // V5
        turnOnSegLights();
    }

    /* kolej 2 vlak doprava */

    /**
     * Vlak přijel na 2. kolej zprava ze segmentu 2
     */
    public void doV2KPS2() {
        variables[1] = true; // Seg2
        variables[13] = true; // V4
        variables[14] = false; // V5
        variables[10] = true; // V1
        turnOffSegLights();
    }

    /**
     * Vlak přijel na 2. kolej zprava ze segmentu 3
     */
    public void doV2KPS3() {
        variables[2] = true; // Seg3
        variables[13] = true; // V4
        variables[14] = false; // V5
        variables[10] = true; // V1
        turnOffSegLights();
    }

    /**
     * Vlak chce odjet z 2. koleje doleva
     */
    public void doCH2KP() {
        variables[10] = !(variables[0]); // V1
        variables[9] = variables[0]; // S7
        variables[0] = false; // Seg1
        // variables[8] = false; // S6
        variables[8] = false;
        variables[3] = !variables[9] && variables[6]; // S1
    }

    /**
     * Vlak odjel z 2. koleje doleva
     */
    public void doO2KP() {
        variables[0] = true; // Seg1
        variables[9] = false; // S7
        variables[10] = false; // V1
        variables[13] = false; // V4
        variables[14] = true; // V5
        turnOnSegLights();
    }

    /**
     * Vlak odjel z 2. koleje doleva a nádraží je prázdné
     */
    public void doO2KPP() {
        variables[0] = true; // Seg1
        variables[9] = false; // S7
        variables[10] = true; // V1
        variables[13] = true; // V4
        variables[14] = false; // V5
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
