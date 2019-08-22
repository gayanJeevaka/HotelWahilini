package wahilini.window.ui;

import wahilini.window.entity.Customer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.hibernate.HibernateException;
import wahilini.window.dao.CommonDao;
import wahilini.window.dao.CustomerDao;
import static wahilini.window.ui.LoginUIController.privileges;

public class CustomerUIController implements Initializable {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTellNo1;
    @FXML
    private TextField txtNIC;

    @FXML
    private TableView<Customer> tableCustomer;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colTelNo1;
    @FXML
    private TableColumn colNIC;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnClear;
    @FXML
    private TextField txtSerName;

    @FXML
    private AnchorPane customerPane;
    private Customer customer;
    private Customer oldCustomer;
    private ObservableList<Customer> cusomers;
    @FXML
    private ComboBox<String> cmbRoom;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetForm();
    }

    @FXML
    private void tableCustomerMC(MouseEvent event) {

        if (event.getClickCount() == 2) {
            customer = tableCustomer.getSelectionModel().getSelectedItem();
            if (customer != null) {
                Alert confirmAlert = CommonFeature.getConfirmAlert("Customer Form-Select");
                confirmAlert.setContentText("Do you want to select this customer? :- " + customer.getName());

                if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                    fillForm(customer);

                }
            }
        }
    }

    @FXML
    private void btnSaveAP(ActionEvent event) {
        try {
            if (getErrors().isEmpty()) {
                Alert confirmAlert = CommonFeature.getConfirmAlert("Customer Form-Save");
                String message = "Do you need to save this Customer?\n"
                        + "---------------------------------------------------------- \n"
                        + "Customer Name :- " + customer.getName() + "\n"
                        + "NIC :- " + customer.getNic() + "\n"
                        + "Mobile :- " + customer.getMobile() + "\n"
                        + "---------------------------------------------------------- \n";

                confirmAlert.setContentText(message);
                if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                    CommonDao.insertObject(customer);
                    resetForm();
                }
            } else {
                CommonFeature.ERROR_ALERT.setHeaderText("Customer Form-Save");
                CommonFeature.ERROR_ALERT.setContentText(getErrors());
                CommonFeature.ERROR_ALERT.showAndWait();
            }

        } catch (HibernateException ex) {
            CommonFeature.printException("Error from CustomerUIController.btnSaveAP() ");
            System.out.println("Error from CustomerUIController.btnSaveAP()\n" + ex.getMessage());
        }

    }

    @FXML
    private void btnDeleteAP(ActionEvent event) throws Exception {
        Alert confirmAlert = CommonFeature.getConfirmAlert("Customer Form-Delete");
        confirmAlert.setContentText("Do you want to delete this customer? :- " + customer.getName());
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            customer.setNic(null);
            CommonDao.updateObject(customer);
            resetForm();
        }
    }

    @FXML
    private void btnUpdateAP(ActionEvent event) {
        try {
            if (!getUpdates().isEmpty() && getErrors().isEmpty()) {
                Alert confirmAlert = CommonFeature.getConfirmAlert("Customer Form-Update");
                confirmAlert.setContentText(getUpdates());

                if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                    CommonDao.updateObject(customer);
                    resetForm();
                }
            } else {
                String msg = !getErrors().isEmpty() ? getErrors() : "Nothing changed";
                CommonFeature.ERROR_ALERT.setHeaderText("Customer Form-Update");
                CommonFeature.ERROR_ALERT.setContentText(msg);
                CommonFeature.ERROR_ALERT.showAndWait();
            }

        } catch (HibernateException ex) {
            CommonFeature.printException("Error from CustomerUIController.btnUpdateAP() ");
            System.out.println("Error from CustomerUIController.btnUpdateAP()\n" + ex.getMessage());
        }

    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        Alert confirmAlert = CommonFeature.getConfirmAlert("Customer Form-Clear");
        confirmAlert.setContentText("Do you want to clear this form?");
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            resetForm();

        }
    }

    private String getUpdates() {
        String msg = "";
        msg += !oldCustomer.getName().equals(customer.getName()) ? "Name :- " + oldCustomer.getName() + " changed to " + customer.getName() + "\n" : "";
        msg += !oldCustomer.getNic().equals(customer.getNic()) ? "NIC :- " + oldCustomer.getNic() + " changed to " + customer.getNic() + "\n" : "";
        msg += !oldCustomer.getMobile().equals(customer.getMobile()) ? "Mobile :- " + oldCustomer.getMobile() + " changed to " + customer.getMobile() + "\n" : "";

        return msg;
    }

    private void resetForm() {
        oldCustomer = new Customer();
        customer = new Customer();
        customer.setMobile("");
        customer.setNic("");
        customer.setName("");

        txtNIC.setText("");
        txtName.setText("");
        txtTellNo1.setText("");
        tableCustomer.getItems().clear();
        cusomers = CommonDao.getObjectObservableList("Customer.findAll");
        fillTable(cusomers);
        txtNIC.setStyle(null);
        txtName.setStyle(null);
        txtTellNo1.setStyle(null);

        enableButtons(false, true, true);
        customerPane.requestFocus();
    }
// fill customers table 

    private void fillTable(ObservableList<Customer> customers) {

        colNIC.setCellValueFactory(new PropertyValueFactory("nic"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colTelNo1.setCellValueFactory(new PropertyValueFactory("mobile"));
        tableCustomer.setItems(customers);

    }

    private void fillForm(Customer customer) {

        txtNIC.setText(customer.getNic());
        txtName.setText(customer.getName());
        txtTellNo1.setText(customer.getMobile());

        enableButtons(true, false, false);

        txtNIC.setStyle(null);
        txtName.setStyle(null);
        txtTellNo1.setStyle(null);

        oldCustomer.setName(customer.getName());
        oldCustomer.setNic(customer.getNic());
        oldCustomer.setMobile(customer.getMobile());

    }

    @FXML
    private void txtNameKR(KeyEvent event) {
        String name = txtName.getText();
        if (name.matches("[a-zA-Z\\s.]{3,20}")) {
            customer.setName(name);
            txtName.setStyle(CommonFeature.COL_VALID);
        } else {
            customer.setName("");
            txtName.setStyle(CommonFeature.COL_INVALID);
        }
    }

    @FXML
    private void txtTellNo1KR(KeyEvent event) {
        String tele = txtTellNo1.getText();
        if (tele.matches("0[1-9]{2}\\d{7}")) {
            txtTellNo1.setStyle(CommonFeature.COL_VALID);
            customer.setMobile(tele);
        } else {
            txtTellNo1.setStyle(tele.isEmpty() ? CommonFeature.COL_EMPTY : CommonFeature.COL_INVALID);

            customer.setMobile("");
        }
    }

    @FXML
    private void txtNICKR(KeyEvent event) {
        String nic = txtNIC.getText();
        if (nic.matches("^[1-9][\\d\\W]{10}$|^[5-9][\\d\\W]{8}[v|V]$")) {
            customer.setNic(nic);
            txtNIC.setStyle(CommonFeature.COL_VALID);
        } else {
            customer.setNic("");
            txtNIC.setStyle(nic.isEmpty() ? CommonFeature.COL_EMPTY : CommonFeature.COL_INVALID);
        }

    }

    private void enableButtons(boolean save, boolean update, boolean delete) {
        btnSave.setDisable(save || !privileges.get("Customer_Reg_Form_insert"));
        btnUpdate.setDisable(update || !privileges.get("Customer_Reg_Form_update"));
        btnDelete.setDisable(delete || !privileges.get("Customer_Reg_Form_delete"));

    }

    private String getErrors() {
        String errors = "";
        //Check invalid data
        errors += !txtName.getText().isEmpty() && customer.getName().equals("") ? "Invalid Name \n" : "";
        errors += !txtTellNo1.getText().isEmpty() && customer.getMobile().equals("") ? "Invalid Telephone 1 \n" : "";
        errors += !txtNIC.getText().isEmpty() && customer.getNic().equals("") ? "Invalid NIC \n" : "";

        //Check emptiness data
        errors += txtName.getText().isEmpty() ? "Enter Name \n" : "";
        errors += txtTellNo1.getText().isEmpty() ? "Enter Mobile number \n" : "";
        /* Check already inserted customer */
        if (errors.isEmpty()) {
            Customer customer_ = CustomerDao.getCustomerByTelNo(customer.getMobile());
            if (customer_ != null && customer != null && customer_.getId().equals(customer.getId())) {
                errors += "";
            } else if (customer_ != null) {
                errors += "This customer is already added as " + customer_.getName();
            } else {
                errors += "";
            }
        }
        return errors;
    }

    @FXML
    private void txtSerNameKR(KeyEvent event) {
        String value = txtSerName.getText();
        if (value.isEmpty()) {
            tableCustomer.setItems(cusomers);
        } else {
            tableCustomer.setItems(CustomerDao.getCustomerByName(value));
        }

    }

}
