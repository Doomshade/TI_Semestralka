package martin;

public class FMSStation {
    final int VAR_COUNT = 15;
    /**
     * pole pomocných stavových proměnných
     * pořadí proměnných:
     * Seg1-Seg3 [0-2], S1-S7[3-9], V1-V5[10-14]
     */
    public boolean[] variables;

    final int STATE_COUNT = 9;
    public int currentState = 0;

    public FMSStation() {
        initFMSVar();
    }

    /* Segmenty */
    /* ----------- */

    /**
     * Obsaď Seg1
     */
    public void do_s1() {
        variables[0] = false;
    }

    /**
     * Obsaď Seg2
     */
    public void do_s2() {
        variables[1] = false;
        variables[12] = true;
        variables[11] = !variables[2];
    }

    /**
     * Obsaď Seg3
     */
    public void do_s3() {
        variables[2] = false;
        variables[11] = true;
        variables[12] = !variables[1];
    }

    /* ----------- */

    /* kolej 1 vlak zleva */
    /* ----------- */

    public void do_v1KL() {
        variables[0] = true;
        variables[10] = false;
        variables[13] = false;
        variables[14] = false;
    }

    public void do_v1KLP() {
        variables[0] = true;
        variables[10] = true;
        variables[13] = false;
        turnOffSegLights();
    }

    public void do_ch1KL() {
        variables[11] = variables[1];
        variables[4] = variables[1] || variables[2];
        variables[5] = false;
        variables[6] = false;
        variables[7] = false;
        variables[1] = false;
        variables[2] = variables[2] && variables[11];
    }

    public void o1KL() {
        variables[1] = variables[11];
        variables[2] = variables[2] && variables[11];
        variables[4] = false;
        variables[10] = true;
        variables[14] = true;
        turnOnSegLights();
    }

    /* ----------- */

    // Seg1-Seg3 [0-2], S1-S7[3-9], V1-V5[10-14]

    /* kolej 2 vlak zleva */
    /* ----------- */

    public void v2KL() {
        variables[0] = true;
        variables[10] = true;
        variables[13] = true;
        variables[14] = false;
        variables[3] = false;
        variables[6] = false;
        variables[7] = false;
    }

    public void ch2KL() {
        variables[12] = variables[2];
        variables[5] = variables[1] || variables[2];
        variables[4] = false;
        variables[6] = false;
        variables[7] = false;
        variables[2] = false;
        variables[1] = variables[1] && variables[12];
    }

    public void o2KL() {
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

    public void o2KLP() {
        variables[2] = variables[12];
        variables[1] = variables[1] && variables[12];
        variables[5] = false;
        variables[10] = true;
        variables[13] = true;
        variables[14] = false;
        turnOnSegLights();
    }

    /* ----------- */

    // Seg1-Seg3 [0-2], S1-S7[3-9], V1-V5[10-14]

    /* kolej 1 vlak doprava */
    /* ----------- */

    public void do_v1KPS2() {
        variables[1] = true;

        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
        variables[12] = false;
        variables[11] = true;
    }

    public void do_v1KPS3() {
        variables[2] = true;

        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
        variables[12] = true;
        variables[11] = false;
    }

    public void do_v1KPS2P() {
        variables[1] = true;

        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
        variables[12] = false;
        variables[11] = true;
        turnOffSegLights();
    }

    public void do_v1KPS3P() {
        variables[2] = true;

        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
        variables[12] = true;
        variables[11] = false;
        turnOffSegLights();
    }

    public void do_ch1KP() {
        variables[10] = variables[0];
        variables[8] = variables[0];
        variables[0] = false;
        variables[9] = false;
        variables[3] = false;
    }

    public void do_o1KP() {
        variables[0] = true;
        variables[8] = false;
        variables[10] = true;
        variables[13] = true;
        variables[14] = false;
        turnOnSegLights();
    }

    /* ----------- */

    // Seg1-Seg3 [0-2], S1-S7[3-9], V1-V5[10-14]

    /* kolej 2 vlak doprava */
    /* ----------- */

    public void do_v2KPS2() {
        variables[1] = true;
        variables[13] = true;
        variables[14] = false;
        variables[10] = true;
        turnOffSegLights();
    }

    public void do_v2KPS3() {
        variables[2] = true;
        variables[13] = true;
        variables[14] = false;
        variables[10] = true;
        turnOffSegLights();
    }

    public void do_ch2KP() {
        variables[10] = !(variables[0]);
        variables[9] = variables[0];
        variables[0] = false;
        variables[8] = false;
        variables[3] = false;
    }

    public void do_o2KP() {
        variables[0] = true;
        variables[9] = false;
        variables[10] = false;
        variables[13] = false;
        variables[14] = true;
        turnOnSegLights();
    }

    public void do_o2KPP() {
        variables[0] = true;
        variables[8] = true;
        variables[10] = true;
        variables[13] = true;
        variables[14] = false;
        turnOnSegLights();
    }

    /* ----------- */

    /* pomocné */
    private void turnOffSegLights() {
        variables[3] = false;
        variables[6] = false;
        variables[7] = false;
    }

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
