import martin.FMSController;
import martin.FMSSignals;
import martin.FMSStates;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static martin.FMSStates.*;

public class MainFrame extends JPanel {

    private static final int W = 1222, H = 342;

    private static final int IMAGE_COUNT = 35;

    /*
    S1, S2, S3,
    V1KL, V1KLP, CH1KL, O1KL,
    V2KL, CH2KL, O2KL, O2KLP,
    V1KPS2, V1KPS3, V1KPS2P, V1KPS3P, CH1KP, O1KP,
    V2KPS2, V2KPS3, CH2KP, O2KP, O2KPP
    */

    private static final JButton[] BUTTONS = new JButton[(int) Arrays.stream(FMSSignals.values()).count()];

    private static final BufferedImage[] IMGS = new BufferedImage[IMAGE_COUNT];
    private static final boolean[] USED_IMGS = new boolean[IMAGE_COUNT];

    private static final String[] NAMES = {
            "Seg1", "Seg2", "Seg3",
            "Vlak L na kolej 1", "V1KLP", "CH1KL", "O1KL"
    };

    private FMSController controller;
    private JPanel koleje;

    public void showScreen() throws IOException {
        String[] fileNames = {
                "koleje_empty", "S1_C", "S1_Z", "S2_C",
                "S2_Z", "S3_C", "S3_Z", "S4_C", "S4_Z",
                "S5_C", "S5_Z", "S6_C", "S6_Z", "S7_C", "S7_Z",
                "Seg1_C", "Seg1_Z", "Seg2_C", "Seg2_Z", "Seg3_C", "Seg3_Z",
                "V1_F", "V1_T", "V2_F", "V2_T",
                "V3_F", "V3_T", "V4_F", "V4_T", "V5_F", "V5_T",
                "Z_Vlak1_jede_doleva", "Z_Vlak1_jede_doprava",
                "Z_Vlak2_jede_doleva", "Z_Vlak2_jede_doprava"
        };

        for (int i = 0; i < fileNames.length; ++i) {
            URL url = getClass().getResource("img/" + fileNames[i] + ".png");
            IMGS[i] = ImageIO.read(url);
        }

        JFrame frame = new JFrame("Semestrální práce TI - Jakubašek a Šmrha");
        this.setLayout(new BorderLayout());
        Panel p = new Panel();
        p.setLayout(new FlowLayout(FlowLayout.LEADING));

        this.controller = new FMSController();

        for (int i = 0; i < FMSSignals.values().length; i++) {
            String s;
            if (i >= NAMES.length) {
                s = FMSSignals.values()[i].toString();
            } else {
                s = NAMES[i];
            }
            BUTTONS[i] = createButton(i + ": " + FMSSignals.values()[i].toString(), FMSSignals.values()[i]);
            p.add(BUTTONS[i]);
        }

        // seg1-3 necháme enabled
        for (int i = 0; i < 3; ++i) {
            BUTTONS[i].setEnabled(true);
        }

        p.setPreferredSize(new Dimension(300, 100));
        add(p, BorderLayout.SOUTH);

        koleje = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < IMGS.length; ++i) {
                    if (!USED_IMGS[i]) continue;

                    BufferedImage img = IMGS[i];
                    int x = getWidth() - W;
                    if (x < 0) {
                        x = 0;
                    } else {
                        x /= 2;
                    }

                    int y = getHeight() - H;
                    if (y < 0) {
                        y = 0;
                    } else {
                        y /= 2;
                    }

                    g.drawImage(img, x, y, Math.min(W, getWidth()), Math.min(H, getHeight()), null);
                }
            }
        };

        add(koleje, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(900, 600));

        frame.add(this);

        frame.pack();
        frame.setVisible(true);

        resetStatesAndDraw();
    }

    private JButton createButton(String text, FMSSignals signal) {
        final JButton btn = new JButton(text);
        btn.setEnabled(true);
        btn.addActionListener(e -> {
            if (!MainFrame.this.controller.switchState(signal)) {
                System.out.println("Neplatný signál");
                return;
            }
            System.out.println("Volám signál " + signal.toString());
            updateButtons();
            koleje.repaint();
        });
        return btn;
    }

    /*
    0 - S1
    1 - S2
    2 - S3

    3 - V1KL
    4 - V1KLP
    5 - CH1KL
    6 - O1KL

    7 - V2KL
    8 - CH2KL
    9 - O2KL
    10 - O2KLP

    11 - V1KPS2
    12 - V1KPS3
    13 - V1KPS2P
    14 - V1KPS3P
    15 - CH1KP
    16 - O1KP

    17 - V2KPS2
    18 - V2KPS3
    19 - CH2KP
    20 - O2KP
    21 - O2KPP
    */

    private void resetStatesAndDraw() {

        // resetneme
        Arrays.fill(USED_IMGS, false);

        // koleje empty necháme
        USED_IMGS[0] = true;

        boolean[] vars = controller.station.variables;

        // Seg1
        USED_IMGS[15] = vars[0];
        USED_IMGS[16] = !vars[0];

        // Seg2
        USED_IMGS[17] = vars[1];
        USED_IMGS[18] = !vars[1];

        // Seg3
        USED_IMGS[19] = vars[2];
        USED_IMGS[20] = !vars[2];

        // S1-7
        for (int i = 3; i < 10; ++i) {
            int firstImg = mapSToImg(i);
            USED_IMGS[firstImg] = !vars[i];
            USED_IMGS[firstImg + 1] = vars[i];
        }

        // V1-5
        for (int i = 10; i < 15; ++i) {
            int firstImg = mapVToImg(i);
            USED_IMGS[firstImg] = !vars[i];
            USED_IMGS[firstImg + 1] = vars[i];
        }

        int k1_ZP = 31;
        int k1_ZL = k1_ZP + 1;
        int k2_ZP = k1_ZL + 1;
        int k2_ZL = k2_ZP + 1;
        switch (controller.station.currentState) {
            case EMPTY:
                USED_IMGS[k1_ZP] = false;
                USED_IMGS[k1_ZL] = false;
                USED_IMGS[k2_ZP] = false;
                USED_IMGS[k2_ZL] = false;
                break;
            case PK_ZL:
                USED_IMGS[k1_ZP] = false;
                USED_IMGS[k1_ZL] = true;
                USED_IMGS[k2_ZP] = false;
                USED_IMGS[k2_ZL] = false;
                break;
            case PK_ZP:
                USED_IMGS[k1_ZP] = true;
                USED_IMGS[k1_ZL] = false;
                USED_IMGS[k2_ZP] = false;
                USED_IMGS[k2_ZL] = false;
                break;
            case PK_ZL_DK_ZL:
                USED_IMGS[k1_ZP] = false;
                USED_IMGS[k1_ZL] = true;
                USED_IMGS[k2_ZP] = false;
                USED_IMGS[k2_ZL] = true;
                break;
            case DK_ZL:
                USED_IMGS[k1_ZP] = false;
                USED_IMGS[k1_ZL] = false;
                USED_IMGS[k2_ZP] = false;
                USED_IMGS[k2_ZL] = true;
                break;
            case PK_ZP_DK_ZL:
                USED_IMGS[k1_ZP] = true;
                USED_IMGS[k1_ZL] = false;
                USED_IMGS[k2_ZP] = false;
                USED_IMGS[k2_ZL] = true;
                break;
            case PK_ZL_DK_ZP:
                USED_IMGS[k1_ZP] = false;
                USED_IMGS[k1_ZL] = true;
                USED_IMGS[k2_ZP] = true;
                USED_IMGS[k2_ZL] = false;
                break;
            case DK_ZP:
                USED_IMGS[k1_ZP] = false;
                USED_IMGS[k1_ZL] = false;
                USED_IMGS[k2_ZP] = true;
                USED_IMGS[k2_ZL] = false;
                break;
            case PK_ZP_DK_ZP:
                USED_IMGS[k1_ZP] = true;
                USED_IMGS[k1_ZL] = false;
                USED_IMGS[k2_ZP] = true;
                USED_IMGS[k2_ZL] = false;
                break;
        }

        for (int i = 0; i < BUTTONS.length; ++i) {

            // enable-ne první tři buttony
            BUTTONS[i].setEnabled(i < 3);
        }
    }

    private int mapSToImg(int varindex) {

        varindex -= 3;
        // 0 = 1
        // 1 = 3
        // 2 = 5
        // 3 = 7
        return (varindex * 2) + 1;
    }

    private int mapVToImg(int varindex) {
        varindex -= 10;

        // 0 = 21
        // 1 = 23
        // 2 = 25
        // 3 = 27
        return (varindex * 2) + 21;
    }

    private boolean odjizdi() {
        boolean[] vars = controller.station.variables;

        // na jednom ze světel je zelená -> vlak chce odjet
        return vars[4] || vars[5] || vars[8] || vars[9];
    }

    private void updateSegments() {
        boolean[] vars = controller.station.variables;
        // Seg1-3
        BUTTONS[0].setEnabled(vars[0] && !odjizdi());
        BUTTONS[1].setEnabled(vars[1] && !odjizdi());
        BUTTONS[2].setEnabled(vars[2] && !odjizdi());
    }

    private void updateButtons() {
        /*
         * pole pomocných stavových proměnných
         * pořadí proměnných:
         * Seg1-Seg3 [0-2], S1-S7[3-9], V1-V5[10-14]
         */
        boolean[] vars = controller.station.variables;

        // resetneme do defaultního stavu
        resetStatesAndDraw();

        // Seg1-3 default
        updateSegments();

        System.out.println("STATE: " + controller.station.currentState);

        switch (controller.station.currentState) {
            case EMPTY:
                // vjezdy na kolej
                BUTTONS[3].setEnabled(!vars[0]);
                BUTTONS[11].setEnabled(!vars[1]);
                BUTTONS[12].setEnabled(!vars[2]);
                break;
            case PK_ZL:

                // CH1KL, pokud je jeden volný segment a zároveň je červená
                BUTTONS[5].setEnabled((vars[1] || vars[2]) && !vars[4] && !odjizdi());

                // V2KPS2-3 - volno
                BUTTONS[17].setEnabled(!vars[1] && !odjizdi());
                BUTTONS[18].setEnabled(!vars[2] && !odjizdi());

                // V2KL
                BUTTONS[7].setEnabled(!vars[0] && !odjizdi());

                // O1KL - musí být zelená
                BUTTONS[6].setEnabled(vars[4]);
                break;
            case PK_ZP:
                // CH1KP, pokud je volný první segment a zároveň je červená
                BUTTONS[15].setEnabled(vars[0] && !vars[8] && !odjizdi());

                // V2KPS2-3
                BUTTONS[17].setEnabled(!vars[1] && !odjizdi());
                BUTTONS[18].setEnabled(!vars[2] && !odjizdi());

                // V2KL - occupied Seg1
                BUTTONS[7].setEnabled(!vars[0] && !odjizdi());

                // O1KP - musí být zelená
                BUTTONS[16].setEnabled(vars[8]);
                break;
            case PK_ZL_DK_ZL:

                // !!!! MUSÍME NECHAT UVOLNĚNÝ SEGMENT !!!!
                BUTTONS[1].setEnabled(vars[1] && vars[2]);
                BUTTONS[2].setEnabled(vars[2] && vars[1]);
                // CH1KL, pokud je jeden volný segment a zároveň je červená na S2
                BUTTONS[5].setEnabled((vars[1] || vars[2]) && !vars[4] && !odjizdi());

                // CH2KL, pokud je jeden volný segment a zároveň je červená na S3
                BUTTONS[8].setEnabled((vars[1] || vars[2]) && !vars[5] && !odjizdi());

                // O1KL - musí být zelená
                BUTTONS[6].setEnabled(vars[4]);

                // O2KL - musí být zelená
                BUTTONS[9].setEnabled(vars[5]);
                break;
            case DK_ZL:
                // CH2KL, pokud je jeden volný segment a zároveň je červená na S3
                BUTTONS[8].setEnabled((vars[1] || vars[2]) && !vars[5] && !odjizdi());

                // V1KLP - enabled, pokud je ve seg1 vlak
                BUTTONS[4].setEnabled(!vars[0] && !odjizdi());

                // V1KPS2-3P - vlak je v seg 2-3
                BUTTONS[13].setEnabled(!vars[1] && !odjizdi());
                BUTTONS[14].setEnabled(!vars[2] && !odjizdi());

                BUTTONS[10].setEnabled(vars[5]);

                break;
            case PK_ZP_DK_ZL:
                // 15 CH1KP, 8 CH2KL, 16 O1KP, 9 O2KL

                // !!!! MUSÍME NECHAT UVOLNĚNÝ SEGMENT !!!!
                BUTTONS[0].setEnabled((vars[1] || vars[2]) && vars[0]);
                BUTTONS[1].setEnabled(vars[0] || vars[2] && vars[1]);
                BUTTONS[2].setEnabled(vars[0] || vars[1] && vars[2]);

                // CH1KP - musí být volný seg1
                BUTTONS[15].setEnabled(vars[0] && !odjizdi());

                // CH2KL - musí být volný seg2 nebo 3
                BUTTONS[8].setEnabled((vars[1] || vars[2]) && !odjizdi());

                // O1KP - je tam zelená na S6
                BUTTONS[16].setEnabled(vars[8]);

                // O2KL - je tam zelená na S3
                BUTTONS[9].setEnabled(vars[5]);
                break;
            case PK_ZL_DK_ZP:
                // 5 CH1KL, 19 CH2KP, 6 O1KL, 20 O2KP

                // !!!! MUSÍME NECHAT UVOLNĚNÝ SEGMENT !!!!
                BUTTONS[0].setEnabled((vars[1] || vars[2]) && vars[0]);
                BUTTONS[1].setEnabled(vars[0] || vars[2] && vars[1]);
                BUTTONS[2].setEnabled(vars[0] || vars[1] && vars[2]);

                // CH1KL - musí být volný seg2 nebo 3
                BUTTONS[5].setEnabled((vars[1] || vars[2]) && !odjizdi());

                // CH2KP - musí být volný seg1
                BUTTONS[19].setEnabled(vars[0] && !odjizdi());

                // O1KL
                BUTTONS[6].setEnabled(vars[4]);

                // O2KP
                BUTTONS[20].setEnabled(vars[9]);
                break;
            case DK_ZP:
                // 19 CH2KP, 13 V1KPS2P, 14 V1KPS3P, 21 O2KPP, 4 V1KLP

                // CH2KP - musí být volný seg1
                BUTTONS[19].setEnabled(vars[0] && !odjizdi());

                // V1KPS2P - musí být vlak v seg2
                BUTTONS[13].setEnabled(!vars[1] && !odjizdi());

                // V1KPS3P - musí být vlak v seg3
                BUTTONS[14].setEnabled(!vars[2] && !odjizdi());

                // V1KLP - musí být vlak v seg1
                BUTTONS[4].setEnabled(!vars[0] && !odjizdi());

                // O2KPP - musí být zelená u S7
                BUTTONS[21].setEnabled(vars[9]);
                break;
            case PK_ZP_DK_ZP:
                // 15 CH1KP, 19 CH2KP, 16 O1KP, 20 O2KP

                // !!!! MUSÍME NECHAT UVOLNĚNÝ SEGMENT !!!!
                BUTTONS[0].setEnabled(false);

                // CH1KP - musí být volný seg1
                BUTTONS[15].setEnabled(vars[0] && !odjizdi());

                // CH2KP - musí být volný seg1
                BUTTONS[19].setEnabled(vars[0] && !odjizdi());

                // O1KP - S6 zelená
                BUTTONS[16].setEnabled(vars[8]);

                // O2KP - S7 zelená
                BUTTONS[20].setEnabled(vars[9]);
                break;
        }
        /*FMSStates state = controller.station.currentState;

        // na obou kolejích jsou dva vlaky, které jedou doleva
        // -> Seg1 musí být volný
        if (state == PK_ZP_DK_ZP) {
            Seg1(false);
        }


        // na obou kolejích jsou dva vlaky, které jedou zleva
        // Seg2 nebo Seg3 musí být volný
        if (state == PK_ZL_DK_ZL) {

            // Seg2 je obsazeno
            // Seg3 musí být volný
            if (vars[1]) {
                Seg3(false);
            }
            // Seg3 je obsazeno
            // Seg2 musí být volný
            else if (vars[2]) {
                Seg2(false);

            }
        }

        // obě koleje jsou obsazené -> musíme nechat jeden výstup
        if (state == PK_ZL_DK_ZP || state == PK_ZP_DK_ZL) {
            if (vars[1]) {

                // seg1 a seg2 obsazené -> seg3 musí být volný
                if (vars[2]) {
                    Seg3(false);
                }
                // seg1 a seg3 obsazené -> seg2 musí být volný
                else if (vars[3]) {
                    Seg2(false);
                }
            }
            // seg2 a seg3 jsou obsazené
            else if (vars[2] && vars[3]) {
                Seg1(false);
            }
        }*/
    }

    private void Seg1(boolean removeVlak) {

        // Seg1 btn
        BUTTONS[0].setEnabled(removeVlak);

        //
        BUTTONS[3].setEnabled(!removeVlak);

        /*switch (controller.station.currentState) {
            case PK_ZL:
                BUTTONS[6].setEnabled(!removeVlak);
                BUTTONS[7].setEnabled(!removeVlak);
                BUTTONS[17].setEnabled(!removeVlak);
                BUTTONS[18].setEnabled(!removeVlak);
                break;
            case PK_ZP:
                BUTTONS[0].setEnabled(!removeVlak);
                break;
            case EMPTY:
                BUTTONS[3].setEnabled(!removeVlak);
                break;
        }*/
    }

    private void Seg2(boolean removeVlak) {

        // Seg2 btn
        BUTTONS[1].setEnabled(removeVlak);
        BUTTONS[11].setEnabled(false);
        BUTTONS[17].setEnabled(false);

        switch (controller.station.currentState) {
            case PK_ZL:
            case PK_ZP:
                BUTTONS[17].setEnabled(!removeVlak);
                break;
            case EMPTY:
                BUTTONS[11].setEnabled(!removeVlak);
                break;
        }
    }

    private void Seg3(boolean removeVlak) {

        // Seg3 btn
        BUTTONS[2].setEnabled(removeVlak);
        BUTTONS[12].setEnabled(false);

        switch (controller.station.currentState) {
            case PK_ZL:
                break;
            case PK_ZP:
                break;
            case EMPTY:
                BUTTONS[12].setEnabled(!removeVlak);
                break;
        }
    }
}
