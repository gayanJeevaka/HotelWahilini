package wahilini.window.ui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import static wahilini.window.ui.LoginUIController.privileges;

public class MainWindowUIController implements Initializable {

    @FXML
    private Label lblClock;
    @FXML
    private AnchorPane anpMain;
    @FXML
    private Button btnPriceList;
    @FXML
    private Button btnItemReg;
    @FXML
    private Button btnCatReg;
    @FXML
    private Button btnUserReg;
    public static MainWindowUIController mainWindowUIController;
    @FXML
    private TabPane tabPane;
    public static TabPane tabPane2;
    @FXML
    private MenuItem mItemEmployee;
    @FXML
    private MenuItem mItemRegistration;
    @FXML
    private MenuItem mItemPriceList;
    @FXML
    private MenuItem mitemInvoiceHistory;
    @FXML
    private MenuItem mItemCategoryReg;
    @FXML
    private MenuItem mItemPrivilege;

    @FXML
    private Label lblUser;
    @FXML
    private MenuItem mItemPasswordChange;
    public static Stage tempStage;
    @FXML
    private MenuItem mItemDbBackup;
    @FXML
    private MenuItem mItemCashierBalance;
    @FXML
    private Button btnInvoiceTakeAway;
    @FXML
    private Button btnTableRoomInvoice;
    @FXML
    private Button btnCustomerReg;
    @FXML
    private MenuItem mItemCustomerReg;
    @FXML
    private MenuItem mItemInvoiceTakeAway;
    @FXML
    private MenuItem mItemInvoiceTableRoom;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mainWindowUIController = this;
        ClockView();
        tabPane2 = tabPane;
        lblUser.setText(LoginUIController.employee.getName().toUpperCase());
        mItemEmployee.setDisable(!privileges.get("Employee_Reg_Form_select"));
        btnUserReg.setDisable(!privileges.get("Employee_Reg_Form_select"));
        btnCustomerReg.setDisable(!privileges.get("Customer_Reg_Form_select"));
        mItemCustomerReg.setDisable(!privileges.get("Customer_Reg_Form_select"));

        mItemPrivilege.setDisable(!privileges.get("Privilege_User_A/C_Form_insert"));
        mitemInvoiceHistory.setDisable(!privileges.get("Invoice_Form_insert"));
        mItemInvoiceTakeAway.setDisable(!privileges.get("Invoice_Form_insert"));
        mItemInvoiceTableRoom.setDisable(!privileges.get("Invoice_Form_insert"));

        btnInvoiceTakeAway.setDisable(!privileges.get("Invoice_Form_insert"));
        btnTableRoomInvoice.setDisable(!privileges.get("Invoice_Form_insert"));
        btnCatReg.setDisable(!privileges.get("Category_Reg_Form_insert"));
        mItemCategoryReg.setDisable(!privileges.get("Category_Reg_Form_insert"));

        btnPriceList.setDisable(!privileges.get("Item_Reg_Form_select"));
        mItemPriceList.setDisable(!privileges.get("Item_Reg_Form_select"));
        btnItemReg.setDisable(!privileges.get("Item_Reg_Form_insert"));
        mItemRegistration.setDisable(!privileges.get("Item_Reg_Form_insert"));
        mItemDbBackup.setDisable(!privileges.get("DB_Backup_delete"));
        mItemCashierBalance.setDisable(!privileges.get("Cashier_Balance_delete"));

    }

    private void ClockView() {

        final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
            final Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            lblClock.setText(sdf.format(cal.getTime()));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadWindowInTabPane(String fxmlPath, String tabTitle, String tabId) {
        boolean status = true;
        Tab tab = new Tab();
        for (Tab t : tabPane2.getTabs()) {
            tab = t;
            if (t.getId() != null && t.getId().equals(tabId)) {
                status = false;
                break;
            }
        }
        if (status) {
            Tab newTab = new Tab(tabTitle);
            newTab.setId(tabId);
            try {
                newTab.setContent(FXMLLoader.load(getClass().getResource(fxmlPath)));
                tabPane2.getTabs().add(newTab);
                tabPane2.getSelectionModel().select(newTab);
            } catch (IOException | NullPointerException ex) {
                CommonFeature.printException("Exception from MainWindowUIController.loadWindowInTabPane() ");
                System.out.println("Error From " + newTab.getId() + ex.getMessage());
            }
            tabPane2.requestFocus();
        } else {
            tabPane2.getSelectionModel().select(tab);
            tabPane2.requestFocus();
        }

    }

    @FXML
    private void btnPriceListAP(ActionEvent event) {
        loadWindowInTabPane("ItemSearchMainUI.fxml", "Price List", "ItemSearchMain");
    }

    @FXML
    private void btnItemRegAP(ActionEvent e) {
        loadWindowInTabPane("ItemRegistration.fxml", "Item Reg", "ItemReg");
    }

    @FXML
    private void btnCatRegAP(ActionEvent e) {
        loadWindowInTabPane("CategoryUI.fxml", "Category Reg", "CategoryReg");
    }

    @FXML
    private void btnUserRegAP(ActionEvent e) {
        loadWindowInTabPane("EmployeeUI.fxml", "Employee Reg", "EmployeeReg");
    }

    @FXML
    private void mItemEmployeeAP(ActionEvent e) {
        loadWindowInTabPane("EmployeeUI.fxml", "Employee Reg", "EmployeeReg");

    }

    @FXML
    private void mItemRegistrationAP(ActionEvent e) {
        loadWindowInTabPane("ItemRegistration.fxml", "Item Reg", "ItemReg");
    }

    @FXML
    private void mItemPriceListAP(ActionEvent e) {
        loadWindowInTabPane("ItemSearchMainUI.fxml", "Price List", "ItemSearchMain");
    }

    @FXML
    private void mitemInvoiceHistoryAP(ActionEvent e) {
        loadWindowInTabPane("InvoiceVeiwUI.fxml", "Invoice History", "InvoiceVeiw");
    }

    @FXML
    private void mItemCategoryRegAP(ActionEvent e) {
        loadWindowInTabPane("CategoryUI.fxml", "Category Reg", "Category");
    }

    @FXML
    private void mItemPrivilegeAP(ActionEvent e) {
        loadWindowInTabPane("PrivilegeUI.fxml", "Privilege", "Privilege");
    }

    @FXML
    private void btnLogOff(ActionEvent e) {
        Alert confirmAlert = CommonFeature.getConfirmAlert("System-Logged off");
        String message = LoginUIController.employee.getName() + ", Do you need to logg off from the system?";
        confirmAlert.setContentText(message);
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            try {
                MainApplication main = new MainApplication();
                main.start(new Stage());
                ((Stage) anpMain.getScene().getWindow()).close();

            } catch (Exception ex) {
                CommonFeature.printException("Exception from MainWindowUIController.btnLogOff() ");
                System.out.println("Exception from MainWindowUIController.btnLogOff()" + ex.getMessage());
            }
        }
    }

    @FXML
    private void btnMinimizeAP(ActionEvent e) {
        ((Stage) anpMain.getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void btnCloseAp(ActionEvent e) {
        Alert confirmAlert = CommonFeature.getConfirmAlert("System Exit");
        String message = LoginUIController.employee.getName() + ", Do you need to exit from the system?";
        confirmAlert.setContentText(message);
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            try {
                ((Stage) anpMain.getScene().getWindow()).close();
            } catch (Exception ex) {
                CommonFeature.printException("Exception from MainWindowUIController.btnCloseAp() ");
                System.out.println("Exception from MainWindowUIController.btnCloseAp()" + ex.getMessage());
            }
        }
    }

    @FXML
    private void mItemPasswordChangeAP(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ChangePasswordUI.fxml"));
            Scene scene = new Scene(root);
            tempStage = new Stage();
            tempStage.setScene(scene);
            tempStage.setTitle("Password Change");
            tempStage.initModality(Modality.APPLICATION_MODAL);
            tempStage.show();
            tempStage.setResizable(false);
        } catch (IOException ex) {
            CommonFeature.printException("Exception from MainWindowUIController.mItemPasswordChangeAP() ");
            System.out.println("Exception from MainWindowUIController.mItemPasswordChangeAP()" + ex.getMessage());
        }

    }

    @FXML
    private void mItemDbBackupAP(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("DbBackUpUI.fxml"));
            Scene scene = new Scene(root);
            tempStage = new Stage();
            tempStage.setScene(scene);
            tempStage.setTitle("Database Backup");
            tempStage.initModality(Modality.APPLICATION_MODAL);
            tempStage.show();
            tempStage.setResizable(false);
        } catch (IOException ex) {
            CommonFeature.printException("Exception from MainWindowUIController.mItemDbBackupAP() ");
            System.out.println("Exception from MainWindowUIController.mItemDbBackupAP()" + ex.getMessage());
        }

    }

    @FXML
    private void mItemCashierBalanceAP(ActionEvent event) {
        loadWindowInTabPane("CashierBalanceUI.fxml", "Cashier Balance", "CashierBalance");

    }

    @FXML
    private void btnInvoiceTakeAwayAP(ActionEvent event) {
        loadWindowInTabPane("InvoiceUI.fxml", "Invoice Take Away ", "takeAwayInvoice");

    }

    @FXML
    private void btnTableRoomInvoiceAP(ActionEvent event) {
        loadWindowInTabPane("RoomBillInvoiceUI.fxml", "Invoice Table/Room", "tableRoomInvoice");

    }

    @FXML
    private void btnCustomerRegAP(ActionEvent event) {
        loadWindowInTabPane("CustomerUI.fxml", "Customer Reg", "customerReg");

    }

    @FXML
    private void mItemCustomerRegAP(ActionEvent event) {
        loadWindowInTabPane("CustomerUI.fxml", "Customer Reg", "customerReg");

    }

    @FXML
    private void mItemInvoiceTakeAwayAP(ActionEvent event) {
        loadWindowInTabPane("InvoiceUI.fxml", "Invoice Take Away ", "takeAwayInvoice");

    }

    @FXML
    private void mItemInvoiceTableRoomAP(ActionEvent event) {
        loadWindowInTabPane("RoomBillInvoiceUI.fxml", "Invoice Table/Room", "tableRoomInvoice");

    }

}
