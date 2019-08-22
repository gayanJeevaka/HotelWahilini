package wahilini.window.ui;

import wahilini.window.dao.CommonDao;
import wahilini.window.dao.ItemDao;
import wahilini.window.entity.Item;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import wahilini.window.entity.ItemType;

public class ItemSearchUIController implements Initializable {

    /* Table Columns*/
    @FXML
    private TableView<Item> ItemTable;
    @FXML
    private TableColumn<Item, Double> colItemPrice;

    @FXML
    private TableColumn<Item, String> colItemName;

    @FXML
    private TextField txtSerItemName;
    @FXML
    private AnchorPane paneItemSearch;
    private ObservableList<Item> items;
    public static String controllerName;
    public static String barcode;
    public static ItemSearchUIController itemSearchUIController;
    @FXML
    private ComboBox<ItemType> cmbItemType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbItemType.setItems(CommonDao.getObjectObservableList("ItemType.findAll"));
        cmbItemType.getSelectionModel().select(-1);

        items = ItemDao.getAllItems();
        fillTable(items);
    }

    @FXML
    private void txtSerItemNameKR(KeyEvent event) {

        String itemName = txtSerItemName.getText();
        if (event.getCode() == KeyCode.DOWN) {
            ItemTable.requestFocus();
            ItemTable.getSelectionModel().select(0);
        }
        getItemsByNameAndBrand();

    }

    private void fillTable(ObservableList<Item> items) {
        colItemName.setCellValueFactory(new PropertyValueFactory("itemName"));
        colItemPrice.setCellValueFactory(new PropertyValueFactory("retailPrice"));
        ItemTable.setItems(items);

    }

    @FXML
    private void itemTableKR(KeyEvent event) {
        Item item = ItemTable.getSelectionModel().getSelectedItem();
        switch (controllerName) {
            case "Invoice":
                if (item != null && event.getCode() == KeyCode.ENTER) {
                    InvoiceUIController.invoiceUIController.fillForm(item);
                    InvoiceUIController.stage.close();
                    // InvoiceUIController.roomBillinvoiceUIController.txtQty.requestFocus();
                }
                break;

            case "RoomTableInvoice":
                if (item != null && event.getCode() == KeyCode.ENTER) {
                    RoomBillInvoiceUIController.roomBillinvoiceUIController.fillForm(item);
                    RoomBillInvoiceUIController.stage.close();
                }
                break;
        }

    }

    @FXML
    private void paneItemSearchKR(KeyEvent event) {
        if (controllerName.equals("Invoice") && event.getCode() == KeyCode.ESCAPE) {
            InvoiceUIController.stage.close();
        } else if (controllerName.equals("RoomTableInvoice") && event.getCode() == KeyCode.ESCAPE) {
            RoomBillInvoiceUIController.stage.close();
        }
    }

    @FXML
    private void cmbItemTypeAP(ActionEvent event) {

        getItemsByNameAndBrand();

    }

    private void getItemsByNameAndBrand() {
        String itemName = txtSerItemName.getText();
        ItemType itemType = cmbItemType.getSelectionModel().getSelectedItem();

        if (!itemName.isEmpty() && itemType != null) {
            ItemTable.setItems(ItemDao.getItemsByItemNameAndItemType(itemName, itemType));
        } else if (itemType != null) {
            ItemTable.setItems(ItemDao.getItemsByItemType(itemType));
            txtSerItemName.requestFocus();
        } else if (!itemName.isEmpty()) {
            ItemTable.setItems(ItemDao.getItemsByItemName(itemName));
        } else {
            ItemTable.setItems(items);
        }

    }

}
