package wahilini.window.dao;

import wahilini.window.entity.ChequePayment;
import wahilini.window.entity.Expense;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import wahilini.window.ui.CommonFeature;

public class CommonDao {

    public static <T> void insertObject(T t) throws HibernateException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(t);
            transaction.commit();
        } catch (HibernateException ex) {
            System.out.println("Exception from insertObject() " + ex.getMessage());
            CommonFeature.printException("Exception from insertObject() ");
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static <T> void updateObject(T t) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();// Begin the transaction through the session
            session.update(t);// Update the object 
            transaction.commit();// Commit the transaction 
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from updateObject() ");
            System.out.println("Exception from updateObject() \n" + ex.getMessage());
            if (transaction != null) {

                transaction.rollback();
            }

        } finally {
            session.close();
        }

    }

    public static <T> void deleteObject(T t) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(t);
            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from CommonDao deleteObject() ");
            System.out.println("Error in deleteing CommonDao deleteObject()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }

        } finally {
            session.close();
        }
    }

    public static ObservableList getObjectObservableList(String querys) throws HibernateException {
        List list = null;
        Transaction transaction = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Query query = session.getNamedQuery(querys);
            list = query.list();

            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from getObjectObservableList() ");
            System.out.println("Error in getObjectObservableList() \n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }

        } finally {
            if (session != null) {
                session.close();
            }
        }
        return FXCollections.observableArrayList(list);
    }

    public static ObservableList getObjectObservableList(String nameQuery, HashMap hashMap) throws HibernateException {
        List list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Query query = session.getNamedQuery(nameQuery).setProperties(hashMap);
            list = query.list();
        } catch (HibernateException e) {
            CommonFeature.printException("Exception from getObjectObservableList(Params) " + e.getMessage());
            System.out.println("Exception from getObjectObservableList(Params) \n" + e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }

        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    }

    //   This method is used in expense history UI and return all expenses
    public static ObservableList<Expense> getAllExpensesDetails() {
        List<Expense> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SELECT * FROM expense e ORDER BY e.id DESC LIMIT 100");
            query.addEntity(Expense.class);
            list = query.list();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from CommonDao.getAllExpensesDetails() ");
            System.out.println("Exception from CommonDao.getAllExpensesDetails()\n" + ex.getMessage());
        } finally {
            session.close();

        }
        return FXCollections.observableArrayList(list);
    }

    //   This method is used in Due payments UI and return all sales cheques which on status 
    public static ObservableList<ChequePayment> getAllSalesChequesByStatus(boolean status) {
        List<ChequePayment> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SELECT * FROM cheque_payment c "
                    + " WHERE c.status =" + status + " ORDER BY c.id DESC LIMIT 100");
            query.addEntity(ChequePayment.class);
            list = query.list();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from CommonDao.getAllSalesChequesByStatus() ");
            System.out.println("Exception from CommonDao.getAllSalesChequesByStatus()\n" + ex.getMessage());
        } finally {
            session.close();

        }
        return FXCollections.observableArrayList(list);
    }

  
}
