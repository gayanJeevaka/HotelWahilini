package wahilini.window.dao;

import wahilini.window.entity.Invoice;
import wahilini.window.entity.ItemType;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import wahilini.window.ui.CommonFeature;

public class CategoryDao {

    public static ObservableList<ItemType> getAllItemTypesByItemTypeName(String itemTyepeName) {
        List<ItemType> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query = session.createSQLQuery("SELECT * FROM item_type i WHERE i.name LIKE '" + itemTyepeName + "%' LIMIT 10");
            query.addEntity(ItemType.class);
            list = query.list();
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            CommonFeature.printException("Exception from InvoiceDao.getItemTypeByItemTypeName()" + ex.getMessage());
            System.out.println("Exception from InvoiceDao.getItemTypeByItemTypeName()\n" + ex.getMessage());
        } finally {
            session.close();

        }
        return FXCollections.observableArrayList(list);
    }
    
  
}
