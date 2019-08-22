package wahilini.window.ui;

import wahilini.window.dao.CommonDao;
import wahilini.window.dao.DBManager;
import wahilini.window.dao.ItemDao;
import wahilini.window.entity.Invoice;
import wahilini.window.entity.Invoiceitems;
import wahilini.window.entity.Item;
import wahilini.window.entity.SalesReturn;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;

public class SalesReturnUIController implements Initializable {

    @FXML
    private TextField txtxInvoiceNo;
    @FXML
    private TableView<Invoiceitems> tbInvoiceItems;
    @FXML
    private TableColumn<?, ?> colNo;
    @FXML
    private TableColumn<?, ?> colItem;
    @FXML
    private TableColumn<?, ?> colQty;
    @FXML
    private TableColumn<?, ?> colDiscount;
    @FXML
    private TableColumn<?, ?> colSubtotal;
    private ObservableList<Invoiceitems> Invoiceitems;
    private HashMap hashMap;
    @FXML
    private TableColumn<Invoiceitems, NumberTextField> colReturn;
    @FXML
    private TableColumn colRtnAmt;
    @FXML
    private TextField txtTotalRefundAmt;
    @FXML
    private Button btnSave;

    @FXML
    private TableColumn<?, ?> colUnitPrice;
    private Invoice invoice;
    private Double refundAmt;
    @FXML
    private TextField txtPaidAmt;
    @FXML
    private Label lblInvDate;
    @FXML
    private Label lblInvTime;
    @FXML
    private Label lblTaken;
    @FXML
    private Label lblNetTotal;
    @FXML
    private Label lblDiscount;
    @FXML
    private Label lblGrandTotal;
    @FXML
    private Label lblInvNo;
    @FXML
    private Label lblInvoiceNo;
    private SalesReturn salesReturn;
    @FXML
    private AnchorPane paneSRtn;
    private int count;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetForm();
    }

    private void resetForm() {
        salesReturn = new SalesReturn();
        salesReturn.setEmployeeId(LoginUIController.employee);
        refundAmt = 0.0;
        count = 0;
        txtTotalRefundAmt.setText("");
        txtPaidAmt.setText("");
        txtxInvoiceNo.setText("");
        invoice = new Invoice();
        hashMap = new HashMap();
        Invoiceitems = FXCollections.observableArrayList();
        tbInvoiceItems.getItems().clear();
        lblInvoiceNo.setText("-- Enter Invoice No --");
        lblDiscount.setText("-- Enter Invoice No --");
        lblGrandTotal.setText("-- Enter Invoice No --");
        lblInvDate.setText("-- Enter Invoice No --");
        lblInvTime.setText("-- Enter Invoice No --");
        lblNetTotal.setText("-- Enter Invoice No --");
        lblTaken.setText("-- Enter Invoice No --");
        txtxInvoiceNo.requestFocus();
    }

    @FXML
    private void txtxInvoiceNoKR(KeyEvent event) {
        String invNo = txtxInvoiceNo.getText();

        int index = 1;
        if (!invNo.isEmpty() && event.getCode() == KeyCode.ENTER) {
            hashMap.put("invNo", Integer.valueOf(invNo));
            Invoiceitems = CommonDao.getObjectObservableList("Invoiceitems.findByInvNo", hashMap);
            if (Invoiceitems.size() > 0) {
                for (Invoiceitems invoiceitems : Invoiceitems) {
                    invoiceitems.setIndex(index);
                    invoiceitems.setTempRefundQty(invoiceitems.getRtnQty().doubleValue());
                    invoice = invoiceitems.getInvoiceid();
                    index++;
                }
                txtPaidAmt.setText(invoice.getNetTotal().toString());
                lblInvoiceNo.setText(invoice.getInvNo());
                lblDiscount.setText(invoice.getTotalDiscount().toString());
                lblGrandTotal.setText(invoice.getGrandTotal().toString());
                lblInvDate.setText(invoice.getInvDate().toString());
                lblInvTime.setText(invoice.getInvTime().toString());
                lblNetTotal.setText(invoice.getNetTotal().toString());
                lblTaken.setText(invoice.getEmployeeId().getName());
                fillInvoiceItemsTB(Invoiceitems);
                txtxInvoiceNo.setText("");
                

            } else {
                CommonFeature.ERROR_ALERT.setHeaderText("Sales Return - Invoice Number");
                CommonFeature.ERROR_ALERT.setContentText("This Invoice is already returned or \n"
                        + "Invalid invoice No or \nThis invoice is canceled.");
                CommonFeature.ERROR_ALERT.showAndWait();
                resetForm();
            }
        }
    }

    private void fillInvoiceItemsTB(ObservableList<Invoiceitems> invoiceitemses) {

        colItem.setCellValueFactory(new PropertyValueFactory("itemId"));
        colNo.setCellValueFactory(new PropertyValueFactory("index"));
        colDiscount.setCellValueFactory(new PropertyValueFactory("discount"));
        colQty.setCellValueFactory(new PropertyValueFactory("qty"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory("subTotal"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colReturn.setCellValueFactory((TableColumn.CellDataFeatures<Invoiceitems, NumberTextField> param) -> {
            Invoiceitems invoiceitem = param.getValue();
            NumberTextField txtRtnQty = new NumberTextField();
            txtRtnQty.setPromptText(param.getValue().getRtnQty().toString());
            double refundQty = invoiceitem.getTempRefundQty();

            txtRtnQty.setOnKeyReleased(event -> {

                invoice = invoiceitem.getInvoiceid();
                double rtnQty = Double.valueOf(txtRtnQty.getText().matches("[0-9]{1,}") ? txtRtnQty.getText() : "0.0");
                double subTotal = invoiceitem.getSubTotal().doubleValue();
                double discount = invoiceitem.getDiscount().doubleValue();
                double unitValue = (subTotal - discount) / invoiceitem.getQty().doubleValue();

                if (event.getCode() == KeyCode.ENTER) {
                    count++;
                    if (rtnQty >= 0 && (refundQty + rtnQty) <= invoiceitem.getQty().doubleValue()) {
                        invoiceitem.setRtnQty(BigDecimal.valueOf(refundQty + rtnQty));
                        invoiceitem.setRefundAmt(BigDecimal.valueOf((refundQty + rtnQty) * unitValue));
                        getRefundTotal();
                        colDiscount.setVisible(false);
                        colDiscount.setVisible(true);
                        paneSRtn.requestFocus();

                    } else {
                        if (count % 2 == 1) {
                            CommonFeature.ERROR_ALERT.setHeaderText("Sales Return - Rtn Quantity");
                            CommonFeature.ERROR_ALERT.setContentText("Invalid quantity you have given, check again!");
                            CommonFeature.ERROR_ALERT.show();

                        }
                    }
                }

            });

            return new SimpleObjectProperty<>(txtRtnQty);
        });
        colRtnAmt.setCellValueFactory(new PropertyValueFactory("refundAmt"));

        tbInvoiceItems.setItems(invoiceitemses);
        getRefundTotal();
    }

    @FXML
    private void btnSaveAP(ActionEvent event) {

        if (refundAmt > invoice.getRefundAmount().doubleValue()) {
            Alert confirmAlert = CommonFeature.getConfirmAlert("Sales Return - Save");
            confirmAlert.setContentText("Do you want to return of Ivoice No:- " + invoice.getInvNo());
            if (confirmAlert.showAndWait().get() == ButtonType.YES) {

                salesReturn.setInvoiceId(invoice);
                salesReturn.setRefundAmount(BigDecimal.valueOf(refundAmt - invoice.getRefundAmount().doubleValue()));
                salesReturn.setRtnDate(java.sql.Date.valueOf(LocalDate.now()));
                salesReturn.setRtnTime(java.sql.Time.valueOf(LocalTime.now()));
                CommonDao.insertObject(salesReturn);
                invoice.setInvoiceitemsList(Invoiceitems);
                invoice.setRefundPayAmount(BigDecimal.valueOf(refundAmt - invoice.getRefundAmount().doubleValue()));
                invoice.setRefundAmount(BigDecimal.valueOf(refundAmt));
                for (Invoiceitems i : Invoiceitems) {
                    if (i.getRtnQty().doubleValue() > 0.0) {
                        Item item = i.getItemId();
                        item.setStock(BigDecimal.valueOf(ItemDao.getStoockValueById(item) + i.getRtnQty().doubleValue()));
                        ItemDao.updateItem(item);
                    }
                }

                CommonDao.updateObject(invoice);
                String path = System.getProperty("user.dir") + "\\reports\\SalesRtn.jrxml";
                HashMap params = new HashMap();
                params.put("invoiceId", invoice.getId());
                params.put("SUBREPORT_DIR", System.getProperty("user.dir") + "\\reports\\");
                System.out.println(System.getProperty("user.dir") + "\\reports\\");
                try {
                    JasperReport jasperreports = JasperCompileManager.compileReport(path);
                    JasperPrint jasperprint1 = JasperFillManager.fillReport(jasperreports, params, DBManager.JDBC_CONNECTION);
                    JasperPrintManager.printReport(jasperprint1, false);
                    Alert information = new Alert(Alert.AlertType.INFORMATION);
                    information.setTitle("Information Message");
                    information.setContentText("Invoice :- " + invoice.getId() + " Successfully saved!");
                    resetForm();
                    information.showAndWait();
                } catch (JRException ex) {
                    CommonFeature.printException("Exception from SalesReturnUIController.btnSaveAP() sales rtn ");
                    System.out.println("Exception from SalesReturnUIController.btnSaveAP() sales rtn \n" + ex.getMessage());
                }
            }
        } else {
            CommonFeature.ERROR_ALERT.setHeaderText("Sales Return - Save");
            CommonFeature.ERROR_ALERT.setContentText("No items return");
            CommonFeature.ERROR_ALERT.show();
        }

    }

    private void getRefundTotal() {
        refundAmt = 0.0;
        Double qty = 0.0;

        for (Invoiceitems invoiceitems : Invoiceitems) {
            qty = invoiceitems.getQty().doubleValue() - invoiceitems.getRtnQty().doubleValue();
            refundAmt += invoiceitems.getRefundAmt().doubleValue();
            // invoiceitems.setRefundAmt(BigDecimal.ZERO);

        }
        txtTotalRefundAmt.setText(CommonFeature.TO_DECI_FORMAT.format(refundAmt));
    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        Alert confirmAlert = CommonFeature.getConfirmAlert("Sales Return Form - Clear");
        confirmAlert.setContentText("Do you want to clear this form?");
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            resetForm();
        }
    }

    @FXML
    private void paneSRtnKP(KeyEvent event) {

        if (event.getCode().equals(KeyCode.CONTROL)) {
            btnSave.setText("Save (F12)");
            lblInvNo.setText("Inv-No(Ctrl+I):");

        }

    }

    @FXML
    private void paneSRtnKR(KeyEvent event) {

        if (event.getCode().equals(KeyCode.CONTROL)) {
            btnSave.setText("Save");
            lblInvNo.setText("Invoice No:-");
        }
        if (event.getCode() == KeyCode.I && event.isControlDown()) {
            txtxInvoiceNo.requestFocus();
            btnSave.setText("Save");
        }
        if (event.getCode() == KeyCode.F12) {
            btnSave.fire();
        }

    }

    @FXML
    private void paneSRtnMC(MouseEvent event) {
        paneSRtn.requestFocus();
    }
}
