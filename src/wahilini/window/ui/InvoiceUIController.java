package wahilini.window.ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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
import wahilini.window.dao.CustomerDao;
import wahilini.window.entity.Customer;
import wahilini.window.entity.Invoice;
import wahilini.window.entity.Invoiceitems;
import wahilini.window.entity.Item;

public class InvoiceUIController implements Initializable {

    @FXML
    private TableColumn colNo;
    @FXML
    private TableColumn colItemName;
    @FXML
    private TableColumn colUnitPrice;
    @FXML
    private TableColumn colSubTotal;
    @FXML
    private TableView<Invoiceitems> tbInvoiceItems;
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

    public static InvoiceUIController invoiceUIController;
    public static Stage stage;
    private Item item;
    public Invoice invoice;
    private boolean validityQty;
    private Double grandTotal;
    public Double netTotal;
    private double saleCost;
    private int index;
    private Customer customer;
    private double discountedPrice;
    private ObservableList<Invoiceitems> invoiceItems;

    @FXML
    private TextField txtItemName;

    @FXML
    public TextField txtNetPrice;

    private int validityTxtNetPrice;

    @FXML
    private Label lblCustTelNo;
    @FXML
    private TextField txtCusTelNo;
    @FXML
    private Label lblCustomerName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        MainWindowUIController.tabPane2.requestFocus();
        resetForm();
        invoiceUIController = this;

    }

    public void resetForm() {

        txtCusTelNo.setText("");
        lblCustomerName.setText("UNKNOWN");
        validityTxtNetPrice = 0;
        discountedPrice = 0.0;
        validityQty = true;
        txtQty.setDisable(true);
        customer = new Customer(1);
        invoice = new Invoice();
        invoiceItems = FXCollections.observableArrayList();
        txtServiceCharge.setText("0.00");
        txtGrandTotal.setText("0.00");
        txtNetTotal.setText("0.00");
        txtNoOfItems.setText("0.0");
        txtQty.setText("");
        txtItemName.setText("");

        btnSave.setText("Pay");
        lblQty.setText("Qty :-");
        txtNetPrice.setText("");
        invoice.setEmployeeId(LoginUIController.employee);
        paneInvoice.requestFocus();
        // Table Columns
        colItemName.setCellValueFactory(new PropertyValueFactory("itemId"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory("qty"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory("subTotal"));
        colNo.setCellValueFactory(new PropertyValueFactory<>("index"));
        tbInvoiceItems.setItems(invoiceItems);

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
        grandTotal = 0.0;
        netTotal = 0.0;

        saleCost = 0.0;

        for (Invoiceitems invoiceitem : invoiceItems) {
            invoiceitem.setIndex(index);

            grandTotal += invoiceitem.getSubTotal().doubleValue();
            index++;
        }
      
        netTotal = grandTotal ;
        txtGrandTotal.setText(CommonFeature.TO_DECI_FORMAT.format(grandTotal));
        txtServiceCharge.setText("");
        txtNetTotal.setText(CommonFeature.TO_DECI_FORMAT.format(netTotal));
        txtNoOfItems.setText(String.valueOf(invoiceItems.size()));
        invoice.setGrandTotal(BigDecimal.valueOf(grandTotal));
     //   invoice.setTotalDiscount(BigDecimal.valueOf(serviceCharge));
        invoice.setNetTotal(BigDecimal.valueOf(0.0));
    }

    @FXML
    private void txtQtyKR(KeyEvent event) {

        if (txtQty.getText().matches("[a-zA-Z]{1,}")) {
            txtQty.setText("");
        }
        int qtys = 0;
        if (item != null) {
            qtys = !txtQty.getText().isEmpty() && txtQty.getText().matches("[\\d]{1,3}") ? Integer.valueOf(txtQty.getText()) : 0;
        }

        if (validityQty && event.getCode() == KeyCode.ENTER) {
            if (item != null && qtys > 0.0) {
                addInvoiceItem(qtys);

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
        String errorMsg = invoiceItems.size() >= 1 ? "" : "Please add item to the invoice. \n";
        CommonFeature.ERROR_ALERT.setContentText("Do you want to save this invoice?");

        if (errorMsg.isEmpty()) {

            invoice.setCustomerId(customer);
            invoice.setInvoiceitemsList(invoiceItems);
            invoice.setInvTime(java.sql.Time.valueOf(LocalTime.now()));
            invoice.setInvDate(java.sql.Date.valueOf(LocalDate.now()));
            invoice.setSaleCost(BigDecimal.valueOf(saleCost));
            invoice.setRefundAmount(BigDecimal.ZERO);
            invoice.setRefundPayAmount(BigDecimal.ZERO);
            invoice.setTotalDiscount(BigDecimal.ZERO);

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
        ItemSearchUIController.controllerName = "Invoice";

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
        Invoiceitems invoiceItem = tbInvoiceItems.getSelectionModel().getSelectedItem();
        if (invoiceItem != null) {
            if (event.getCode() == KeyCode.DELETE) {
                Alert confirmAlert = CommonFeature.getConfirmAlert("Invoice - Save");
                confirmAlert.setContentText("Do you want to remove item no :- " + invoiceItem.getIndex());
                if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                    this.invoiceItems.remove(invoiceItem.getIndex() - 1);
                    getTotal();
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

   

    private void addInvoiceItem(double qtys) {
        Invoiceitems invoiceitem = new Invoiceitems();
        invoiceitem.setItemId(item);
        invoiceitem.setInvoiceid(invoice);
        invoiceitem.setQty(BigDecimal.valueOf(qtys));
        invoiceitem.setUnitPrice(item.getRetailPrice());
        invoiceitem.setRtnQty(BigDecimal.ZERO);
        invoiceitem.setRefundAmt(BigDecimal.ZERO);
        invoiceitem.setDiscount(BigDecimal.valueOf(0.0));
        invoiceitem.setSubTotal(BigDecimal.valueOf(invoiceitem.getUnitPrice().doubleValue() * invoiceitem.getQty().doubleValue()));

        invoiceItems.add(invoiceitem);
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

}
