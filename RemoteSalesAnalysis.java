import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.sql.*;

public class RemoteSalesAnalysis extends Application {
    private DefaultPieDataset dataset;

    @Override
    public void start(Stage stage) {
        dataset = new DefaultPieDataset();
        loadDataFromDatabase();

        JFreeChart chart = createChart(dataset);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(new ChartPanel(chart));

        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);

        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Sales Analysis in South America");
        stage.show();
    }

    private JFreeChart createChart(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "Sales Analysis",
                dataset,
                true,
                true,
                false
        );
        return chart;
    }

    private void loadDataFromDatabase() {
        String url = "jdbc:mysql://your_remote_mysql_host:3306/your_database_name";
        String username = "root";
        String password = "";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();
            String query = "SELECT country, device, COUNT(*) AS total FROM sales WHERE country IN ('Argentina', 'Brazil', 'Chile', 'Colombia', 'Ecuador', 'Guyana', 'Paraguay', 'Peru', 'Suriname', 'Uruguay', 'Venezuela') AND device LIKE '%Apple%' GROUP BY country, device";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String country = rs.getString("country");
                String device = rs.getString("device");
                int total = rs.getInt("total");
                dataset.setValue(country + " - " + device, total);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
