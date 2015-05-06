package hicmd;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javafx.scene.chart.XYChart;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Gary
 */
public class ResultsTab extends JPanel {

    private ArrayList<XYChart.Series> seriesList;

    private ChartTab chart;
    private JScrollPane scrollpane;
    private JPanel seriesPanel;
    private final JTextArea outputArea;
    private final JButton copyButton;
    private String result;

    private final int TAB_SIZE = 4;
    private final String COPY_BUTTON = "Copy Result";
    private final Font MONO_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    /**
     *
     * @param chart
     */
    public ResultsTab(ChartTab chart) {
        this.chart = chart;
        outputArea = new JTextArea();
        scrollpane = new JScrollPane();
        seriesList = new ArrayList<>();
        copyButton = new JButton(COPY_BUTTON);
        result = "";
        seriesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        init();
    }

    private void init() {
        outputArea.setFont(MONO_FONT);
        outputArea.setEditable(false);
        outputArea.setTabSize(TAB_SIZE);

        scrollpane.add(outputArea);
        scrollpane.setViewportView(outputArea);
        
        seriesPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Data series"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //copy to clipboard, use with excel etc...
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Clipboard clipboard = toolkit.getSystemClipboard();
                StringSelection strSel = new StringSelection(result);
                clipboard.setContents(strSel, null);
            }
        });
        
        setLayout(new BorderLayout(5, 5));
        setMinimumSize(new Dimension(300, getMinimumSize().height));
        add(scrollpane, BorderLayout.CENTER);
        add(seriesPanel, BorderLayout.NORTH);
        add(copyButton, BorderLayout.SOUTH);
    }
    
    /**
     * Set result string, this is copied to the clipboard.
     * @param result Formatted result if set, otherwise empty String.
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     *
     * @param series
     */
    public void addSeries(XYChart.Series series) {
        seriesList.add(series);
        chart.addSeries(series);
        
        addSeriesLabel(series);
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        outputArea.setText(text);
    }

    private void addSeriesLabel(XYChart.Series series) {
        SeriesCheckBox checkBox = new SeriesCheckBox(series);
        seriesPanel.add(checkBox);
    }
    
    class SeriesCheckBox extends JCheckBox {

        private XYChart.Series series;
        
        public SeriesCheckBox(XYChart.Series series) {
            this.series = series;
            setText(series.getName());
            setSelected(true);
            
            //Add or remove series depending on if we're selected
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isSelected()) {
                        chart.addSeries(getSeries());
                    } else {
                        chart.removeSeries(getSeries());
                    }
                }
            });
        }
        
        public XYChart.Series getSeries() {
            return series;
        }
    } 
}
