package wahilini.window.dao;

import wahilini.window.entity.Grn;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import wahilini.window.ui.CommonFeature;

public class GRNDao {

    public static ObservableList<Grn> getGRNByGRNNo(String grnNo) {
        List<Grn> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SELECT * FROM grn g  WHERE g.id = '" + grnNo + "' LIMIT 1");
            query.addEntity(Grn.class);
            list = query.list();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from GRNDao.getGRNByGRNNo() ");
            System.out.println("Exception from GRNDao.getGRNByGRNNo() \n" + ex.getMessage());
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    }

    public static ObservableList<Grn> getAllGRNs() {
        List<Grn> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SELECT * FROM grn g ORDER BY g.id DESC LIMIT 300");
            query.addEntity(Grn.class);
            list = query.list();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from GRNDao.getAllGRNs() ");
            System.out.println("Exception from GRNDao.getAllGRNs() \n" + ex.getMessage());
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    }

    public static Integer getGRNID() {
        String query = "SELECT MAX(id) as id FROM grn";
        Integer id = null;
       
        try ( Statement statement = DBManager.JDBC_CONNECTION.createStatement()){          
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
                
        } catch (SQLException ex) {
            CommonFeature.printException("Exception from GRNDao.getGRNID() ");
            System.out.println("Exception from GRNDao.getGRNID() \n" + ex.getMessage());
        }
        return id != null ? id : 0;
    }
}
