package wahilini.window.dao;

import wahilini.window.entity.Employee;
import wahilini.window.entity.Privilege;
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

public class EmployeeDao {

    public static ObservableList<Employee> getEmployeeByName(String name) {
        List<Employee> employees = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SELECT * FROM employee e WHERE e.name LIKE '" + name + "%'"
                    + "AND e.nic IS NOT NULL");
            query.addEntity(Employee.class);
            employees = query.list();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Exception from EmployeeDao.getEmployeeByName() \n" + ex.getMessage());
            CommonFeature.printException("Exception from EmployeeDao.getEmployeeByName() ");
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(employees);
    }

    
}
