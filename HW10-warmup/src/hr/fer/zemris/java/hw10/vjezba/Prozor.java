package hr.fer.zemris.java.hw10.vjezba;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author ilijan
 */

public class Prozor extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton gumb;
    private FormLocalizationProvider flp;


    public Prozor() throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocation(0, 0);
        setTitle("Demo");

        flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);

        //flp.addLocalizationListener(() -> gumb.setText(LocalizationProvider.getInstance().getString("login")));

        initGUI();
        pack();
    }
    private void initGUI() {
        getContentPane().setLayout(new BorderLayout());
        gumb = new JButton(
                new LocalizableAction("login", flp) {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                }
        );

        getContentPane().add(gumb, BorderLayout.CENTER);
        getContentPane().add(new LJLabel("logout", flp), BorderLayout.PAGE_END);

        JMenuBar menuBar = new JMenuBar();

        JMenu languagesMenu = new JMenu("Languages");
        menuBar.add(languagesMenu);

        JMenuItem enLang = new JMenuItem("en");
        enLang.addActionListener(e -> LocalizationProvider.getInstance().setLanguage("en"));
        languagesMenu.add(enLang);

        JMenuItem deLang = new JMenuItem("de");
        deLang.addActionListener(e -> LocalizationProvider.getInstance().setLanguage("de"));
        languagesMenu.add(deLang);

        JMenuItem hrLang = new JMenuItem("hr");
        hrLang.addActionListener(e -> LocalizationProvider.getInstance().setLanguage("hr"));
        languagesMenu.add(hrLang);

        setJMenuBar(menuBar);

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Prozor().setVisible(true));
    }
}
