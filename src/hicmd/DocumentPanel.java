package hicmd;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * Document Panel
 *
 * @author Gary
 */
class DocumentPanel extends JPanel {

    HICmd parent;
    DocumentPanel thisPanel;
    GridBagLayout gridbag;
    GridBagConstraints gbc;

    File file;
    JLabel fileLabel;
    JTextField fileField;
    JFileChooser fileChooser;
    JButton showButton;
    JButton openButton;
    JLabel documentLabel;
    JTextArea documentArea;
    JLabel commandLabel;
    JTextArea commandArea;
    JButton saveRunButton;

    private final int TAB_SIZE = 4;
    private final String FILE_LABEL = "File path";
    private final String OPEN_TEXT = "Go!";
    private final String SHOW_TEXT = "Pick File";
    private final String DOCUMENT_LABEL = "Document";
    private final String COMMAND_LABEL = "Commands";
    private final String SAVE_RUN_TEXT = "Save & Run";
    private final String NL = "\n";
    private final String TAB = "\t";
    private final Font DEF_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final Font MONO_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    @SuppressWarnings("LeakingThisInConstructor")
    public DocumentPanel(HICmd parent) {
        this.parent = parent;
        thisPanel = this;
        init();
        addListeners();
    }

    private void init() {
        gridbag = new GridBagLayout();
        gbc = new GridBagConstraints();

        file = null;
        fileLabel = new JLabel();
        fileField = new JTextField();
        fileChooser = new JFileChooser("C:\\Users\\Gary\\Google Drive\\Swansea University\\Year 3\\Dissertation\\Code");
        openButton = new JButton();
        showButton = new JButton();
        documentLabel = new JLabel();
        documentArea = new JTextArea();
        commandLabel = new JLabel();
        commandArea = new JTextArea();
        saveRunButton = new JButton();

        JPanel top = new JPanel();
        JPanel bottom = new JPanel();
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        fileLabel.setText(FILE_LABEL);
//        fileLabel.setFont(DEF_FONT);

//        fileField.setFont(DEF_FONT);
        fileField.setMinimumSize(new Dimension(fileField.getMinimumSize().width, 30));
        fileField.setPreferredSize(new Dimension(fileField.getMinimumSize().width, 30));

        openButton.setText(OPEN_TEXT);
//        openButton.setFont(DEF_FONT);

        showButton.setText(SHOW_TEXT);
//        showButton.setFont(DEF_FONT);

        documentLabel.setText(DOCUMENT_LABEL);
//        documentLabel.setFont(DEF_FONT);

        documentArea.setFont(MONO_FONT);
        documentArea.setEditable(true);
        documentArea.setTabSize(TAB_SIZE);

        commandLabel.setText(COMMAND_LABEL);
//        commandLabel.setFont(DEF_FONT);

        commandArea.setFont(MONO_FONT);
        commandArea.setEditable(true);
        commandArea.setTabSize(TAB_SIZE);

        saveRunButton.setText(SAVE_RUN_TEXT);
//        saveRunButton.setFont(DEF_FONT);

        top.setLayout(gridbag);
        bottom.setLayout(gridbag);

        gbc.insets = new Insets(2,2,2,2);
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(fileLabel, gbc);

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(fileField, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(showButton, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(openButton, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(documentLabel, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridy = 3;
        gbc.weighty = 3;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel documentNoWrapPanel = new JPanel(new BorderLayout());
        documentNoWrapPanel.add(documentArea);
        JScrollPane documentScroll = new JScrollPane(documentNoWrapPanel);
        top.add(documentScroll, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bottom.add(commandLabel, gbc);

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridy = 1;
        gbc.weighty = 3;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel commandNoWrapPanel = new JPanel(new BorderLayout());
        commandNoWrapPanel.add(commandArea);
        JScrollPane commandScroll = new JScrollPane(commandNoWrapPanel);
        bottom.add(commandScroll, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(saveRunButton);
        bottom.add(buttonPanel, gbc);
        
        split.setLeftComponent(top);
        split.setRightComponent(bottom);
        split.setDividerLocation(300);
        split.setOneTouchExpandable(true);
        
        setLayout(new BorderLayout());
        add(split, BorderLayout.CENTER);
    }

    private void addListeners() {
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(parent);
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (file != null && file.exists()) {
                        readFile();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(DocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        fileChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    try {
                        file = fileChooser.getSelectedFile();
                        fileField.setText(file.getAbsolutePath());
                        readFile();
                    } catch (IOException ex) {
                        Logger.getLogger(DocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        saveRunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (file != null && file.exists()) {
                        saveAndRun();
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void readFile() throws IOException {
        documentArea.setText("");
        commandArea.setText("");

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String doc = "";
        String line = "";
        while ((line = reader.readLine()) != null) {
            doc += line + NL;
        }
        documentArea.setText(doc.trim());
        reader.close();

        String COMMAND_TEMPLATE
                = "load hi-maude.maude ."
                + NL + "load " + file.getName() + " ."
                + NL + "---"
                + NL + "---Place your commands below"
                + NL + ""
                + NL + ""
                + NL + ""
                + NL + "---Place your commands above"
                + NL + "---"
                + NL + "q .";

        File commandFile = new File(file.getParent() + "//" + file.getName().replace(".himaude", "") + "_HICmd" + ".himaude");
        if (!commandFile.exists()) {
            PrintWriter writer = new PrintWriter(commandFile);
            writer.write(COMMAND_TEMPLATE);
            writer.flush();
            writer.close();
        }

        reader = new BufferedReader(new FileReader(file.getParent() + "//" + file.getName().replace(".himaude", "") + "_HICmd" + ".himaude"));
        doc = "";
        line = "";
        while ((line = reader.readLine()) != null) {
            doc += line + NL;
        }
        commandArea.setText(doc.trim());
        reader.close();

        //Scroll to top on load of files
        documentArea.setCaretPosition(0);
        commandArea.setCaretPosition(0);
    }

    private void saveAndRun() throws FileNotFoundException {
        //Save current edition of files
        File originalFile = file;
        File commandFile = new File(file.getParent() + "//" + file.getName().replace(".himaude", "") + "_HICmd" + ".himaude");

        PrintWriter writer = new PrintWriter(originalFile);
        writer.write(documentArea.getText().trim());
        writer.flush();

        writer = new PrintWriter(commandFile);
        writer.write(commandArea.getText().trim());
        writer.flush();

        writer.close();

        //Create new tab for displaying output
        OutputPanel outputTab = new OutputPanel(parent);
        parent.tabs.add(commandFile.getName(), outputTab);

        //Process data...
        MyThread thread = new MyThread(commandFile, outputTab);
        thread.start();

        //Show output tab
        parent.tabs.setSelectedComponent(outputTab);
    }
}

class ProgressThread extends Thread implements Runnable {

    public boolean terminate = false;
    private final OutputPanel outputPanel;

    public ProgressThread(OutputPanel outputPanel) {
        this.outputPanel = outputPanel;
    }

    public void terminate() {
        terminate = true;
    }

    @Override
    public void run() {
        int i = 0;
        int index = outputPanel.parent.tabs.indexOfComponent(outputPanel);
        JTabbedPane tabs = outputPanel.parent.tabs;

        //Preserve original title
        String tabTitle = tabs.getTitleAt(index);
        Boolean flip = false;

        while (!terminate) {
            try {
                //Update 2x per second
                sleep(500);

                //If returns -1, component has been disposed 
                // and this will terminate next round
                index = outputPanel.parent.tabs.indexOfComponent(outputPanel);

                //Display either progress indicating title
                if (flip) {
                    tabs.setTitleAt(index, "* " + tabTitle + " *");
                } else {
                    tabs.setTitleAt(index, "- " + tabTitle + " -");
                }

                //Flip boolean
                flip = !flip;
            } catch (InterruptedException | ArrayIndexOutOfBoundsException ex) {
                //Log and terminate cleanly
                Logger.getLogger(HICmd.class.getName()).log(Level.SEVERE, null, ex);
                terminate();
            }
        }

        //Restore tab title
        tabs.setTitleAt(index, tabTitle);
    }
}
    
class MyThread extends Thread implements Runnable {
   
    private final File file;
    private final OutputPanel outPanel;
    private String result;
    private String error;

    private final String BEGIN = "cmd /c \"";
    private final String CD = " cd ";
    private final String AND = " & ";
    private final String MAUDE = " maude ";
    private final String END = "\"";

    MyThread(File file, OutputPanel outPanel) {
        this.file = file;
        this.outPanel = outPanel;
    }

    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    @Override
    public void run() {
        result = "";
        error = "";

        //Run command, process output + error streams
        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {

            //Show processing message
            ProgressThread prog = new ProgressThread(outPanel);
            prog.start();

            // using the Runtime exec method:
            //Change dir, run the file.
            Process p = Runtime.getRuntime().exec(
                    BEGIN + CD + file.getParent() + AND
                    + MAUDE + file.getName() + END);

            stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            String sI = "";
            while ((sI = stdInput.readLine()) != null) {
                result += sI + "\n";
                outPanel.rawOutputArea.setText(result);
                outPanel.rawOutputArea.setCaretPosition(outPanel.rawOutputArea.getDocument().getLength());
            }

            // read error output from command line
            String sE = "";
            while ((sE = stdError.readLine()) != null) {
                error += sE + "\n";
            }

            stdError.close();
            stdInput.close();

            //Terminate progress thread and wait for it to finish
            prog.terminate();
            try {
                prog.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(HICmd.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Make output panel deal with it's output...
            outPanel.addResultProcessor(new ResultProcessor(result));

        } catch (IOException ex) {
            Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
