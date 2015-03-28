package hicmd;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Background;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

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
    final LineChart<Number, Number> lineChart;

    public ChartTab(String title) {
        if (title.length() < 1) {
            this.title = "Title";
        } else {
            this.title = title;
        }
        
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

        jfxPanel.setScene(scene);

        setLayout(new BorderLayout(5, 5));
        add(jfxPanel, BorderLayout.CENTER);
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

    public void saveView() throws IOException {
        JFileChooser fm = new JFileChooser();
        fm.setSelectedFile(new File(lineChart.getTitle() + ".png"));

        if (fm.showSaveDialog(fm) == JFileChooser.APPROVE_OPTION) {
            BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            paint(img.getGraphics());
            ImageIO.write(img, "png", fm.getSelectedFile());
        };
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
