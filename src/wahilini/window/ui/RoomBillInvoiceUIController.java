package wahilini.window.ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import wahilini.window.dao.CommonDao;
import wahilini.window.dao.CustomerDao;
import wahilini.window.entity.Customer;
import wahilini.window.entity.Invoice;
import wahilini.window.entity.Item;
import wahilini.window.entity.RoomTable;
import wahilini.window.entity.TempBillDetails;

public class RoomBillInvoiceUIController implements Initializable {

    @FXML
    private TableColumn colNo;
    @FXML
    private TableColumn colItemName;
    @FXML
    private TableColumn colUnitPrice;
    @FXML
    private TableColumn colSubTotal;
    @FXML
    private TableView<TempBillDetails> tbInvoiceItems;
    @FXML
    public TextField txtQty;
    @FXML
    private TableColumn colQty;

    @FXML
    private TextField txtNoOfItems;
    @FXML
    private TextField txtGrandTotal;
    @FXML
    private TextField txtNetTotal;
    @FXML
    private Button btnSave;
    @FXML
    private AnchorPane paneInvoice;
    @FXML
    private TextField txtServiceCharge;
    @FXML
    private Label lblQty;

    public static RoomBillInvoiceUIController roomBillinvoiceUIController;
    public static Stage stage;
    private Item item;
    public Invoice invoice;
    private boolean validityQty;
    private Double grandTotal;
    public Double netTotal;
    private double saleCost;
    public double serviceCharge;
    private int index;
    private Customer customer;
    private double discountedPrice;
    private ObservableList<TempBillDetails> tempBillDetails;
    public ObservableList<TempBillDetails> saveTempBillDetails;
    private int validityTxtNetPrice;

    @FXML
    private TextField txtItemName;
    @FXML
    public TextField txtNetPrice;

    @FXML
    private Label lblCustTelNo;
    @FXML
    private TextField txtCusTelNo;

    @FXML
    private ComboBox<RoomTable> cmbTable;
    @FXML
    private ComboBox<RoomTable> cmbRoom;
    private HashMap parameter;
    private RoomTable roomTable;
    @FXML
    private Label lblCustomerName;
    @FXML
    private TableView<TempBillDetails> tbTempBillDetailsSave;
    @FXML
    private TableColumn<?, ?> colItemSaveTemp;
    @FXML
    private TableColumn<?, ?> colQTYTempSave;
    @FXML
    private TableColumn<?, ?> colIndexTempSave;
    @FXML
    private TableColumn<?, ?> colsubTotalTempSave;
    private int orderNo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        MainWindowUIController.tabPane2.requestFocus();
        resetForm();
        roomBillinvoiceUIController = this;

    }

    public void resetForm() {

        invoice = new Invoice();
        customer = new Customer(1);
        txtCusTelNo.setText("");
        lblCustomerName.setText("UNKNOWN");
        discountedPrice = 0.0;
        orderNo = 0;
        txtServiceCharge.setText("0.00");
        txtGrandTotal.setText("0.00");
        txtNetTotal.setText("0.00");
        txtNoOfItems.setText("0.0");

        cmbRoom.setItems(CommonDao.getObjectObservableList("RoomTable.findByIsRoom"));
        cmbRoom.getSelectionModel().select(-1);

        cmbTable.setItems(CommonDao.getObjectObservableList("RoomTable.findByIsTable"));
        cmbTable.getSelectionModel().select(-1);
        if (roomTable != null && roomTable.getIsRoom()) {
            cmbRoom.getSelectionModel().select(roomTable);
            getSelectedRoomTable(roomTable);
        } else if (roomTable != null && !roomTable.getIsRoom()) {
            cmbTable.getSelectionModel().select(roomTable);
            getSelectedRoomTable(roomTable);
        } else {
            roomTable = new RoomTable();
            saveTempBillDetails = FXCollections.observableArrayList();
        }

        parameter = new HashMap();
        validityTxtNetPrice = 0;
        validityQty = true;
        txtQty.setDisable(true);

        tempBillDetails = FXCollections.observableArrayList();
        txtQty.setText("");
        txtItemName.setText("");

        btnSave.setText("Pay");
        lblQty.setText("Qty :-");
        txtNetPrice.setPromptText("0.00");
        txtNetPrice.setText("");
        invoice.setEmployeeId(LoginUIController.employee);
        paneInvoice.requestFocus();
        // Table Columns
        colItemName.setCellValueFactory(new PropertyValueFactory("itemId"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("price"));
        colQty.setCellValueFactory(new PropertyValueFactory("qty"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory("subtotal"));
        colNo.setCellValueFactory(new PropertyValueFactory<>("index"));
        tbInvoiceItems.setItems(tempBillDetails);
        /*Saved temp bill details*/
        colItemSaveTemp.setCellValueFactory(new PropertyValueFactory("itemId"));
        colQTYTempSave.setCellValueFactory(new PropertyValueFactory("qty"));
        colIndexTempSave.setCellValueFactory(new PropertyValueFactory("index"));
        colsubTotalTempSave.setCellValueFactory(new PropertyValueFactory("subtotal"));
        tbTempBillDetailsSave.setItems(saveTempBillDetails);

    }

    public void fillForm(Item item) {
        this.item = item;
        txtItemName.setText(item.getItemName());
        txtNetPrice.setText(item.getRetailPrice().toString());
        txtQty.setDisable(false);
        txtQty.requestFocus();

    }

    private void getTotal() {
        index = 1;
        orderNo = 0;
        grandTotal = 0.0;
        netTotal = 0.0;
        serviceCharge = 0.0;

        for (TempBillDetails invoiceitem : saveTempBillDetails) {
            invoiceitem.setIndex(index);

            grandTotal += invoiceitem.getSubtotal().doubleValue();
            orderNo = invoiceitem.getOrderNo();
            index++;
        }
        orderNo++;
        serviceCharge = grandTotal * 0.1;
        netTotal = grandTotal + serviceCharge;
        txtGrandTotal.setText(CommonFeature.TO_DECI_FORMAT.format(grandTotal));
        txtServiceCharge.setText(CommonFeature.TO_DECI_FORMAT.format(serviceCharge));
        txtNetTotal.setText(CommonFeature.TO_DECI_FORMAT.format(netTotal));
        txtNoOfItems.setText(saveTempBillDetails.size() + "");
        invoice.setGrandTotal(BigDecimal.valueOf(grandTotal));
        invoice.setTotalDiscount(BigDecimal.valueOf(serviceCharge));
        invoice.setNetTotal(BigDecimal.valueOf(0.0));
    }

    @FXML
    private void txtQtyKR(KeyEvent event) {

        if (txtQty.getText().matches("[a-zA-Z]{1,}")) {
            txtQty.setText("");
        }
        Integer qtys = 0;
        if (item != null) {
            qtys = !txtQty.getText().isEmpty() && txtQty.getText().matches("[\\d]{1,3}") ? Integer.valueOf(txtQty.getText()) : 0;
        }

        if (validityQty && event.getCode() == KeyCode.ENTER) {
            if (item != null && qtys > 0.0) {
                addInvoiceItem(qtys.doubleValue());

            } else {

                if (validityQty) {
                    CommonFeature.ERROR_ALERT.setHeaderText("Invoice - Save");
                    CommonFeature.ERROR_ALERT.setContentText(item == null ? "Select an item before enter qty " : "Please check validity of input qty ");
                    CommonFeature.ERROR_ALERT.showAndWait();
                    validityQty = false;
                }
            }
        } else {
            validityQty = true;
        }

    }

    @FXML
    private void btnSaveAP(ActionEvent event) {
        String errorMsg = saveTempBillDetails.size() >= 1 ? "" : "Please add item to the invoice. \n";
        CommonFeature.ERROR_ALERT.setContentText("Do you want to save this invoice?");

        if (errorMsg.isEmpty()) {
            ItemSearchUIController.controllerName = "RoomTableInvoice";
            invoice.setCustomerId(customer);
            invoice.setInvTime(java.sql.Time.valueOf(LocalTime.now()));
            invoice.setInvDate(java.sql.Date.valueOf(LocalDate.now()));
            invoice.setSaleCost(BigDecimal.ZERO);
            invoice.setRefundAmount(BigDecimal.ZERO);
            invoice.setRefundPayAmount(BigDecimal.ZERO);
            invoice.setTotalDiscount(BigDecimal.valueOf(serviceCharge));

            try {

                stage = new Stage();
                AnchorPane balancePane = FXMLLoader.load(getClass().getResource("InvoiceBalanceUI.fxml"));
                Scene scene = new Scene(balancePane);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();

            } catch (IOException ex) {
                CommonFeature.printException("Exception from InvoiceUIController.btnSaveAP() ");
                System.out.println("Exception from InvoiceUIController.btnSaveAP()\n" + ex.getMessage());
            }

        } else {
            CommonFeature.ERROR_ALERT.setContentText(errorMsg);
            CommonFeature.ERROR_ALERT.showAndWait();

        }
    }

    @FXML
    private void paneInvoiceKR(KeyEvent event) {
        KeyCode key = event.getCode();
        if (key.equals(KeyCode.CONTROL)) {
            btnSave.setText("Pay");

        } else if (event.isControlDown() && key == KeyCode.S) {
            btnSave.fire();
            btnSave.setText("Pay");

        } else if (key == KeyCode.F2) {

            loadItemsSearchForm();

        }
    }

    @FXML
    private void paneInvoiceKP(KeyEvent event) {
        if (event.getCode().equals(KeyCode.CONTROL)) {
            btnSave.setText("Pay(Crtl+S)");

        }
    }

    private void loadItemsSearchForm() {
        stage = new Stage();
        ItemSearchUIController.controllerName = "RoomTableInvoice";

        try {
            AnchorPane designationPane = FXMLLoader.load(getClass().getResource("ItemSearchUI.fxml"));
            Scene scene = new Scene(designationPane);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException ex) {
            CommonFeature.printException("Exception from InvoiceUIController.loadItemsSearchForm() ");
            System.out.println("Exception from InvoiceUIController.loadItemsSearchForm()\n" + ex.getMessage());
        }
    }

    @FXML
    private void paneInvoiceMC(MouseEvent event) {
        paneInvoice.requestFocus();
    }

    @FXML
    private void tbInvoiceItemsKR(KeyEvent event) {
        TempBillDetails tempBillDetails_ = tbInvoiceItems.getSelectionModel().getSelectedItem();
        if (tempBillDetails_ != null) {
            if (event.getCode() == KeyCode.DELETE) {
                Alert confirmAlert = CommonFeature.getConfirmAlert("Invoice - Save");
                confirmAlert.setContentText("Do you want to remove item no :- " + tempBillDetails_.getIndex());
                if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                    this.tempBillDetails.remove(tempBillDetails_.getIndex() - 1);
                    index = 1;
                    for (TempBillDetails tempBillDetails1 : tempBillDetails) {
                        tempBillDetails1.setIndex(index);
                        index++;
                    }

                }
            }
        }
    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        Alert confirmAlert = CommonFeature.getConfirmAlert("Invoice Form-Clear");
        confirmAlert.setContentText("Do you want to clear this form?");
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            resetForm();
        }

    }

    @FXML
    private void txtNetPriceKR(KeyEvent event) {

    }

    private void addInvoiceItem(Double qtys) {
        if (roomTable.getId() != null) {

            TempBillDetails tempBillDetails_ = new TempBillDetails();
            tempBillDetails_.setItemId(item);
            tempBillDetails_.setQty(qtys.intValue());/* make double */

            tempBillDetails_.setPrice(item.getRetailPrice());
            tempBillDetails_.setSubtotal(BigDecimal.valueOf(item.getRetailPrice().doubleValue() * qtys));
            tempBillDetails_.setRoomTableId(roomTable);
            tempBillDetails.add(tempBillDetails_);
            index = 1;
            for (TempBillDetails tempBillDetails1 : tempBillDetails) {
                tempBillDetails1.setIndex(index);
                index++;
            }
        } else {
            CommonFeature.ERROR_ALERT.setContentText("Please select room or table.");
            CommonFeature.ERROR_ALERT.showAndWait();
        }

        paneInvoice.requestFocus();
        txtQty.setText("");
        txtNetPrice.setText("");

        txtItemName.setText("");
        paneInvoice.requestFocus();
        txtQty.setDisable(true);
        getTotal();
        tbInvoiceItems.scrollTo(tbInvoiceItems.getItems().size());
        discountedPrice = 0.0;
    }

    @FXML
    private void txtCusTelNoKR(KeyEvent event) {
        String telNo = txtCusTelNo.getText();
        if (event.getCode() == KeyCode.ENTER) {
            customer = CustomerDao.getCustomerByTelNo(telNo);
            if (customer != null) {
                lblCustomerName.setText(customer.getName());

                Alert confirmAlert = CommonFeature.getConfirmAlert("Invoice Form - Customer");
                confirmAlert.setContentText("Do you want to change customer?");
                if (roomTable.getId() != null && confirmAlert.showAndWait().get() == ButtonType.YES) {
                    roomTable.setCustomerId(customer);
                    CommonDao.updateObject(roomTable);
                }

            } else {
                txtCusTelNo.setText("");
                lblCustomerName.setText("UNKNOWN");
                customer = new Customer(1);
                Alert information = new Alert(Alert.AlertType.INFORMATION);
                information.setTitle("Information Message");
                information.setContentText("Not a registered customer");
                information.showAndWait();
            }
            paneInvoice.requestFocus();
        }
    }

    @FXML
    private void btnAddCusomerAP(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("CustomerUI.fxml"));
            Scene scene = new Scene(root);
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Customer Form");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            stage.setResizable(false);
        } catch (IOException ex) {
            CommonFeature.printException("Exception from InvoiceUIController.btnAddCusomerAP() ");
            System.out.println("Exception from InvoiceUIController.btnAddCusomerAP()" + ex.getMessage());
        }
    }

    @FXML
    private void cmbTableAP(ActionEvent event) {
        RoomTable roomTable_ = cmbTable.getSelectionModel().getSelectedItem();
        if (roomTable_ != null) {
            cmbRoom.getSelectionModel().select(-1);
            getSelectedRoomTable(roomTable_);
        }

    }

    @FXML
    private void cmbRoomAP(ActionEvent event) {

        RoomTable roomTable_ = cmbRoom.getSelectionModel().getSelectedItem();
        if (roomTable_ != null) {
            cmbTable.getSelectionModel().select(-1);
            getSelectedRoomTable(roomTable_);
        }

    }

    private void getSelectedRoomTable(RoomTable r) {

        parameter.put("roomTableId", r);
        saveTempBillDetails = CommonDao.getObjectObservableList("TempBillDetails.findByRoomTableId", parameter);
        tbTempBillDetailsSave.getItems().clear();

        tbTempBillDetailsSave.setItems(saveTempBillDetails);
        getTotal();
        invoice.setRoomTableId(r);
        roomTable = r;
        customer = r.getCustomerId();
        if (customer != null && customer.getId() > 1) {
            txtCusTelNo.setText(customer.getMobile());
            lblCustomerName.setText(customer.getName());
        } else {
            customer = new Customer(1);
            txtCusTelNo.setText("");
            lblCustomerName.setText("UNKNOWN");
        }

    }

    @FXML
    private void btnKOTSendAP(ActionEvent event) {

        Alert confirmAlert = CommonFeature.getConfirmAlert("Invoice Form");
        confirmAlert.setContentText("Do you want to send these items to kitchen?");
        String errorMsg = tempBillDetails.size() >= 1 ? "" : "Please add item to the invoice. \n";

        if (errorMsg.isEmpty() && confirmAlert.showAndWait().get() == ButtonType.YES) {

            for (TempBillDetails temp : tempBillDetails) {
                temp.setOrderNo(orderNo);
                CommonDao.insertObject(temp);
                /*print stuff*/
            }
            resetForm();

        } else {
            CommonFeature.ERROR_ALERT.setContentText("Invoice Form");
            CommonFeature.ERROR_ALERT.setContentText(errorMsg);
            CommonFeature.ERROR_ALERT.showAndWait();

        }

    }

    @FXML
    private void tbTempBillDetailsSaveMC(MouseEvent event
    ) {
    }

    @FXML
    private void tbTempBillDetailsSaveKR(KeyEvent event
    ) {
        TempBillDetails tempBillDetails_ = tbTempBillDetailsSave.getSelectionModel().getSelectedItem();
        if (tempBillDetails_ != null) {
            if (event.getCode() == KeyCode.DELETE) {
                Alert confirmAlert = CommonFeature.getConfirmAlert("Invoice - Save");
                confirmAlert.setContentText("Do you want to remove item no :- " + tempBillDetails_.getIndex());
                if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                    this.saveTempBillDetails.remove(tempBillDetails_.getIndex() - 1);
                    CommonDao.deleteObject(tempBillDetails_);
                    getTotal();

                }
            }
        }
    }
}
