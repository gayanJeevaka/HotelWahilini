package wahilini.window.dao;

import wahilini.window.entity.Privilege;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import wahilini.window.ui.CommonFeature;

public class UserDao {

    public static ObservableList<Privilege> getModulesByUserId(int id) {
        String query = "SELECT * FROM privilege p WHERE p.sys_user_id=" + id;
        List<Privilege> list = null;
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery(query);
            query2.addEntity(Privilege.class);
            list = query2.list();
            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from getModulesByUserId() ");
            System.out.println("Exception from getModulesByUserId()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    }
}
