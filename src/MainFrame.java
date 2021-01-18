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
            BUTTONS[i] = createButton(FMSSignals.values()[i].toString(), FMSSignals.values()[i]);
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

        resetStates();
    }

    private JButton createButton(String text, FMSSignals signal) {
        final JButton btn = new JButton(text);
        btn.setEnabled(false);
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

    private void resetStates() {

        // resetneme
        Arrays.fill(USED_IMGS, false);

        // koleje empty necháme
        USED_IMGS[0] = true;

        // Semafory
        // S1 S4 S5 defaultně na zelenou
        S1(true);
        S2(false);
        S3(false);
        S4(true);
        S5(true);
        S6(false);
        S7(false);

        // Segmenty
        Seg1(true);
        Seg2(true);
        Seg3(true);

        // Vyhybky
        V1(true);
        V2(true);
        V3(true);
        V4(true);

        // poslední výhybka je obráceně
        V5(false);
    }

    private void updateButtons() {
        boolean[] vars = controller.station.variables;

        // resetneme do defaultního stavu
        resetStates();
        FMSStates state = controller.station.currentState;

        // seg1 je obsazený
        if (!vars[0]) {
            Seg1(false);
        }

        // seg2 je obsazený
        if (!vars[1]) {
            Seg2(false);
            V2(false);
            V3(true);
        }

        // seg3 je obsazený
        if (!vars[2]) {
            Seg3(false);
            V2(true);
            V3(false);
        }

        // na první koleji je vlak
        if (state == PK_ZL || state == PK_ZP) {
            V1(false);
            V4(false);
            V5(true);
        }
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
                V2(false);
                V3(true);
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
        }
    }

    private void Seg1(boolean removeVlak) {

        // Seg1 btn
        BUTTONS[0].setEnabled(removeVlak);

        BUTTONS[3].setEnabled(false);

        switch(controller.station.currentState){
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
        }

        // CH1KL

        // 1. segment
        USED_IMGS[15] = removeVlak;
        USED_IMGS[16] = !removeVlak;

        // Vlak chce vjet na kolej
    }

    private void Seg2(boolean removeVlak) {

        // Seg2 btn
        BUTTONS[1].setEnabled(removeVlak);
        BUTTONS[11].setEnabled(false);
        BUTTONS[17].setEnabled(false);

        // 4. semafor
        USED_IMGS[17] = removeVlak;
        USED_IMGS[18] = !removeVlak;

        switch (controller.station.currentState){
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

        // 5. semafor
        USED_IMGS[19] = removeVlak;
        USED_IMGS[20] = !removeVlak;

        switch (controller.station.currentState){
            case PK_ZL:
                break;
            case PK_ZP:
                break;
            case EMPTY:
                BUTTONS[12].setEnabled(!removeVlak);
                break;
        }
    }

    private void S1(boolean isGreen) {
        USED_IMGS[1] = !isGreen;
        USED_IMGS[2] = isGreen;
    }

    private void S2(boolean isGreen) {
        USED_IMGS[3] = !isGreen;
        USED_IMGS[4] = isGreen;
    }

    private void S3(boolean isGreen) {
        USED_IMGS[5] = !isGreen;
        USED_IMGS[6] = isGreen;
    }

    private void S4(boolean isGreen) {
        USED_IMGS[7] = !isGreen;
        USED_IMGS[8] = isGreen;
    }

    private void S5(boolean isGreen) {
        USED_IMGS[9] = !isGreen;
        USED_IMGS[10] = isGreen;
    }

    private void S6(boolean isGreen) {
        USED_IMGS[11] = !isGreen;
        USED_IMGS[12] = isGreen;
    }

    private void S7(boolean isGreen) {
        USED_IMGS[13] = !isGreen;
        USED_IMGS[14] = isGreen;
    }

    private void V1(boolean rovne) {
        USED_IMGS[21] = !rovne;
        USED_IMGS[22] = rovne;
    }

    private void V2(boolean rovne) {
        USED_IMGS[23] = !rovne;
        USED_IMGS[24] = rovne;
    }

    private void V3(boolean rovne) {
        USED_IMGS[25] = !rovne;
        USED_IMGS[26] = rovne;
    }

    private void V4(boolean rovne) {
        USED_IMGS[27] = !rovne;
        USED_IMGS[28] = rovne;
    }

    private void V5(boolean rovne) {
        USED_IMGS[29] = !rovne;
        USED_IMGS[30] = rovne;
    }
}
