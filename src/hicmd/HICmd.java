package hicmd;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

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
    
    private String TITLE = "HICmd";
    private String DOC_TITLE = "Document";
    private Dimension WINDOW_SIZE = new Dimension(600, 600);
    private Dimension MIN_WINDOW_SIZE = new Dimension(600, 600);
    
    public HICmd() {
        init();
    }
    
    private void init() {
        gridbag = new GridBagLayout();
        gbc = new GridBagConstraints();
        tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        documentPanel = new DocumentPanel(this);
        
        tabs.add(DOC_TITLE, documentPanel);
        
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
