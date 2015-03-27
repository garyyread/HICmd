package hicmd;

import hicmd.resultprocessor.FindEarliestResult;
import hicmd.resultprocessor.HybridRewriteResult;
import hicmd.resultprocessor.HybridSearchResult;
import hicmd.resultprocessor.ModelCheckResult;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javafx.scene.chart.XYChart;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Output panel
 *
 * @author Gary
 */
public class OutputPanel extends JSplitPane {

    HICmd parent;
    OutputPanel thisPanel;
    GridBagLayout gridbag;
    GridBagConstraints gbc;

    File file;
    JLabel outputLabel;
    JTextArea rawOutputArea;
    JLabel closeButton;
    JTabbedPane tabs;
    ChartTab chart;

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

        outputLabel = new JLabel();
        rawOutputArea = new JTextArea();
        closeButton = new JLabel();
        tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

        outputLabel.setText(OUTPUT_LABEL);
        outputLabel.setFont(DEF_FONT);

        rawOutputArea.setFont(MONO_FONT);
        rawOutputArea.setEditable(false);
        rawOutputArea.setTabSize(TAB_SIZE);

        closeButton.setText(CLOSE_BUTTON);
        closeButton.setFont(DEF_FONT);

        JPanel top = new JPanel();
        top.setLayout(gridbag);
        top.setPreferredSize(new Dimension(500, 300));

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        top.add(outputLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridy = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        top.add(closeButton, gbc);

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridy = 1;
        gbc.weighty = 2;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane rawOutputScroll = new JScrollPane(rawOutputArea);
        tabs.add("Raw", rawOutputScroll);
        top.add(tabs, gbc);

        chart = new ChartTab("");
        chart.run();

        setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setLeftComponent(top);
        setRightComponent(chart);
        setOneTouchExpandable(true);
    }

    private void addListeners() {
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                parent.tabs.remove(thisPanel);
            }
        });
    }

    public void addResultProcessor(ResultProcessor processor) {

        int i = 1;
        ArrayList<HybridRewriteResult> hybridRewriteResults = processor.getHybridRewriteResults();
        for (HybridRewriteResult res : hybridRewriteResults) {

            //Create and add JPanel to display textual result
            ResultsTab results = new ResultsTab(chart);
            for (XYChart.Series series : convertResultToSeriesList(res.source, res.result)) {
                results.addSeries(series);
            }
            results.setText(res.toString());

            tabs.add(i + " hrew Result", results);
            i++;
        }

        i = 1;
        ArrayList<HybridSearchResult> hybridSearchResults = processor.getHybridSearchResults();
        for (HybridSearchResult res : hybridSearchResults) {

            //Create and add JPanel to display textual result
            ResultsTab results = new ResultsTab(chart);
            for (XYChart.Series series : convertSolutionsToSeriesList(res.source, res.solutions)) {
                results.addSeries(series);
            }
            results.setText(res.toString());

            tabs.add(i + " hsearch Result", results);

            i++;
        }

        i = 1;
        ArrayList<FindEarliestResult> findEarliestResults = processor.getFindEarliestResults();
        for (FindEarliestResult res : findEarliestResults) {

            //Create and add JPanel to display textual result
            ResultsTab results = new ResultsTab(chart);
            for (XYChart.Series series : convertResultToSeriesList(res.source, res.result)) {
                results.addSeries(series);
            }
            results.setText(res.toString());

            tabs.add(i + " hfind Result", results);
            i++;
        }

        i = 1;
        ArrayList<ModelCheckResult> modelCheckResults = processor.getModelCheckResults();
        for (ModelCheckResult res : modelCheckResults) {

            //Create and add JPanel to display textual result
            ResultsTab results = new ResultsTab(chart);
            results.setText(res.toString());

            tabs.add(i + " hmc Result", results);
            i++;
        }
    }

    private ArrayList<XYChart.Series> convertResultToSeriesList(String[] names, String[][] result) {
        ArrayList<XYChart.Series> seriesList = new ArrayList<>();

        int xpos = 0;
        int ypos = 2;
        for (String name : names) {

            XYChart.Series series = new XYChart.Series();
            series.setName(name);

            for (String[] row : result) {
                float x = Float.parseFloat(row[xpos]);
                float y = Float.parseFloat(row[ypos]);
                series.getData().add(new XYChart.Data(x, y));
            }

            seriesList.add(series);
            ypos++;
        }

        return seriesList;
    }

    private ArrayList<XYChart.Series> convertSolutionsToSeriesList(String[] names, String[][][] solutions) {
        ArrayList<XYChart.Series> seriesList = new ArrayList<>();

        int sol = 1;
        for (String[][] result : solutions) {
            int xpos = 0;
            int ypos = 2;
            for (String name : names) {

                XYChart.Series series = new XYChart.Series();
                series.setName(name + " (s" + sol + ")");

                for (String[] row : result) {
                    float x = Float.parseFloat(row[xpos]);
                    float y = Float.parseFloat(row[ypos]);
                    series.getData().add(new XYChart.Data(x, y));
                }

                seriesList.add(series);
                ypos++;
            }

            sol++;
        }

        return seriesList;
    }
}
