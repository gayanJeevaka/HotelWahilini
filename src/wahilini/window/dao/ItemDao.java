package wahilini.window.dao;

import wahilini.window.entity.Category;
import wahilini.window.entity.Item;
import wahilini.window.entity.ItemType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import wahilini.window.ui.CommonFeature;

public class ItemDao {

    public static void updateItem(Item item) {

        String query = "UPDATE item SET stock=? WHERE id=? ";
        try {
            if (DBManager.JDBC_CONNECTION.isClosed()) {
                DBManager.getDBConnection();
            }
        } catch (SQLException ex) {
            CommonFeature.printException("Exception from ItemDao.updateItem() ");
            System.out.println("Exception from ItemDao.updateItem()\n" + ex.getMessage());
        }
        try (PreparedStatement prestatement = DBManager.JDBC_CONNECTION.prepareStatement(query)) {
            prestatement.setBigDecimal(1, item.getStock());
            prestatement.setInt(2, item.getId());
            prestatement.execute();
            System.out.println("itme updates");

        } catch (SQLException ex) {
            CommonFeature.printException("Exception from ItemDao.updateItem() ");
            System.out.println("Exception from ItemDao.updateItem()\n" + ex.getMessage());
        }

    }

    public static int getItemCountByItemType(ItemType itemType) {
        int count = 0;
        String query = "SELECT COUNT(id) AS row_count FROM item i where i.Item_Type_Id =" + itemType.getId();
        try (Statement statement = DBManager.JDBC_CONNECTION.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                count = resultSet.getInt("row_count");
            }
        } catch (SQLException ex) {
            CommonFeature.printException("Exception from ItemDao.getItemCountByItemType() ");
            System.out.println("Exception from ItemDao.getItemCountByItemType()\n" + ex.getMessage());
        }
        return count;
    }

    public static ObservableList<Item> getItemByBarcode(String barcode) {
        List<Item> items = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM item i WHERE i.is_deleted='false' AND i.barcode = '" + barcode + "'");
            query2.addEntity(Item.class);
            items = query2.list();
            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from ItemDao.getItemByBarcode() ");
            System.out.println("Exception from ItemDao.getItemByBarcode()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(items);
    }

    //This method is used in category UI and delete a itemType
    public static boolean deleteItemType(ItemType itemType) {
        boolean isdeleted;
        try {
            String query = "DELETE FROM item_type WHERE id=" + itemType.getId();
            DBManager.JDBC_CONNECTION.createStatement().execute(query);
            isdeleted = true;

        } catch (SQLException ex) {
            CommonFeature.printException("Exception from ItemDao.deleteItemType() ");
            System.out.println("Exception from ItemDao.deleteItemType()\n" + ex.getMessage());
            isdeleted = false;
        }
        return isdeleted;
    }

    //This method is used in category UI and delete a category
    public static boolean deleteCategory(Category category) {
        boolean isdeleted;
        try {
            String query = "DELETE FROM category WHERE id=" + category.getId();
            DBManager.JDBC_CONNECTION.createStatement().execute(query);
            isdeleted = true;

        } catch (SQLException ex) {
            CommonFeature.printException("Exception from ItemDao.deleteCategory() ");
            System.out.println("Exception from ItemDao.deleteCategory()\n" + ex.getMessage());
            isdeleted = false;
        }
        return isdeleted;
    }

    public static ObservableList<Item> getItemsByCategoryId(Category category) {
        List<Item> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM item i WHERE i.Category_Id = " + category.getId() + " AND i.is_deleted='false'");
            query2.addEntity(Item.class);
            list = query2.list();
            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from ItemDao.getByCategoryId() ");
            System.out.println("Exception from ItemDao.getByCategoryId()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    }

    public static Double getStoockValueById(Item item) {
        Item item2 = new Item();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM item i WHERE i.id = " + item.getId() + " LIMIT 1");
            query2.addEntity(Item.class);
            item2 = (Item) query2.uniqueResult();
            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from ItemDao.getStoockValueById() ");
            System.out.println("Exception from ItemDao.getStoockValueById()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return item2.getStock().doubleValue();
    }

    public static ObservableList<Item> getAllItems() {
        List<Item> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM item i WHERE i.is_deleted = 'false' order by id desc LIMIT 300");
            query2.addEntity(Item.class);
            list = query2.list();

            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from ItemDao.getAllItems() ");
            System.out.println("Exception from ItemDao.getAllItems()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    }

    public static ObservableList<Item> getItemsByItemName(String name) {
        List<Item> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM item i WHERE i.is_deleted='false' AND "
                    + "i.item_name LIKE '%" + name + "%' LIMIT 15");
            query2.addEntity(Item.class);
            list = query2.list();
            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from ItemDao.getItemsByItemName() ");
            System.out.println("Exception from ItemDao.getItemsByItemName()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    }

    public static ObservableList<Item> getItemsByItemBarcode(String barcode) {
        List<Item> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM item i WHERE i.is_deleted= false AND "
                    + "i.barcode  ='" + barcode + "' LIMIT 15");
            query2.addEntity(Item.class);

            list = query2.list();
            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from ItemDao.getItemsByItemBarcode() ");
            System.out.println("Exception from ItemDao.getItemsByItemBarcode()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    }

    //This method is used in category UI and delete a brand
    public static boolean deleteItem(Item item) {
        boolean isdeleted;
        try {
            String query = "DELETE FROM item WHERE id=" + item.getId();
            DBManager.JDBC_CONNECTION.createStatement().execute(query);
            isdeleted = true;

        } catch (SQLException ex) {
            CommonFeature.printException("Exception from ItemDao.deleteItem() ");
            System.out.println("Exception from ItemDao.deleteItem()\n" + ex.getMessage());
            isdeleted = false;
        }
        return isdeleted;
    }

    //This method is used to generate barcode number for new items
    public static String generateBarcodeNumber() {
        NumberFormat numFormat = new DecimalFormat("0000000000");
        String query = "SELECT COUNT(id) AS id_count FROM item i";
        String barcode = "";
        try (Statement statement = DBManager.JDBC_CONNECTION.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                barcode = numFormat.format(resultSet.getInt("id_count") + 1);
            }

        } catch (SQLException ex) {
            CommonFeature.printException("Exception from ItemDao.generateBarcodeNumber() ");
            System.out.println("Exception from ItemDao.generateBarcodeNumber() \n" + ex.getMessage());
        }
        return barcode;
    }

    public static String getConCurrency() {
        NumberFormat numFormat = new DecimalFormat("0000000000");
        String query = "LOCK TABLE item READ; SELECT COUNT(id) AS id_count FROM item i";

        String barcode = "";

        try (Statement statement = DBManager.JDBC_CONNECTION.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                barcode = numFormat.format(resultSet.getInt("id_count") + 1);
            }

        } catch (SQLException ex) {
            CommonFeature.printException("Exception from ItemDao.generateBarcodeNumber() ");
            System.out.println("Exception from ItemDao.generateBarcodeNumber() \n" + ex.getMessage());
        }
        return barcode;
    }

    public static ObservableList<Item> getItemsByItemNameAndItemType(String itemName, ItemType itemType) {
        List<Item> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM item i WHERE i.item_type_id=" + itemType.getId()
                    + " AND i.is_deleted= false"
                    + " AND i.item_name  LIKE '%" + itemName + "%' LIMIT 25");
            query2.addEntity(Item.class);

            list = query2.list();
            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from ItemDao.getItemsByItemNameAndItemType() ");
            System.out.println("Exception from ItemDao.getItemsByItemNameAndItemType()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);

    }

    public static ObservableList<Item> getItemsByItemType(ItemType itemType) {
        List<Item> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM item i WHERE i.item_type_id=" + itemType.getId()
                    + " AND i.is_deleted= false LIMIT 15");
                   
            query2.addEntity(Item.class);

            list = query2.list();
            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from ItemDao.getItemsByItemType() ");
            System.out.println("Exception from ItemDao.getItemsByItemType()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);

    }

    public static ObservableList<Category> getCategoriesByItemType(ItemType itemType) {
  List<Category> list = new ArrayList<>();
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            SQLQuery query2 = session.createSQLQuery("SELECT * FROM category c WHERE c.item_type_id=" + itemType.getId());
            query2.addEntity(Category.class);

            list = query2.list();
            transaction.commit();
        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from ItemDao.getCategoriesByItemType() ");
            System.out.println("Exception from ItemDao.getCategoriesByItemType()\n" + ex.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return FXCollections.observableArrayList(list);
    
    }

}
