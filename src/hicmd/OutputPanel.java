package hicmd;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Output panel
 *
 * @author Gary
 */
class OutputPanel extends JPanel {

    HICmd parent;
    OutputPanel thisPanel;
    GridBagLayout gridbag;
    GridBagConstraints gbc;
    String[][] data;

    File file;
    JLabel outputLabel;
    JLabel errorLabel;
    JTextArea rawOutputArea;
    JTextArea outputArea;
    JTextArea errorArea;
    JFXPanel chartJFXPanel;
    JButton copyButton;
    JButton closeButton;

    private final int TAB_SIZE = 4;
    private String OUTPUT_LABEL = "Output";
    private String ERROR_LABEL = "Errors";
    private String COPY_BUTTON = "Copy";
    private String CLOSE_BUTTON = "Close";
    private Font DEF_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private Font MONO_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    @SuppressWarnings("LeakingThisInConstructor")
    public OutputPanel(HICmd parent) {
        this.parent = parent;
        thisPanel = this;
        init();
        addListeners();
    }

    private void init() {
        gridbag = new GridBagLayout();
        gbc = new GridBagConstraints();
        data = null;

        outputLabel = new JLabel();
        errorLabel = new JLabel();
        rawOutputArea = new JTextArea();
        outputArea = new JTextArea();
        errorArea = new JTextArea();
        chartJFXPanel = new JFXPanel();
        copyButton = new JButton();
        closeButton = new JButton();

        outputLabel.setText(OUTPUT_LABEL);
        outputLabel.setFont(DEF_FONT);

        errorLabel.setText(ERROR_LABEL);
        errorLabel.setFont(DEF_FONT);

        rawOutputArea.setFont(MONO_FONT);
        rawOutputArea.setEditable(false);
        rawOutputArea.setTabSize(TAB_SIZE);

        outputArea.setFont(MONO_FONT);
        outputArea.setEditable(false);
        outputArea.setTabSize(TAB_SIZE);

        errorArea.setFont(MONO_FONT);
        errorArea.setEditable(false);
        errorArea.setTabSize(TAB_SIZE);

        copyButton.setText(COPY_BUTTON);
        copyButton.setFont(DEF_FONT);

        closeButton.setText(CLOSE_BUTTON);
        closeButton.setFont(DEF_FONT);

        setLayout(gridbag);

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(outputLabel, gbc);

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridy = 1;
        gbc.weighty = 2;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane rawOutputScroll = new JScrollPane(rawOutputArea);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.add("Raw", rawOutputScroll);
        tabs.add("Processed", outputScroll);
        tabs.add("Chart", chartJFXPanel);
        add(tabs, gbc);

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(errorLabel, gbc);

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridy = 3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane errorScroll = new JScrollPane(errorArea);
        add(errorScroll, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridy = 4;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(copyButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, gbc);
    }

    private void addListeners() {
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //copy to clipboard, use with excel etc...
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Clipboard clipboard = toolkit.getSystemClipboard();
                StringSelection strSel = new StringSelection(outputArea.getText());
                clipboard.setContents(strSel, null);
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.tabs.remove(thisPanel);
            }
        });
    }

    public void createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Effort/Flow");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Output Chart");
        lineChart.setCreateSymbols(false);

        //For every data column after {time, timestep}
        for (int i = 2; i < data[0].length; i++) {
            XYChart.Series series = new XYChart.Series();
            series.setName("Series " + (i - 1));

            for (String[] row : data) {
                float time = Float.parseFloat(row[0]);
                float effort = Float.parseFloat(row[i]);
                series.getData().add(new XYChart.Data(time, effort));
            }

            lineChart.getData().add(series);
        }

        Scene scene = new Scene(lineChart, 800, 600);

        chartJFXPanel.setScene(scene);
    }
}
