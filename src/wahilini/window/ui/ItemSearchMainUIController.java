package wahilini.window.ui;

import java.net.URL;
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
import javafx.scene.input.KeyEvent;
import wahilini.window.dao.CommonDao;
import wahilini.window.dao.ItemDao;
import wahilini.window.entity.Category;
import wahilini.window.entity.Item;
import wahilini.window.entity.ItemType;

public class ItemSearchMainUIController implements Initializable {

    @FXML
    private TableView<Item> ItemTable;
    @FXML
    private TextField txtSerItemName;
    @FXML
    private TableColumn colRetPrice;
    @FXML
    private TableColumn colStockQty;
    @FXML
    private TableColumn colItemName;
    @FXML
    private TableColumn colItemCode;
    @FXML
    private TableColumn colCost;
    @FXML
    private TableColumn colItemType;
    @FXML
    private TableColumn colBrand;

    private ObservableList<Item> items;
    @FXML
    private ComboBox<ItemType> cmbItemType;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetForm();
    }

    @FXML
    private void txtSerItemNameKR(KeyEvent event) {
        getItemsByNameAndItemType();
    }

    private void fillTable(ObservableList<Item> items) {
        colItemCode.setCellValueFactory(new PropertyValueFactory("rop"));
        colItemName.setCellValueFactory(new PropertyValueFactory("itemName"));
        colCost.setCellValueFactory(new PropertyValueFactory("cost"));
        colRetPrice.setCellValueFactory(new PropertyValueFactory("retailPrice"));
        colStockQty.setCellValueFactory(new PropertyValueFactory("stock"));
        colItemType.setCellValueFactory(new PropertyValueFactory("itemTypeId"));
        colBrand.setCellValueFactory(new PropertyValueFactory("brandId"));
        ItemTable.setItems(items);
    }

    private void resetForm() {
        cmbItemType.setItems(CommonDao.getObjectObservableList("ItemType.findAll"));
        cmbItemType.getSelectionModel().select(-1);
        ItemTable.getItems().clear();
        items = ItemDao.getAllItems();
        fillTable(items);
        txtSerItemName.setText("");
    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        resetForm();
    }

    @FXML
    private void cmbItemTypeAP(ActionEvent event) {
        getItemsByNameAndItemType();
    }

    private void getItemsByNameAndItemType() {
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
//
    }
}
