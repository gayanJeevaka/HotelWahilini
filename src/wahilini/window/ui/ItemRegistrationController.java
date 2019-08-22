package wahilini.window.ui;

import wahilini.window.dao.CommonDao;
import wahilini.window.dao.ItemDao;
import wahilini.window.entity.Category;
import wahilini.window.entity.Item;
import wahilini.window.entity.ItemType;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.TextFields;
import static wahilini.window.ui.LoginUIController.privileges;

public class ItemRegistrationController implements Initializable {

    /* fx Controllers */
    @FXML
    private ComboBox<ItemType> cmbItemType;
    @FXML
    private ComboBox<Category> cmbCategory;
    @FXML
    private TextField txtItemName;
    @FXML
    private TextField txtRetailPrice;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSave;
    @FXML
    private AnchorPane PaneItemReg;
    @FXML
    private Button btnClear;
    /* fx Table & Columns */
    @FXML
    private TableView<Item> tbItems;
    @FXML
    private TableColumn colItemName;
    @FXML
    private TableColumn colSpecialPrice;
    @FXML
    private TableColumn colRetailPrice;
    @FXML
    private TableColumn<Item, String> colBarcode;
    /* Object declaration*/
    private HashMap parameters;
    private Item oldItem;
    private Item item;
    private String categoryName;
    private String brandName;
    private String ItemName;
    private ObservableList<Item> items;
    private ObservableList<ItemType> itemTypes;
    private ObservableList<Category> categories;
    private boolean isUpdate;
    @FXML
    private RadioButton rbtnKotYes;
    @FXML
    private ToggleGroup tgKotItem;
    @FXML
    private RadioButton rbtnKotNo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetForm();
    }

    @FXML
    private void cmbItemTypeAP(ActionEvent event) {
        try {
            String itemTypeName = cmbItemType.getEditor().getText();
            ItemType itemType = new ItemType(0);

            for (ItemType itemTy : itemTypes) {
                if (itemTy.getName().toLowerCase().equals(itemTypeName.toLowerCase()) && (itemTy.getId() != null)) {
                    itemType.setId(itemTy.getId());
                    itemType.setName(itemTy.getName());
                    break;
                }
            }

            if (!isUpdate && itemType.getId() > 0) {
                item.setItemTypeId(itemType);
                categories = ItemDao.getCategoriesByItemType(itemType);
                cmbCategory.setItems(categories);
                cmbItemType.getEditor().setStyle(CommonFeature.COL_VALID);
                txtItemName.setText("");
                cmbItemType.getSelectionModel().select(itemType);
                cmbCategory.setStyle(null);

            } //            else if (isUpdate && itemType.getId() > 0) {
            //                brands = ItemDao.getBrandsByItemType(itemType);
            //                cmbBrand.setItems(brands);
            //                cmbCategory.setStyle(null);
            //
            else if (itemTypeName.isEmpty() || itemType.getId() <= 0) {
                cmbCategory.getItems().clear();
                categories.clear();
                txtItemName.setText("");
                cmbItemType.getEditor().setStyle(CommonFeature.COL_EMPTY);

            }
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            CommonFeature.getAlertInformation("Your current work start again!");
            resetForm();
            resetForm();
            CommonFeature.printException("Exception from Item Registration cmbItemTypeAP()");
            System.out.println("Exception from Item Registration cmbItemTypeAP() \n" + ex.getMessage());
        }
    }

    private void enableButtons(boolean save, boolean update, boolean delete) {
        btnSave.setDisable(save || !privileges.get("Item_Reg_Form_insert"));
        btnUpdate.setDisable(update || !privileges.get("Item_Reg_Form_update"));
        btnDelete.setDisable(delete || !privileges.get("Item_Reg_Form_delete"));
    }

    @FXML
    private void cmbCategoryAP(ActionEvent event) {
        try {
            Category category = cmbCategory.getSelectionModel().getSelectedItem();
            if (category != null && item.setCategoryId(category)) {
                cmbCategory.setStyle(CommonFeature.COL_VALID);
                categoryName = category.getName();
                items = ItemDao.getItemsByCategoryId(category);
                tbItems.setItems(items);
                ItemName = brandName + " " + categoryName;
                txtItemName.setText(ItemName);
                txtItemName.setStyle(CommonFeature.COL_VALID);
                item.setItemName(ItemName);

            } else {
                cmbCategory.setStyle(null);
            }
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            CommonFeature.getAlertInformation("Your current work start again!");
            resetForm();
            CommonFeature.printException("Exception from Item Registration cmbCategoryAP()");
            System.out.println("Exception from Item Registration cmbCategoryAP() \n" + ex.getMessage());
        }

    }

    @FXML
    private void btnSaveAP(ActionEvent event) {
        String errors = getErrors();
        if (errors.isEmpty()) {

            item.setRemarks("Item inserted by " + LoginUIController.employee + " at "
                    + new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss a").format(new Date()));
            Alert confirmAlert = CommonFeature.getConfirmAlert("Item Form-Save");
            String message = "Do you need to save this item?\n"
                    + "---------------------------------------------------------- \n"
                    + "Item Name :- " + item.getItemName() + "\n"
                    + "Item Type :- " + item.getItemTypeId() + "\n"
                    + "Category  :- " + item.getCategoryId() + "\n"
                    + "Retail Price (Rs.) :- " + item.getRetailPrice() + "\n"
                    + "---------------------------------------------------------- \n";

            confirmAlert.setContentText(message);
            if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                CommonDao.insertObject(item);
                resetForm();
            }
        } else {
            CommonFeature.ERROR_ALERT.setHeaderText("Item - Save");
            CommonFeature.ERROR_ALERT.setContentText(errors);
            CommonFeature.ERROR_ALERT.showAndWait();
        }

    }

    private void resetForm() {
        try {

            this.oldItem = new Item();
            this.item = new Item();
            this.categoryName = "";
            this.brandName = "";
            this.ItemName = "";

            /* set default values for item */
            this.item.setCost(BigDecimal.ZERO);
            this.item.setRetailPrice(BigDecimal.ZERO);
            this.item.setRop(0);
            this.item.setStock(BigDecimal.ZERO);
            this.item.setIsKotItem(true);
            enableButtons(false, true, true);
            this.tbItems.getItems().clear();
            this.items = ItemDao.getAllItems();
            this.parameters = new HashMap();
            this.cmbItemType.getItems().clear();
            this.cmbCategory.getItems().clear();
            this.cmbCategory.getSelectionModel().select(-1);
            this.itemTypes = CommonDao.getObjectObservableList("ItemType.findAll");
            categories = FXCollections.observableArrayList();
            this.cmbItemType.setItems(itemTypes);
            TextFields.bindAutoCompletion(cmbItemType.getEditor(), itemTypes);
            /*  set Emptiness to text fields */

            this.cmbItemType.getEditor().setText("");
            this.txtItemName.setText("");
            this.txtRetailPrice.setText("");

            /*  set empty style to ui controls */
            this.cmbCategory.setStyle(null);
            this.cmbItemType.setStyle(null);
            this.txtItemName.setStyle(CommonFeature.COL_EMPTY);
            this.txtRetailPrice.setStyle(CommonFeature.COL_EMPTY);
            this.rbtnKotYes.setSelected(true);
            this.btnClear.setText("Clear");
            this.btnSave.setText("Save");
            this.cmbCategory.setPromptText("Select Category");
            /* Table Cloumns and set Values to table */
            this.colSpecialPrice.setCellValueFactory(new PropertyValueFactory("categoryId"));
            this.colItemName.setCellValueFactory(new PropertyValueFactory("itemName"));
            this.colBarcode.setCellValueFactory(new PropertyValueFactory("itemTypeId"));
            this.colRetailPrice.setCellValueFactory(new PropertyValueFactory("retailPrice"));
            this.tbItems.setItems(items);
            this.isUpdate = false;
            this.cmbItemType.setMouseTransparent(isUpdate);
            this.cmbItemType.getEditor().requestFocus();

        } catch (NullPointerException ex) {
            CommonFeature.printException("Exception from Item Registration resetForm() -" + ex.getMessage());
            System.out.println("Exception from Item Registration resetForm() \n" + ex.getMessage());
            CommonFeature.getAlertInformation("Please close the application and run again.......");
        }

    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        Alert confirmAlert = CommonFeature.getConfirmAlert("Item Form-Clear");
        confirmAlert.setContentText("Do you want to clear this form?");
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            resetForm();
        }
    }

    @FXML
    private void txtItemNameKR(KeyEvent event) {
        try {
            String iname = txtItemName.getText();
            if (!iname.isEmpty()) {
                item.setItemName(txtItemName.getText());
                txtItemName.setStyle(CommonFeature.COL_VALID);
            } else {
                item.setItemName("");
                txtItemName.setStyle(CommonFeature.COL_INVALID);
            }
            if (iname.isEmpty()) {
                txtItemName.setStyle(CommonFeature.COL_EMPTY);
            }
        } catch (NullPointerException ex) {
            CommonFeature.getAlertInformation("Your current work, start again!");
            resetForm();
            CommonFeature.printException("Exception from Item Registration txtItemNameKR() -" + ex.getMessage());
            System.out.println("Exception from Item Registration txtItemNameKR() \n" + ex.getMessage());
        }

    }

    @FXML
    private void txtRetailPriceKR(KeyEvent event) {
        try {
            String rPrice = txtRetailPrice.getText();
            if (rPrice.matches("[\\d]{1,7}[.]{0,1}[0-9]{0,2}")) {
                item.setRetailPrice(BigDecimal.valueOf(Double.valueOf(rPrice)));
                txtRetailPrice.setStyle(CommonFeature.COL_VALID);
            } else {
                item.setRetailPrice(BigDecimal.valueOf(0.0));
                txtRetailPrice.setStyle(CommonFeature.COL_INVALID);
            }
            if (rPrice.isEmpty()) {
                txtRetailPrice.setStyle(CommonFeature.COL_EMPTY);
            }
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            CommonFeature.getAlertInformation("Your current work start again!");
            resetForm();
            CommonFeature.printException("Exception from Item Registration txtRetailPriceKR() -" + ex.getMessage());
            System.out.println("Exception from Item Registration txtRetailPriceKR() \n" + ex.getMessage());
        }

    }

    private void fillItemForm(Item item) {
        try {
            isUpdate = true;
            String name = item.getItemName();
            txtRetailPrice.setText(item.getRetailPrice().toString());
            cmbItemType.getSelectionModel().select(item.getItemTypeId());
            cmbCategory.getSelectionModel().select(item.getCategoryId());
            cmbItemType.getEditor().setStyle(null);
            cmbCategory.setStyle(null);
            cmbItemType.setMouseTransparent(isUpdate);
            enableButtons(true, false, false);
            tbItems.requestFocus();
            txtItemName.setText(name);
            txtItemName.setStyle(CommonFeature.COL_EMPTY);
            item.setItemName(name);

        } catch (NullPointerException ex) {
            CommonFeature.getAlertInformation("Your current work start again!");
            resetForm();
            CommonFeature.printException("Exception in ItemRegistrationController.ItemFillForm() -" + ex.getMessage());
            System.out.println("Exception in ItemRegistrationController.ItemFillForm()\n" + ex.getMessage());
        }

    }

    @FXML
    private void tbItemsMC(MouseEvent event) {
        try {
            item = tbItems.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 2 && item != null) {
                Alert confirmAlert = CommonFeature.getConfirmAlert("Item Form-Select");
                confirmAlert.setContentText("Do you want to select this item? :- " + item.getItemName());
                if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                    parameters.put("id", item.getId());
                    oldItem = (Item) CommonDao.getObjectObservableList("Item.findById", parameters).get(0);
                    fillItemForm(item);
                    txtItemName.setStyle(CommonFeature.COL_EMPTY);
                }
            }
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            CommonFeature.getAlertInformation("Your current work start again!");
            resetForm();
            CommonFeature.printException("Exception from Item Registration tbItemsMC()");
            System.out.println("Exception from Item Registration tbItemsMC() \n" + ex.getMessage());
        }
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        String errors = getErrors();

        if (!getUpdates().isEmpty() && errors.isEmpty()) {
            Alert confirmAlert = CommonFeature.getConfirmAlert("Item Form-Update");
            confirmAlert.setContentText(getUpdates());

            if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                item.setRemarks("Item updated by " + LoginUIController.employee + " at "
                        + new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss a").format(new Date()));
                CommonDao.updateObject(item);
                resetForm();
            }
        } else {
            String msg = !errors.isEmpty() ? errors : "Nothing changed";
            CommonFeature.ERROR_ALERT.setHeaderText("Item - Update");
            CommonFeature.ERROR_ALERT.setContentText(msg);
            CommonFeature.ERROR_ALERT.showAndWait();
        }
    }

    @FXML
    private void btnDeleteAP(ActionEvent event) {
        Alert confirmAlert = CommonFeature.getConfirmAlert("Item Form-Delete");
        confirmAlert.setContentText("Do you want to delete this item? :- " + item.getItemName());
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            item.setIsDeleted(true);
            item.setRemarks("Item deleted by " + LoginUIController.employee + " at "
                    + new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss a").format(new Date()));
            CommonDao.updateObject(item);
            resetForm();
        }
    }

    private String getErrors() {
        String errors = "";
        try {

            //Check invalid data
            errors += !txtRetailPrice.getText().isEmpty() && item.getRetailPrice().doubleValue() == 0.0 ? "Invalid Retail Price\n" : "";
            //Check emptiness data
            errors += cmbItemType.getSelectionModel().getSelectedItem() == null ? "Select Item Type\n" : "";
            errors += cmbCategory.getSelectionModel().getSelectedItem() == null ? "Select Category\n" : "";
            errors += txtItemName.getText().isEmpty() ? "Enter Item Name \n" : "";
            errors += txtRetailPrice.getText().isEmpty() ? "Enter Retail Price \n" : "";

        } catch (NullPointerException ex) {
            CommonFeature.getAlertInformation("Your current work start again!");
            resetForm();
            CommonFeature.printException("Exception from Item Registration getErrors()");
            System.out.println("Exception from Item Registration getErrors() \n" + ex.getMessage());
        }
        return errors;
    }

    private String getUpdates() {
        String updConfirm = "";
        try {
            if (!oldItem.getItemTypeId().equals(item.getItemTypeId())) {
                updConfirm += "Item Type:- " + oldItem.getItemTypeId() + " changed to " + item.getItemTypeId() + "\n";
            }

            if (!oldItem.getCategoryId().equals(item.getCategoryId())) {
                updConfirm += "Category:-" + oldItem.getCategoryId() + " changed to " + item.getCategoryId() + "\n";
            }
            updConfirm += !oldItem.getItemName().equals(item.getItemName()) ? "Item Name:- " + oldItem.getItemName() + " changed to " + item.getItemName() + "\n" : "";
            updConfirm += !oldItem.getRetailPrice().equals(item.getRetailPrice()) ? "Retail Price:- " + oldItem.getRetailPrice() + " changed to " + item.getRetailPrice() + "\n" : "";
            updConfirm += !oldItem.getCost().equals(item.getCost()) ? "Cost:- " + oldItem.getCost() + " changed to " + item.getCost() + "\n" : "";
            updConfirm += !oldItem.getRop().equals(item.getRop()) ? "Reorder Level:- " + oldItem.getRop() + " changed to " + item.getRop() + "\n" : "";

        } catch (NullPointerException ex) {
            CommonFeature.getAlertInformation("Your current work start again!");
            resetForm();
            CommonFeature.printException("Exception from Item Registration getUpdates()");
            System.out.println("Exception from Item Registration getUpdates() \n" + ex.getMessage());
        }

        return updConfirm;
    }

    @FXML
    private void PaneItemRegKR(KeyEvent event) {
        if (event.getCode().equals(KeyCode.CONTROL)) {
            btnSave.setText("Save");
            btnClear.setText("Clear");
        }
        if (!isUpdate && event.isControlDown() && event.getCode() == KeyCode.S) {
            btnSave.setText("Save");
            btnClear.setText("Clear");
            btnSave.fire();
        }
        if (event.isControlDown() && event.getCode() == KeyCode.C) {
            btnClear.setText("Clear");
            btnSave.setText("Save");
            btnClear.fire();
        }
    }

    @FXML
    private void PaneItemRegKP(KeyEvent event) {
        if (event.isControlDown()) {
            btnSave.setText("Ctrl+S");
            btnClear.setText("Ctrl+C");
        }
    }

    @FXML
    private void PaneItemRegMC(MouseEvent event) {
        PaneItemReg.requestFocus();
    }

    @FXML
    private void rbtnKotAP(ActionEvent event) {
        item.setIsKotItem(rbtnKotYes.isSelected());
    }

}
