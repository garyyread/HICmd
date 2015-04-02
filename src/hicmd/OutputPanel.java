package hicmd;

import hicmd.resultprocessor.FindEarliestResult;
import hicmd.resultprocessor.HybridRewriteResult;
import hicmd.resultprocessor.HybridSearchResult;
import hicmd.resultprocessor.ModelCheckResult;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
    JButton closeButton;
    JTabbedPane tabs;
    ChartTab chart;
    JSlider xzoomer;

    private final int TAB_SIZE = 4;
    final int STEP = 200;
    private String OUTPUT_LABEL = "Output";
    private String ERROR_LABEL = "Errors";
    private String COPY_BUTTON = "Copy";
    private String CLOSE_BUTTON = "Close";
    private Font DEF_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private Font MONO_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    private Dimension LABEL_SIZE = new Dimension(80, 30);
    private Dimension BUTTON_SIZE = new Dimension(150, 30);

    /**
     *
     * @param parent
     */
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

        rawOutputArea = new JTextArea();
        tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

        rawOutputArea.setFont(MONO_FONT);
        rawOutputArea.setEditable(false);
        rawOutputArea.setTabSize(TAB_SIZE);

        JPanel top = new JPanel();
        top.setLayout(gridbag);
        top.setPreferredSize(new Dimension(500, 300));

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

        //EAST border slider, to zoom image
        xzoomer = new JSlider(JSlider.VERTICAL, 100, 7000, 100);
        xzoomer.setMajorTickSpacing(1000);
        xzoomer.setMinorTickSpacing(250);
        xzoomer.setPaintLabels(false);
        xzoomer.setPaintTicks(true);
        xzoomer.setBackground(Color.WHITE);
        xzoomer.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                chart.setPreferredSize(new Dimension(xzoomer.getValue(), 0));
                chart.revalidate();
            }
        });

        //Create and add options for the chart
        createChartOptionsTab();

        //Scrollpane for chart
        JScrollPane chartScroller = new JScrollPane(chart);
        JPanel right = new JPanel(new BorderLayout(5, 5));
        right.add(xzoomer, BorderLayout.EAST);
        right.add(chartScroller, BorderLayout.CENTER);

        setRightComponent(right);
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

    /**
     *
     * @param processor
     */
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
                try {
                    float x = Float.parseFloat(row[xpos]);
                    float y = Float.parseFloat(row[ypos]);
                    series.getData().add(new XYChart.Data(x, y));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(getParent(), "Excepion creating graph.\nGraph may not represent all data points.", "HIcmd", JOptionPane.WARNING_MESSAGE);
                    Logger.getLogger(OutputPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
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

    private void createChartOptionsTab() {
        gridbag = new GridBagLayout();
        gbc = new GridBagConstraints();

        JPanel chartOptions = new JPanel(gridbag);

        //Save chart view button
        JButton save = new JButton("Save to image");
        save.setPreferredSize(BUTTON_SIZE);
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    chart.saveView();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(getParent(), "Error saving chart view.", "HIcmd", JOptionPane.WARNING_MESSAGE);
                    Logger.getLogger(OutputPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        //Custom image size adjusters
        JTextField imgWidth = new JTextField();
        JTextField imgHeight = new JTextField();
        ActionListener resizeBtnListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    xzoomer.setValue(Integer.parseInt(imgWidth.getText()));
                    chart.setPreferredSize(new Dimension(Integer.parseInt(imgWidth.getText()), Integer.parseInt(imgHeight.getText())));
                    chart.revalidate();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(getParent(), "Error resizing chart.\nPlease use valid Integers.", "HIcmd", JOptionPane.WARNING_MESSAGE);
                    Logger.getLogger(OutputPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        //Chart Resize listener
        chart.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                super.componentResized(evt);

                Component c = (Component) evt.getSource();
                Dimension size = c.getSize();
                imgWidth.setText("" + size.width);
                imgHeight.setText("" + size.height);
            }
        });
        imgWidth.addActionListener(resizeBtnListener);
        imgWidth.setPreferredSize(LABEL_SIZE);
        imgHeight.addActionListener(resizeBtnListener);
        imgHeight.setPreferredSize(LABEL_SIZE);

        //Update chart title
        JTextField chartTitleField = new JTextField("Title");
        chartTitleField.setPreferredSize(LABEL_SIZE);
        chartTitleField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        chart.lineChart.setTitle(chartTitleField.getText());
                    }
                });
            }
        });

        //update chart xaxis label
        JTextField chartXAxisField = new JTextField("Time");
        chartXAxisField.setPreferredSize(LABEL_SIZE);
        chartXAxisField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        chart.lineChart.getXAxis().setLabel(chartXAxisField.getText());
                    }
                });
            }
        });

        //update chart yaxis label
        JTextField chartYAxisField = new JTextField("Effort/Flow");
        chartYAxisField.setPreferredSize(LABEL_SIZE);
        chartYAxisField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        chart.lineChart.getYAxis().setLabel(chartYAxisField.getText());
                    }
                });
            }
        });

        JPanel chartLabels = new JPanel(gridbag);
        chartLabels.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Chart Labels"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2,2,2,2);
        chartLabels.add(new JLabel("Title "), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        chartLabels.add(chartTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        chartLabels.add(new JLabel("X Axis "), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        chartLabels.add(chartXAxisField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        chartLabels.add(new JLabel("Y Axis "), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        chartLabels.add(chartYAxisField, gbc);
        //END CHART LABELS

        JPanel chartSize = new JPanel(gridbag);
        chartSize.setLayout(gridbag);
        chartSize.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Chart Size"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        chartSize.add(new JLabel("Width "), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        chartSize.add(imgWidth, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        chartSize.add(new JLabel("Height "), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        chartSize.add(imgHeight, gbc);
        //END CHART SIZE
        
        //Add option panels and save button
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        chartOptions.add(chartLabels, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        chartOptions.add(chartSize, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        chartOptions.add(Box.createHorizontalGlue(), gbc);
        
        //Save button
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        chartOptions.add(save, gbc);
        
        //Close button
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        closeButton = new JButton();
        closeButton.setText(CLOSE_BUTTON);
        closeButton.setPreferredSize(BUTTON_SIZE);
        chartOptions.add(closeButton, gbc);

        //Add empty filler
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        chartOptions.add(Box.createHorizontalGlue(), gbc);
        
        //Add the panel to  the tab list
        tabs.add("Chart Options", chartOptions);
    }
}
