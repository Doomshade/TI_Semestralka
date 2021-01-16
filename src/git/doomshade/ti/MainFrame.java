package git.doomshade.ti;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MainFrame extends JFrame {

    private static final HashMap<Integer, JButton> BUTTON_MAP = new HashMap<>();

    public MainFrame(String title) throws HeadlessException {
        super(title);
    }

    private static class JButtonBuilder {
        private final JButton btn = new JButton();

        private JButtonBuilder() {
            btn.setEnabled(false);
        }
    }

    public void init() {
        BorderLayout lo = new BorderLayout();
        Panel p = new Panel();
        JButton btn = new JButtonBuilder().btn;
        p.add(btn);
        lo.addLayoutComponent(p, BorderLayout.SOUTH);
        setLayout(lo);
    }
}
