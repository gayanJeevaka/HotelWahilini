package wahilini.window.ui;

import wahilini.window.dao.CategoryDao;
import wahilini.window.dao.CommonDao;
import wahilini.window.dao.ItemDao;
import wahilini.window.entity.Category;
import wahilini.window.entity.ItemType;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class CategoryUIController implements Initializable {

    @FXML
    private TextField txtCategory;
    @FXML
    private TextField txtItemType;

    @FXML
    private ListView<ItemType> lstItemType;
    @FXML
    private ListView<Category> lstCategory;

    @FXML
    private Button btnCategoryAdd;
    @FXML
    private Button btnItemTypeAdd;
    @FXML
    private Button btnItemRemove;
    @FXML
    private Button btnCategoryRemove;
    private Category category;
    private ItemType itemType;
    private boolean isSelectedCategory;
    private boolean isSelectedItemType;
    private HashMap parameter;
    private ObservableList<ItemType> itemTypes;
    private ObservableList<Category> categories;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetform();
    }

    private void resetform() {
        try {

            btnItemTypeAdd.setText("Add");
            btnCategoryAdd.setText("Add");
            itemTypes = FXCollections.observableArrayList();
            itemTypes = CommonDao.getObjectObservableList("ItemType.findAll");
            lstItemType.getItems().clear();
            lstItemType.setItems(itemTypes);
            categories = FXCollections.observableArrayList();

            lstCategory.getItems().clear();
            itemType = new ItemType();
            category = new Category();
            txtItemType.setText("");

            txtCategory.setText("");
            isSelectedItemType = false;
            isSelectedCategory = false;
            parameter = new HashMap();
        } catch (NullPointerException ex) {

            CommonFeature.printException("Exception from CategoryUIController resetForm()");
            System.out.println("Exception from CategoryUIController restForm() \n" + ex.getMessage());
            CommonFeature.getAlertInformation("Application close and run again!");

        }

    }

    @FXML
    private void lstItemTypeMC(MouseEvent event) {
        try {
            lstCategory.setMouseTransparent(false);
            ItemType itemType_ = lstItemType.getSelectionModel().getSelectedItem();
            if (itemType_ != null) {

                isSelectedCategory = false;
                parameter.put("itemTypeId", itemType_);
                categories = CommonDao.getObjectObservableList("Category.findByItemTypeId", parameter);
                lstCategory.setItems(categories);
                txtItemType.setText(itemType_.getName());
                lstItemType.getSelectionModel().select(itemType_);
                isSelectedItemType = true;
                btnItemTypeAdd.setText("Update");
                itemType.setId(itemType_.getId());
                itemType.setName(itemType_.getName());

            }
        } catch (NullPointerException ex) {
            CommonFeature.printException("Exception from CategoryUIController lstItemTypeMC()");
            System.out.println("Exception from CategoryUIController lstItemTypeMC() \n" + ex.getMessage());
            CommonFeature.getAlertInformation("Your current work start again!");
            resetform();
        }

    }

    @FXML
    private void txtItemTypeKR(KeyEvent event) {
        try {
            String itemTypeName = txtItemType.getText();
            if (!itemTypeName.isEmpty()) {
                lstItemType.setItems(CategoryDao.getAllItemTypesByItemTypeName(itemTypeName));
            } else {
                lstItemType.setItems(itemTypes);
            }
            if (event.getCode() == KeyCode.ENTER) {
                btnItemTypeAdd.fire();
            }

        } catch (NullPointerException ex) {
            CommonFeature.printException("Exception from CategoryUIController txtItemTypeKR()");
            System.out.println("Exception from CategoryUIController txtItemTypeKR() \n" + ex.getMessage());
            CommonFeature.getAlertInformation("Your current work start again!");
            resetform();
        }

    }

    @FXML
    private void txtCategoryKR(KeyEvent event) {
        try {
            String catName = txtCategory.getText();
            if (!catName.isEmpty() && itemType.getId() != null) {
                parameter.put("categoryName", catName + "%");
                parameter.put("itemTypeId", itemType);
                lstCategory.setItems(CommonDao.getObjectObservableList("Category.findByItemTypeIdAndName", parameter));
            } else if (catName.isEmpty()) {
                lstCategory.setItems(categories);
            }
            if (event.getCode() == KeyCode.ENTER) {
                btnCategoryAdd.fire();
            }
        } catch (NullPointerException ex) {
            CommonFeature.printException("Exception from CategoryUIController txtCategoryKR()");
            System.out.println("Exception from CategoryUIController txtCategoryKR() \n" + ex.getMessage());
            CommonFeature.getAlertInformation("Your current work start again!");
            resetform();
        }

    }

    @FXML
    private void lstCategoryMC(MouseEvent event) {
        try {
            category = lstCategory.getSelectionModel().getSelectedItem();
            if (category != null) {
                txtCategory.setText(category.getName());
                isSelectedCategory = true;
                btnCategoryAdd.setText("Update");
            }
        } catch (NullPointerException ex) {
            CommonFeature.printException("Exception from CategoryUIController lstCategoryMC()");
            System.out.println("Exception from CategoryUIController lstCategoryMC() \n" + ex.getMessage());
            CommonFeature.getAlertInformation("Your current work start again!");
            resetform();
        }

    }

    @FXML
    private void btnCategoryAddAP(ActionEvent event) {
        try {
            if (isSelectedItemType && !txtCategory.getText().isEmpty() && !isSelectedCategory) {
                Category cat = new Category();
                cat.setName(txtCategory.getText());
                cat.setItemTypeId(itemType);
                cat.setRemarks("Category inserted by " + LoginUIController.employee + " at "
                        + new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss a").format(new Date()));
                CommonDao.insertObject(cat);
                categories.add(cat);
                lstCategory.setItems(categories);
                lstCategory.scrollTo(categories.size());

            }
            if (!txtCategory.getText().isEmpty() && isSelectedCategory && isSelectedItemType) {
                categories.remove(category);
                category.setName(txtCategory.getText());
                categories.add(category);
                category.setRemarks("Category updated by " + LoginUIController.employee + " at "
                        + new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss a").format(new Date()));
                CommonDao.updateObject(category);
                lstCategory.setItems(categories);
                isSelectedCategory = false;
                btnCategoryAdd.setText("Add");

            }
            if (txtCategory.getText().isEmpty() || lstItemType.getSelectionModel().getSelectedItem() == null) {
                CommonFeature.ERROR_ALERT.setHeaderText("Category - Add");
                CommonFeature.ERROR_ALERT.setContentText(txtCategory.getText().isEmpty() ? "Enter Category Name" : "Please select a Brand");
                CommonFeature.ERROR_ALERT.showAndWait();
            }
            txtCategory.setText("");
        } catch (NullPointerException ex) {
            CommonFeature.printException("Exception from CategoryUIController btnCategoryAddAP()");
            System.out.println("Exception from CategoryUIController btnCategoryAddAP() \n" + ex.getMessage());
            CommonFeature.getAlertInformation("Your current work start again!");
            resetform();
        }

    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        Alert confirmAlert = CommonFeature.getConfirmAlert("Category REG Form - Clear");
        confirmAlert.setContentText("Do you want to clear this form?");
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            resetform();
        }
    }

    @FXML
    private void btnItemTypeAddAP(ActionEvent event) {
        try {
            if (!isSelectedItemType && !txtItemType.getText().isEmpty()) {
                ItemType itemTypeNew = new ItemType();
                itemTypeNew.setName(txtItemType.getText());
                itemTypeNew.setRemarks("Item-Type inserted by " + LoginUIController.employee + " at "
                        + new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss a").format(new Date()));
                CommonDao.insertObject(itemTypeNew);
                itemTypes.add(itemTypeNew);
                lstItemType.setItems(itemTypes);
                lstItemType.scrollTo(itemTypes.size());

            } else if (isSelectedItemType && !txtItemType.getText().isEmpty()) {
                itemTypes.remove(itemType);
                itemType.setName(txtItemType.getText());
                itemTypes.add(itemType);
                lstItemType.setItems(itemTypes);
                itemType.setRemarks("Item-Type updated by " + LoginUIController.employee + " at "
                        + new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss a").format(new Date()));
                CommonDao.updateObject(itemType);
                resetform();

            } else {
                CommonFeature.ERROR_ALERT.setHeaderText("Item Type - Add");
                CommonFeature.ERROR_ALERT.setContentText("Please Enter Valid Item Type");
                CommonFeature.ERROR_ALERT.showAndWait();
            }
            txtItemType.setText("");
        } catch (NullPointerException ex) {
            CommonFeature.printException("Exception from CategoryUIController btnItemTypeAddAP()");
            System.out.println("Exception from CategoryUIController btnItemTypeAddAP() \n" + ex.getMessage());
            CommonFeature.getAlertInformation("Your current work start again!");
            resetform();
        }

    }

    @FXML
    private void btnItemTypeRemoveAP(ActionEvent event) {
        try {
            Alert confirmAlert = CommonFeature.getConfirmAlert("Category Form - Item Type remove");
            confirmAlert.setContentText("Do you want to remove this item type? \n-" + itemType.getName());

            if (isSelectedItemType) {

                if (confirmAlert.showAndWait().get() == ButtonType.YES) {

                    if (!ItemDao.deleteItemType(itemType)) {
                        CommonFeature.ERROR_ALERT.setHeaderText("Item Type-Remove");
                        CommonFeature.ERROR_ALERT.setContentText("Cannot remove");
                        CommonFeature.ERROR_ALERT.showAndWait();

                    } else {
                        resetform();
                        resetform();
                    }

                }
            } else {
                CommonFeature.ERROR_ALERT.setHeaderText("Item Type-Remove");
                CommonFeature.ERROR_ALERT.setContentText("Select a Item Type");
                CommonFeature.ERROR_ALERT.showAndWait();
            }
        } catch (NullPointerException ex) {
            CommonFeature.printException("Exception from CategoryUIController btnItemTypeRemoveAP()");
            System.out.println("Exception from CategoryUIController btnItemTypeRemoveAP() \n" + ex.getMessage());
            CommonFeature.getAlertInformation("Your current work start again!");
            resetform();
        }

    }

    @FXML
    private void btnCategoryRemoveAP(ActionEvent event) {
        try {
            Alert confirmAlert = CommonFeature.getConfirmAlert("Category Form - Category remove");
            confirmAlert.setContentText("Do you want to remove this Category? \n-" + category.getName());

            if (isSelectedCategory) {

                if (confirmAlert.showAndWait().get() == ButtonType.YES) {

                    if (!ItemDao.deleteCategory(category)) {
                        CommonFeature.ERROR_ALERT.setHeaderText("Category-Remove");
                        CommonFeature.ERROR_ALERT.setContentText("Cannot remove");
                        CommonFeature.ERROR_ALERT.showAndWait();
                    } else {
                        resetform();
                    }
                }

            } else {
                CommonFeature.ERROR_ALERT.setHeaderText("Category-Remove");
                CommonFeature.ERROR_ALERT.setContentText("Select a Category");
                CommonFeature.ERROR_ALERT.showAndWait();
            }
        } catch (NullPointerException ex) {
            CommonFeature.printException("Exception from CategoryUIController btnCategoryRemoveAP()");
            System.out.println("Exception from CategoryUIController btnCategoryRemoveAP() \n" + ex.getMessage());
            CommonFeature.getAlertInformation("Your current work start again!");
            resetform();
        }

    }

}
