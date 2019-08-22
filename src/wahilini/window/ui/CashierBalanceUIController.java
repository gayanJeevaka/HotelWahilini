package wahilini.window.ui;

import wahilini.window.dao.CommonDao;
import wahilini.window.entity.Employee;
import wahilini.window.entity.Invoice;
import wahilini.window.entity.SalesReturn;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hibernate.HibernateException;

public class CashierBalanceUIController implements Initializable {

    @FXML
    private ComboBox<Employee> cmbEmployee;
    @FXML
    private DatePicker dteStartDate;
    @FXML
    private TextField txtCashIncome;
    @FXML
    private TableView<Invoice> tbInvoice;
    @FXML
    private TableColumn colNo;
    @FXML
    private TableColumn colInvoiceNo;
    @FXML
    private TableColumn colTime;
    @FXML
    private TableColumn colGrandTotal;
    @FXML
    private TableColumn colDiscount;
    @FXML
    private TableColumn colNetTotal;

    @FXML
    private TextField txtNetCashIncome;
    private ObservableList<Invoice> invoices;
    private HashMap parameter;
    @FXML
    private TableColumn<?, ?> colServiceCharges;
    @FXML
    private TextField txtCardIncome;
    @FXML
    private TableColumn<?, ?> colPayTyoe;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetForm();
    }

    private void resetForm() {
        parameter = new HashMap();
        cmbEmployee.setItems(CommonDao.getObjectObservableList("Employee.findAll"));
        cmbEmployee.getSelectionModel().select(-1);
        dteStartDate.setValue(LocalDate.now());
        tbInvoice.getItems().clear();
        txtCashIncome.setText("");
        txtCardIncome.setText("");
        txtNetCashIncome.setText("");
    }

    /* Invoice Table details and columns */
    private void fillInvoiceTable(ObservableList<Invoice> invoices) {
        colTime.setCellValueFactory(new PropertyValueFactory("invTime"));
        colNo.setCellValueFactory(new PropertyValueFactory("index"));
        colInvoiceNo.setCellValueFactory(new PropertyValueFactory("invNo"));
        colDiscount.setCellValueFactory(new PropertyValueFactory("totalDiscount"));
        colGrandTotal.setCellValueFactory(new PropertyValueFactory("grandTotal"));
        colNetTotal.setCellValueFactory(new PropertyValueFactory("netTotal"));
        colServiceCharges.setCellValueFactory(new PropertyValueFactory("serviceCharge"));
        colPayTyoe.setCellValueFactory(new PropertyValueFactory("paymentType"));

        tbInvoice.setItems(invoices);
    }

    @FXML
    private void cmbEmployeeAP(ActionEvent event) {
        if (dteStartDate.getValue() != null && cmbEmployee.getSelectionModel().getSelectedItem() != null) {
            getInvoiceDetailsByEmployee();
        }
    }

    @FXML
    private void dteStartDateAP(ActionEvent event) {
        if (dteStartDate.getValue() != null && cmbEmployee.getSelectionModel().getSelectedItem() != null) {
            getInvoiceDetailsByEmployee();
        } else if (dteStartDate.getValue() != null) {
            cmbEmployee.getSelectionModel().select(-1);
        }
    }

    private void getInvoiceDetailsByEmployee() {
        try {

            parameter.put("employeeId", cmbEmployee.getSelectionModel().getSelectedItem());
            parameter.put("invDate", java.sql.Date.valueOf(dteStartDate.getValue()));
            invoices = CommonDao.getObjectObservableList("Invoice.findAllByDate&Employee", parameter);
            int indexInv = 1;
            double totalCashIncome = 0.0;
            double totalCardIncome = 0.0;
            for (Invoice in : invoices) {
                in.setIndex(indexInv);
                indexInv++;
                if (in.getPaymentType().equals("Cash")) {
                    totalCashIncome += in.getNetTotal().doubleValue();
                    continue;
                }
                totalCardIncome += in.getNetTotal().doubleValue();

            }
            txtCardIncome.setText(CommonFeature.TO_DECI_FORMAT.format(totalCardIncome));
            txtCashIncome.setText(CommonFeature.TO_DECI_FORMAT.format(totalCashIncome));
            txtNetCashIncome.setText(CommonFeature.TO_DECI_FORMAT.format(totalCashIncome + totalCardIncome));

            fillInvoiceTable(invoices);

        } catch (HibernateException ex) {
            CommonFeature.printException("Exception from CashierBalanceUIController.getinvoiceSalesRtnDetails() ");
            System.out.println("Exception from CashierBalanceUIController.getinvoiceSalesRtnDetails()\n" + ex.getMessage());
            resetForm();
        }

    }

    @FXML
    private void btnClearAP(ActionEvent event) {
        resetForm();
    }

}
