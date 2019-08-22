package wahilini.window.ui;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import wahilini.window.dao.CommonDao;
import wahilini.window.dao.InvoiceDao;
import wahilini.window.entity.CrudStatus;
import wahilini.window.entity.Invoice;
import wahilini.window.entity.Invoiceitems;
import wahilini.window.entity.RoomTable;
import wahilini.window.entity.TempBillDetails;
import wahilini.window.report.ReportView;
import static wahilini.window.ui.LoginUIController.privileges;

public class InvoiceViewUIController implements Initializable {
    
    @FXML
    private TableColumn colGrnNo;
    @FXML
    private TableColumn colDate;
    @FXML
    private TableColumn colStatus;
    @FXML
    private TableColumn colNetTotal;
    
    @FXML
    private TableColumn colNo;
    @FXML
    private TableColumn colItemName;
    @FXML
    private TableColumn colCost;
    @FXML
    private TableColumn colQty;
    @FXML
    private TableColumn colSubTotal;
    
    private ObservableList<Invoiceitems> invoiceitemses;
    private ObservableList<Invoice> invoices;
    @FXML
    private DatePicker invoiceStartDate;
    @FXML
    private DatePicker invoiceEndDate;
    @FXML
    private TableView<Invoice> tbInvoice;
    @FXML
    private TableView<Invoiceitems> tbInvoiceItems;
    @FXML
    private TableColumn<Invoice, String> colTime;
    @FXML
    private TableColumn<?, ?> colGrandTotal;
    @FXML
    private TableColumn<?, ?> colDiscount;
    @FXML
    private TextField txtInvNo;
    private HashMap parameter;
    @FXML
    private Button btnInvoiceView;
    @FXML
    private Button btnDelete;
    
    @FXML
    private TextField txtNetSales;
    @FXML
    private TableColumn<?, ?> colServiceCharge;
    @FXML
    private TextField txtGrandTotal;
    @FXML
    private TextField txtDiscount;
    @FXML
    private TextField txtServiceCharge;
    @FXML
    private TableColumn<Invoice, String> colDescription;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetForm();
        fillInvoiceTB();
        btnDelete.setDisable(!privileges.get("Invoice_Form_delete"));
        
    }
    
    private void resetForm() {
        parameter = new HashMap();
        invoiceitemses = FXCollections.observableArrayList();
        invoices = FXCollections.observableArrayList();
        invoices.clear();
        invoiceStartDate.setValue(LocalDate.now());
        invoiceEndDate.setValue(LocalDate.now());
        parameter.put("startDate", Date.valueOf(invoiceStartDate.getValue()));
        parameter.put("endDate", Date.valueOf(invoiceEndDate.getValue()));
        invoices = CommonDao.getObjectObservableList("Invoice.findAllByDate", parameter);
        tbInvoiceItems.getItems().clear();
        tbInvoice.setItems(invoices);
        tbInvoiceItems.getItems().clear();
        txtInvNo.setText("");
        getIncome(Date.valueOf(invoiceStartDate.getValue()), Date.valueOf(invoiceEndDate.getValue()));
        
    }
    
    private void fillInvoiceTB() {
        colDate.setCellValueFactory(new PropertyValueFactory("invDate"));
        colTime.setCellValueFactory(new PropertyValueFactory("invTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory("crudStatus"));
        colGrnNo.setCellValueFactory(new PropertyValueFactory("invNo"));
        colDiscount.setCellValueFactory(new PropertyValueFactory("totalDiscount"));
        colGrandTotal.setCellValueFactory(new PropertyValueFactory("grandTotal"));
        colNetTotal.setCellValueFactory(new PropertyValueFactory("netTotal"));
        colServiceCharge.setCellValueFactory(new PropertyValueFactory("serviceCharge"));
        colDescription.setCellValueFactory(new PropertyValueFactory("roomTableId"));
        
        colDescription.setCellValueFactory((TableColumn.CellDataFeatures<Invoice, String> param) -> {
            RoomTable r = param.getValue().getRoomTableId();
            if (r == null) {
                return new SimpleObjectProperty<>("Take away");
            }
            return new SimpleObjectProperty<>(r.toString());
            
        });
        
    }
    
    private void fillInvoiceItemsTB(ObservableList<Invoiceitems> invoiceItemes) {
        colNo.setCellValueFactory(new PropertyValueFactory("index"));
        colCost.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colItemName.setCellValueFactory(new PropertyValueFactory("itemId"));
        colQty.setCellValueFactory(new PropertyValueFactory("qty"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory("subTotal"));
        
        tbInvoiceItems.setItems(invoiceItemes);
    }
    
    @FXML
    private void invoiceStartDateAP(ActionEvent event) {
        invoiceSearchByDate();
        
    }
    
    @FXML
    private void invoiceEndDateAP(ActionEvent event) {
        invoiceSearchByDate();
        
    }
    
    private void invoiceSearchByDate() {
        LocalDate stDate = invoiceStartDate.getValue();
        LocalDate enDate = invoiceEndDate.getValue();
        if (stDate != null && enDate != null) {
            parameter.put("startDate", java.sql.Date.valueOf(stDate));
            parameter.put("endDate", java.sql.Date.valueOf(enDate));
            invoices = CommonDao.getObjectObservableList("Invoice.findAllByDate", parameter);
            tbInvoice.setItems(invoices);
            txtInvNo.setText("");
            tbInvoiceItems.getItems().clear();
            getIncome(Date.valueOf(stDate), Date.valueOf(enDate));
        }
    }
    
    @FXML
    private void txtInvNoKR(KeyEvent event) {
        String invNo = txtInvNo.getText();
        invoiceEndDate.setValue(null);
        invoiceStartDate.setValue(null);
        if (!invNo.isEmpty() && event.getCode() == KeyCode.ENTER) {
            invoices = InvoiceDao.getInvoiceByInvNo(invNo);
            tbInvoice.setItems(invoices);
            tbInvoiceItems.getItems().clear();
            invoiceEndDate.setValue(null);
            invoiceStartDate.setValue(null);
            // getIncome();

            txtGrandTotal.setText("");
            txtServiceCharge.setText("");
            txtDiscount.setText("");
            txtNetSales.setText("");
        }
    }
    
    @FXML
    private void btnClearAP(ActionEvent event) {
        resetForm();
        
    }
    
    @FXML
    private void tbInvoiceMC(MouseEvent event) {
        Invoice inv = tbInvoice.getSelectionModel().getSelectedItem();
        if (inv != null) {
            parameter.put("invoice", inv);
            invoiceitemses = CommonDao.getObjectObservableList("Invoiceitems.findById", parameter);
            int i = 1;
            for (Invoiceitems invoiceitems : invoiceitemses) {
                invoiceitems.setIndex(i);
                i++;
            }
            fillInvoiceItemsTB(invoiceitemses);
        }
        
    }
    
    @FXML
    private void btnInvoiceViewAP(ActionEvent event) {
        Invoice invoice = tbInvoice.getSelectionModel().getSelectedItem();
        Alert confirmAlert = CommonFeature.getConfirmAlert("Invoice - View");
        if (invoice != null) {
            confirmAlert.setContentText("Do you want to view :- " + invoice.getId() + " ?");
            String workingDirectory = System.getProperty("user.dir");
            if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                String path2 = workingDirectory + "\\reports\\Invoice.jasper";
                parameter.put("invoiceId", invoice.getId());
                try {
                    ReportView.getReportView(path2, parameter);
                } catch (NullPointerException ex) {
                    CommonFeature.printException("Exception from InvoiceView.btnInvoiceViewAP() ");
                }
                
            }
            
        } else {
            CommonFeature.ERROR_ALERT.setHeaderText("Invoice-History View");
            CommonFeature.ERROR_ALERT.setContentText("Please select an invoice from the invoice details table");
            CommonFeature.ERROR_ALERT.showAndWait();
        }
        
    }
    
    @FXML
    private void btnDeleteAP(ActionEvent event) {
        
        Invoice invoice = tbInvoice.getSelectionModel().getSelectedItem();
        Alert confirmAlert = CommonFeature.getConfirmAlert("Invoice - Cancel");
        if (invoice != null && invoice.getCrudStatus().getId() == 1) {
            confirmAlert.setContentText("Do you want to cancel :- " + invoice.getInvNo() + " ?");
            if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                invoice.setCrudStatus(new CrudStatus(2));
                CommonDao.updateObject(invoice);
                             
                resetForm();
            }
        } else {
            CommonFeature.ERROR_ALERT.setHeaderText("Invoice - Cancel");
            CommonFeature.ERROR_ALERT.setContentText("Please select an invoice from the invoice details table \n"
                    + "or this invoice is already cancelled.");
            CommonFeature.ERROR_ALERT.showAndWait();
        }
    }
    
    private void getIncome(Date stDate, Date endDate) {
        
        List<Double> salesDetails = InvoiceDao.getInvoiceDetailsBydate(stDate, endDate);
        double grandTotal = salesDetails.get(0);
        double serviceCharge = salesDetails.get(1);
        double discount = salesDetails.get(2);
        double income = salesDetails.get(3);

//        for (Invoice i : invoices) {
//
//            if (i.getCrudStatus().getId() == 2) {
//                continue;
//            }
//            grandTotal += i.getGrandTotal().doubleValue();
//            serviceCharge += i.getServiceCharge().doubleValue();
//            discount += i.getTotalDiscount().doubleValue();
//            income += i.getNetTotal().doubleValue();
//
//        }
        txtGrandTotal.setText(CommonFeature.TO_DECI_FORMAT.format(grandTotal));
        txtServiceCharge.setText(CommonFeature.TO_DECI_FORMAT.format(serviceCharge));
        txtDiscount.setText(CommonFeature.TO_DECI_FORMAT.format(discount));
        txtNetSales.setText(CommonFeature.TO_DECI_FORMAT.format(income));
        
    }
}
