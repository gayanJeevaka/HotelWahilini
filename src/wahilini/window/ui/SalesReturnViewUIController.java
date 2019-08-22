package wahilini.window.ui;

import wahilini.window.dao.CommonDao;
import wahilini.window.dao.InvoiceDao;
import wahilini.window.entity.Invoice;
import wahilini.window.entity.Invoiceitems;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

// This Gift_center_application
public class SalesReturnViewUIController implements Initializable {

    @FXML
    private TableColumn colGrnNo;
    @FXML
    private TableColumn colDate;
    @FXML
    private TableColumn colEmployee;
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
    private TableColumn<?, ?> colGrandTotal;
    @FXML
    private TableColumn<?, ?> colDiscount;
    private HashMap hMap;
    @FXML
    private TableColumn colRefundAmt;
    @FXML
    private TableColumn<?, ?> colRQty;
    @FXML
    private TableColumn<?, ?> colSubRefundAmt;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetForm();
    }

    private void resetForm() {
        hMap = new HashMap();
        invoiceitemses = FXCollections.observableArrayList();
        invoices = InvoiceDao.getAllInvoicesByStatus();
        fillInvoiceTB(invoices);
        tbInvoiceItems.getItems().clear();
    }

    private void fillInvoiceTB(ObservableList<Invoice> invoices) {
        colDate.setCellValueFactory(new PropertyValueFactory("invDate"));
        colEmployee.setCellValueFactory(new PropertyValueFactory("employeeId"));
        colGrnNo.setCellValueFactory(new PropertyValueFactory("invNo"));
        colDiscount.setCellValueFactory(new PropertyValueFactory("totalDiscount"));
        colGrandTotal.setCellValueFactory(new PropertyValueFactory("grandTotal"));
        colNetTotal.setCellValueFactory(new PropertyValueFactory("netTotal"));
        colRefundAmt.setCellValueFactory(new PropertyValueFactory("refundAmount"));

        tbInvoice.setItems(invoices);
    }

    private void fillInvoiceItemsTB(ObservableList<Invoiceitems> invoiceitemses) {
        colNo.setCellValueFactory(new PropertyValueFactory("index"));
        colCost.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colItemName.setCellValueFactory(new PropertyValueFactory("itemId"));
        colQty.setCellValueFactory(new PropertyValueFactory("qty"));
        colSubTotal.setCellValueFactory(new PropertyValueFactory("subTotal"));
        colRQty.setCellValueFactory(new PropertyValueFactory("rtnQty"));
        colSubRefundAmt.setCellValueFactory(new PropertyValueFactory("refundAmt"));
        tbInvoiceItems.setItems(invoiceitemses);
    }

    @FXML
    private void invoiceStartDateAP(ActionEvent event) {
        LocalDate stDate = invoiceStartDate.getValue();
        LocalDate enDate = invoiceEndDate.getValue();
        if (stDate != null && enDate != null) {
            startEndDateSearch(stDate, enDate);
            tbInvoiceItems.getItems().clear();
        }

    }

    @FXML
    private void invoiceEndDateAP(ActionEvent event) {
        LocalDate stDate = invoiceStartDate.getValue();
        LocalDate enDate = invoiceEndDate.getValue();
        if (stDate != null && enDate != null) {
            startEndDateSearch(stDate, enDate);
            tbInvoiceItems.getItems().clear();
        }

    }

    private void startEndDateSearch(LocalDate stDate, LocalDate enDate) {

        hMap.put("startDate", java.sql.Date.valueOf(stDate));
        hMap.put("endDate", java.sql.Date.valueOf(enDate));
        invoices = CommonDao.getObjectObservableList("Invoice.findAllByDateAndRefundAmt", hMap);
        tbInvoice.setItems(invoices);

    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        resetForm();
        invoiceStartDate.setValue(null);
        invoiceEndDate.setValue(null);
    }

    @FXML
    private void tbInvoiceMC(MouseEvent event) {
        Invoice inv = tbInvoice.getSelectionModel().getSelectedItem();
        if (inv != null) {
            hMap.put("invoice", inv);
            invoiceitemses = CommonDao.getObjectObservableList("Invoiceitems.findById", hMap);
            int i = 1;
            for (Invoiceitems invoiceitems : invoiceitemses) {
                invoiceitems.setIndex(i);
                i++;
            }
            fillInvoiceItemsTB(invoiceitemses);
        }

    }

}
