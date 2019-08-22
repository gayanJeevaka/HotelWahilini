package wahilini.window.dao;

import wahilini.window.entity.Customer;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import wahilini.window.ui.CommonFeature;

public class CustomerDao {

    // This method is used in invoice UI and return a customer using telno param
    public static Customer getCustomerByTelNo(String telNo) {
        Customer customer = new Customer();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SELECT * FROM customer c WHERE c.mobile = '" + telNo + "'"
                    + "AND c.NIC IS NOT NULL LIMIT 1");
            query.addEntity(Customer.class);
            customer = (Customer) query.uniqueResult();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from getCustomerByTelNo() ");
            System.out.println("Exception from getCustomerByTelNo()\n" + ex.getMessage());
        } finally {
            session.close();
        }
        return customer;
    }

    // This method is used in Customer UI and return a customer list using name param
    public static ObservableList<Customer> getCustomerByName(String name) {
        List<Customer> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM customer c WHERE c.Name LIKE '" + name + "%'"
                    + "AND c.NIC IS NOT NULL LIMIT 20");
            query2.addEntity(Customer.class);
            list = query2.list();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from getCustomerByName() ");
            System.out.println("Exception from getCustomerByName()\n" + ex.getMessage());
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    }

    // This method is used in invoice UI and return a customer using NIC param
    public static Customer getCustomerByNIC(String nic) {
        Customer customer = new Customer();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM customer c WHERE c.nic = '" + nic + "'"
                    + "AND c.NIC IS NOT NULL");
            query2.addEntity(Customer.class);
            customer = (Customer) query2.uniqueResult();
            transaction.commit();
        } catch (HibernateException ex) {

            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from getCustomerByNIC() ");
            System.out.println("Exception from getCustomerByNIC() \n" + ex.getMessage());
        } finally {
            session.close();
        }
        return customer;
    }
}
