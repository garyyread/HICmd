package hicmd;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

/**
 * HICmd
 *
 * @author Gary
 */
public class HICmd extends JFrame {

    GridBagLayout gridbag;
    GridBagConstraints gbc;
    JTabbedPane tabs;
    DocumentPanel documentPanel;
//    JMenuBar toolBar;

    private final String TITLE = "HICmd";
    private final String DOC_TITLE = "Document";
    private final Dimension WINDOW_SIZE = new Dimension(800, 600);
    private final Dimension MIN_WINDOW_SIZE = new Dimension(500, 500);

    public HICmd() {
        init();
    }

    private void init() {
        gridbag = new GridBagLayout();
        gbc = new GridBagConstraints();
        tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
        documentPanel = new DocumentPanel(this);
//        toolBar = new JMenuBar();
//
//        JMenu openFile = new JMenu();
//        openFile.setIcon(getImageIcon("/hicmd/resources/ic_action_collection.png"));
//        openFile.setToolTipText("Open File...");
//
//        JMenu help = new JMenu();
//        help.setIcon(getImageIcon("/hicmd/resources/ic_action_help.png"));
//        help.setToolTipText("Help");
//
//        JMenu settings = new JMenu();
//        settings.setIcon(getImageIcon("/hicmd/resources/ic_action_settings.png"));
//        settings.setToolTipText("Settings");
//
//        JMenu run = new JMenu();
//        run.setIcon(getImageIcon("/hicmd/resources/ic_action_play_over_video.png"));
//        run.setToolTipText("Save & Run...");
//
//        JMenu save = new JMenu();
//        save.setIcon(getImageIcon("/hicmd/resources/ic_action_save.png"));
//        save.setToolTipText("Save");
//
//        toolBar.add(openFile);
//        toolBar.add(save);
//        toolBar.add(run);
//        toolBar.add(settings);
//        toolBar.add(help);
//        toolBar.setBorderPainted(false);

        tabs.add(DOC_TITLE, documentPanel);

//        setJMenuBar(toolBar);
        add(tabs);
        setTitle(TITLE);
        setMinimumSize(MIN_WINDOW_SIZE);
        setPreferredSize(WINDOW_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addListeners();
    }

    private void addListeners() {
        //None yet...
    }

    private ImageIcon getImageIcon(String absolutePath) {
        try {
            URL url = getClass().getResource(absolutePath);
            Image img = Toolkit.getDefaultToolkit().getImage(url);
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ImageIcon();
    }

    public static void main(String args[]) {

        //Run GUI
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                HICmd hicmd = new HICmd();
                hicmd.pack();
                hicmd.setVisible(true);
            }
        });
    }
}
