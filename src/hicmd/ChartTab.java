package hicmd;

import java.awt.BorderLayout;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Gary
 */
public class ChartTab extends JPanel {

    String title;
    String xLabel = "Time";
    String yLabel = "Effort/Flow";

    final JFXPanel jfxPanel;
    final NumberAxis xAxis;
    final NumberAxis yAxis;
    Stage stage;
    final LineChart<Number, Number> lineChart;

    public ChartTab(String title) {
        this.title = title;

        jfxPanel = new JFXPanel();
        xAxis = new NumberAxis();
        yAxis = new NumberAxis();
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setBackground(Background.EMPTY);
    }

    public void init() {
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);

        lineChart.setTitle(title);
        lineChart.setCreateSymbols(false);

        Scene scene = new Scene(lineChart, 200, 200);
        stage = new Stage();
        stage.setScene(scene);
        jfxPanel.setScene(scene);

        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(jfxPanel), BorderLayout.CENTER);
    }

    public void addSeries(XYChart.Series series) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lineChart.getData().add(series);
            }
        });
    }

    public void removeSeries(XYChart.Series series) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lineChart.getData().remove(series);
            }
        });
    }

    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }
}
