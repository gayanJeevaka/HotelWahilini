package wahilini.window.report;

import wahilini.window.dao.DBManager;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import wahilini.window.ui.CommonFeature;

public class ReportView {

    Connection connection = null;

    public void initConnection() {

        String HOST = "jdbc:mysql://localhost/budget_battery";
        String USERNAME = "root";
        String PASSWORD = "root";

        try {
            connection = DriverManager.getConnection(HOST, USERNAME, PASSWORD);

        } catch (SQLException ex) {

            ex.printStackTrace();
        }
    }

    // default constructor
    public ReportView() {
    }

    // constructor with String parameter
    public ReportView(String fileName) {
        this(fileName, null);
    }

    // constructor with String & Hashmap parameter
    public ReportView(String fileName, HashMap hashMap) {

        initConnection();
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            //Fill the report with parameter, connection and the stream reader
            JasperPrint jp = JasperFillManager.fillReport(fileName, hashMap, connection);
            //Viewer for JasperReport
            JRViewer jv = new JRViewer(jp);
            //Insert viewer to a JFrame to make it showable
            JFrame jf = new JFrame();
            jf.getContentPane().add(jv);
            //  jf.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("windows/rlanka/images/RLogo.png")));
            jf.validate();
            jf.setVisible(true);
            jf.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            jf.setLocation(0, 0);
            jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            connection.close();
        } catch (JRException | SQLException ex) {
            CommonFeature.printException("Exception from ReportView.ReportView() ");
            ex.printStackTrace();
        }

    }

    public static void getReportView(String fileName, HashMap hashMap) {

//        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            //Fill the report with parameter, connection and the stream reader
            JasperPrint jp = JasperFillManager.fillReport(fileName, hashMap, DBManager.JDBC_CONNECTION);
            //Viewer for JasperReport
            JRViewer jv = new JRViewer(jp);
            //Insert viewer to a JFrame to make it showable
            JFrame jf = new JFrame();
            jf.getContentPane().add(jv);
            //  jf.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("windows/rlanka/images/RLogo.png")));
            jf.validate();
            jf.setVisible(true);
            jf.setSize(Toolkit.getDefaultToolkit().getScreenSize());
            jf.setLocation(0, 0);
            jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        } catch (JRException ex) {
            CommonFeature.printException("Exception from ReportView.getReportView() ");
            System.out.println("Exception from ReportView.getReportView() " + ex.getMessage());
        }

    }
}
