package wahilini.window.dao;

import wahilini.window.entity.Invoice;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import wahilini.window.entity.Item;
import wahilini.window.entity.TempBillDetails;
import wahilini.window.ui.CommonFeature;

public class InvoiceDao {

//   This method is used in Invoice history view UI and return a invoice using invoice No param
    public static ObservableList<Invoice> getInvoiceByInvNo(String invNo) {
        List<Invoice> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SELECT * FROM invoice i WHERE i.inv_no = '" + invNo + "' LIMIT 1");
            query.addEntity(Invoice.class);
            list = query.list();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from InvoiceDao.getInvoiceByInvNo() ");
            System.out.println("Exception from InvoiceDao.getInvoiceByInvNo()\n" + ex.getMessage());
        } finally {
            session.close();

        }
        return FXCollections.observableArrayList(list);
    }
//   This method is used in GRN history view UI and return a invoice list 

    public static ObservableList<Invoice> getAllInvoices() {
        List<Invoice> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SELECT * FROM invoice i ORDER BY i.id DESC LIMIT 300");
            query.addEntity(Invoice.class);
            list = query.list();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from InvoiceDao.getAllInvoices() ");
            System.out.println("Exception from InvoiceDao.getAllInvoices()\n" + ex.getMessage());
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    }

    public static ObservableList<Invoice> getAllInvoicesByStatus() {
        List<Invoice> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SELECT * FROM invoice i WHERE i.refund_amount > 0.0"
                    + "ORDER BY i.id DESC LIMIT 300");
            query.addEntity(Invoice.class);
            list = query.list();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from InvoiceDao.getAllInvoicesByStatus() ");
            System.out.println("Exception from InvoiceDao.getAllInvoicesByStatus()\n" + ex.getMessage());
        } finally {
            session.close();

        }
        return FXCollections.observableArrayList(list);
    }

    public static Integer getInvoiceID() {
        String query = "SELECT MAX(id) as id FROM invoice";
        Integer id = null;

        try (Statement statement = DBManager.JDBC_CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException ex) {
            CommonFeature.printException("Exception from InvoiceDao.getInvoiceID() ");
            System.out.println("Exception from InvoiceDao.getInvoiceID()\n" + ex.getMessage());
        }
        return id != null ? id : 0;

    }

    public static ObservableList<XYChart.Data> getSumInvoices(Date stDate, Date enDate) {
        ObservableList<XYChart.Data> sales = FXCollections.observableArrayList();
        String query = "SELECT (SUM(ins.sub_total)- SUM(ins.discount))- SUM(ins.refund_amt) AS Total,"
                + " i.inv_date AS Date FROM invoiceitems ins INNER JOIN invoice i ON"
                + " i.id=ins.invoice_id\n"
                + "WHERE i.inv_date BETWEEN'" + stDate + "' AND '" + enDate + "' GROUP BY inv_date;";
        try (Statement statement = DBManager.JDBC_CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Double x = resultSet.getBigDecimal("Total").doubleValue();
                XYChart.Data sales1 = new XYChart.Data(resultSet.getDate("Date").toString(), x);
                sales.add(sales1);
            }

        } catch (SQLException ex) {
            CommonFeature.printException("Exception from getSumInvoices() ");
            System.out.println("Exception from getSumInvoices()\n" + ex.getMessage());
        }
        return sales;
    }

    public static List getSalesIncomeFigures(Date startDate, Date endDate) {
        List salesFigures = new ArrayList();
        String query = "SELECT inv_date, SUM(net_total) AS NetTotal FROM invoice i WHERE i.crud_status_id =1 AND (i.inv_date BETWEEN '" + startDate + "' AND '" + endDate + "') GROUP BY i.inv_date";
        try (Statement statement = DBManager.JDBC_CONNECTION.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {

                salesFigures.add(resultSet.getDate("inv_date"));
                salesFigures.add(resultSet.getDouble("NetTotal"));
            }
        } catch (SQLException ex) {
            CommonFeature.printException("Exception from InvoiceDao.getSalesFiguresByDate() ");
            System.out.println("Exception from InvoiceDao.getSalesFiguresByDate()\n" + ex.getMessage());
        }
        return salesFigures;
    }

    public static double getSalesReturnSumByDate(Date startDate, Date endDate) {
        String query = "SELECT SUM(refund_amount) AS refundAmont FROM sales_return s WHERE rtn_date BETWEEN"
                + "'" + startDate + "' AND '" + endDate + "'";

        double sum = 0.0;
        try (Statement statement = DBManager.JDBC_CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                sum = resultSet.getDouble("refundAmont");
            }
        } catch (SQLException ex) {
            CommonFeature.printException("Exception from InvoiceDao.getSalesReturnSumByDate() ");
            System.out.println("Exception from InvoiceDao.getSalesReturnSumByDate()\n" + ex.getMessage());
        }

        return sum;

    }

    public static ObservableList<Double> getSalesFigures_ByDate(Date startDate, Date endDate) {
        ObservableList<Double> salesFigures = FXCollections.observableArrayList();
        String query = "SELECT SUM(net_total) AS NetTotal, SUM(sale_cost) AS SaleCost FROM invoice i WHERE i.crud_status_id = 1 AND (i.inv_date BETWEEN '" + startDate + "' AND '" + endDate + "')";
        String query2 = "SELECT SUM(total_amount) AS ChargeIncome FROM battery_charge b WHERE b.crud_status_id = 1 AND (b.charge_date BETWEEN '" + startDate + "' AND '" + endDate + "') ";
        String query3 = "SELECT SUM(expense_amount) AS Expenses FROM expense e WHERE e.expense_date BETWEEN '" + startDate + "' AND '" + endDate + "' ";

        try (Statement statement = DBManager.JDBC_CONNECTION.createStatement();
                Statement statement1 = DBManager.JDBC_CONNECTION.createStatement();
                Statement statement2 = DBManager.JDBC_CONNECTION.createStatement();) {
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                salesFigures.add(resultSet.getDouble("NetTotal"));
                salesFigures.add(resultSet.getDouble("SaleCost"));
            }
            resultSet = statement1.executeQuery(query2);
            if (resultSet.next()) {
                salesFigures.add(resultSet.getDouble("ChargeIncome"));
            }
            resultSet = statement2.executeQuery(query3);
            if (resultSet.next()) {
                salesFigures.add(resultSet.getDouble("Expenses"));
            }
            resultSet.close();
        } catch (SQLException ex) {
            CommonFeature.printException("Exception from InvoiceDao.getSalesFiguresByDate() ");
            System.out.println("Exception from InvoiceDao.getSalesFiguresByDate()\n" + ex.getMessage());
        }
        return salesFigures;
    }

    public static void deleteTempBillDetails(int roomId) {

        String query = "DELETE from temp_bill_details WHERE room_table_id=?";
        try {
            if (DBManager.JDBC_CONNECTION.isClosed()) {
                DBManager.getDBConnection();
            }
        } catch (SQLException ex) {
            CommonFeature.printException("Exception from InvoiceDao.deleteTempBillDetails() ");
            System.out.println("Exception from InvoiceDao.deleteTempBillDetails()\n" + ex.getMessage());
        }
        try (PreparedStatement prestatement = DBManager.JDBC_CONNECTION.prepareStatement(query)) {
            prestatement.setInt(1, roomId);
            prestatement.execute();

        } catch (SQLException ex) {
            CommonFeature.printException("Exception from InvoiceDao.deleteTempBillDetails() ");
            System.out.println("Exception from InvoiceDao.deleteTempBillDetails()\n" + ex.getMessage());
        }
    }

    public static List<Double> getInvoiceDetailsBydate(Date stDate, Date endDate) {
        List<Double> salesDetails = new ArrayList<>();
        String query = "SELECT sum(grand_total) as GrandTotal, sum(service_charge) as ServiceCharge,"
                + " sum(total_discount) as Discount, sum(net_total)"
                + " NetTotal from invoice i"
                + " where i.crud_status_id = 1 and inv_date between '" + stDate + "' AND '" + endDate + "'";

        try (Statement statement = DBManager.JDBC_CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                double grandTotal = resultSet.getDouble("GrandTotal");
                double serviceCharge = resultSet.getDouble("ServiceCharge");
                double discount = resultSet.getDouble("Discount");
                double netTotal = resultSet.getDouble("NetTotal");

                salesDetails.add(grandTotal);
                salesDetails.add(serviceCharge);
                salesDetails.add(discount);
                salesDetails.add(netTotal);

            }
        } catch (SQLException ex) {
            CommonFeature.printException("Exception from InvoiceDao.getInvoiceDetailsBydate() ");
            System.out.println("Exception from InvoiceDao.getInvoiceDetailsBydate()\n" + ex.getMessage());
        }
        return salesDetails;

    }
}
