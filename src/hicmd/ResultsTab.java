package hicmd;

import java.awt.Font;
import java.util.ArrayList;
import javafx.scene.chart.XYChart;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Gary
 */
public class ResultsTab extends JScrollPane {

    private ArrayList<XYChart.Series> seriesList;

    private ChartTab chart;
    private final JTextArea outputArea;
    
    private final int TAB_SIZE = 4;
    private final Font MONO_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    
    public ResultsTab(ChartTab chart) {
        this.chart = chart;
        outputArea = new JTextArea();
        seriesList = new ArrayList<>();
        init();
    }
    
    public void addSeries(XYChart.Series series) {
        seriesList.add(series);
        chart.addSeries(series);
    }
    
    public void setText(String text) {
        outputArea.setText(text);
    }

    private void init() {
        outputArea.setFont(MONO_FONT);
        outputArea.setEditable(false);
        outputArea.setTabSize(TAB_SIZE);
        
        setViewportView(outputArea);
    }
}
