package wahilini.window.ui;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import wahilini.window.dao.CommonDao;
import wahilini.window.dao.DBManager;
import wahilini.window.dao.InvoiceDao;
import wahilini.window.entity.CrudStatus;
import wahilini.window.entity.Invoice;
import wahilini.window.entity.Invoiceitems;
import wahilini.window.entity.RoomTable;
import wahilini.window.entity.TempBillDetails;
import static wahilini.window.ui.InvoiceUIController.invoiceUIController;
import static wahilini.window.ui.ItemSearchUIController.controllerName;
import static wahilini.window.ui.RoomBillInvoiceUIController.roomBillinvoiceUIController;

public class InvoiceBalanceUIController implements Initializable {

    @FXML
    private TextField txtCash;
    @FXML
    private Label lblNettotal;
    @FXML
    private Label lblBalance;
    @FXML
    private AnchorPane paneBalance;
    @FXML
    private Button btnPay;

    @FXML
    private Label lblSubTotal;

    private double netTotal;
    private double payAmt;
    private double balance;
    private double discount;
    private double serviceCharges;
    private int timesOfErrMsg;

    @FXML
    private RadioButton radioCash;
    @FXML
    private ToggleGroup tgPayType;

    @FXML
    private RadioButton radioCard;
    @FXML
    private TextField txtDiscount;
    private List<Invoiceitems> invoiceItems;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        netTotal = 0.0;
        payAmt = 0.0;
        balance = 0.0;
        serviceCharges = 0.0;
        timesOfErrMsg = 1;
        lblSubTotal.setText("" + netTotal);

        lblNettotal.setText(netTotal + "");
        txtDiscount.setText("");
        invoiceItems = new ArrayList();
        getInvoiceBalance();

    }

    private boolean getInvoiceBalance() {

        if (controllerName.equals("Invoice")) {
            netTotal = InvoiceUIController.invoiceUIController.netTotal;
            lblSubTotal.setText("" + netTotal);
            double temDiscount = 0.0;
            try {
                payAmt = txtCash.getText().matches("[\\d]{1,6}[.]{0,1}[\\d]{0,2}") ? Double.valueOf(txtCash.getText()) : 0.0;
                temDiscount = txtDiscount.getText().matches("[\\d]{1,2}") ? Double.valueOf(txtDiscount.getText()) : 0.0;
                if (temDiscount > 0.0) {
                    discount = netTotal * (temDiscount / 100);
                    netTotal = netTotal - discount;

                }
                if (radioCard.isSelected()) {
                    payAmt = netTotal;
                }
                balance = payAmt - netTotal;
                lblNettotal.setText(CommonFeature.TO_DECI_FORMAT.format(netTotal));
                lblBalance.setText(CommonFeature.TO_DECI_FORMAT.format(balance));

            } catch (NullPointerException ex) {
                CommonFeature.getAlertInformation("Your current work, start again!");
                InvoiceUIController.stage.close();
                CommonFeature.printException("Exception from InvoiceBalanceUIController getInvoiceBalance()");
                System.out.println("Exception from InvoiceBalanceUIController getInvoiceBalance() \n" + ex.getMessage());
            }
            return payAmt >= netTotal;
        } else {
            netTotal = roomBillinvoiceUIController.netTotal;
            lblSubTotal.setText("" + netTotal);
            double temDiscount = 0.0;
            try {
                payAmt = txtCash.getText().matches("[\\d]{1,6}[.]{0,1}[\\d]{0,2}") ? Double.valueOf(txtCash.getText()) : 0.0;
                temDiscount = txtDiscount.getText().matches("[\\d]{1,2}") ? Double.valueOf(txtDiscount.getText()) : 0.0;
                serviceCharges = netTotal / 11;
                if (temDiscount > 0.0) {
                    discount = netTotal * (temDiscount / 100);
                    netTotal = netTotal - discount;
                }
                if (radioCard.isSelected()) {
                    payAmt = netTotal;
                }
                balance = payAmt - netTotal;
                lblNettotal.setText(CommonFeature.TO_DECI_FORMAT.format(netTotal));
                lblBalance.setText(CommonFeature.TO_DECI_FORMAT.format(balance));

            } catch (NullPointerException ex) {
                CommonFeature.getAlertInformation("Your current work, start again!");
                RoomBillInvoiceUIController.stage.close();
                CommonFeature.printException("Exception from InvoiceBalanceUIController getInvoiceBalance()");
                System.out.println("Exception from InvoiceBalanceUIController getInvoiceBalance() \n" + ex.getMessage());
            }
            return payAmt >= netTotal;
        }
    }

    @FXML
    private void btnCancelAp(ActionEvent event) {
        if (controllerName.equals("Invoice")) {
            InvoiceUIController.stage.close();
        } else if (controllerName.equals("RoomTableInvoice")) {
            RoomBillInvoiceUIController.stage.close();
        }

    }

    @FXML
    private void txtCashKR(KeyEvent event) {
        getInvoiceBalance();
        if (event.getCode() == KeyCode.ENTER) {
            btnPay.fire();
        }
    }

    @FXML
    private void btnPayAP(ActionEvent event) throws JRException {

        switch (controllerName) {
            case "Invoice": {
                Invoice invoice = invoiceUIController.invoice;
                insertInvoice(invoice);
                break;
            }
            case "RoomTableInvoice": {
                Invoice invoice = roomBillinvoiceUIController.invoice;
                insertInvoice(invoice);
                break;
            }
        }
    }

    private void insertInvoice(Invoice invoice) throws JRException {

        try {
            Alert confirmAlert = CommonFeature.getConfirmAlert("Invoice Form - Pay");
            confirmAlert.setContentText("Do you want to pay this invoice?");
            if (getInvoiceBalance() && confirmAlert.showAndWait().get() == ButtonType.YES) {
                invoice.setPaidAmount(BigDecimal.valueOf(payAmt));
                invoice.setBalanceAmount(BigDecimal.valueOf(balance));
                invoice.setNetTotal(BigDecimal.valueOf(netTotal));
                invoice.setPaymentType(radioCash.isSelected() ? "Cash" : "Card");
                invoice.setGrandTotal(BigDecimal.valueOf(netTotal + discount));
                String invoiceNo = String.valueOf(InvoiceDao.getInvoiceID() + 1);
                invoice.setInvNo(invoiceNo);
                invoice.setServiceCharge(BigDecimal.ZERO);
                invoice.setTotalDiscount(BigDecimal.valueOf(discount));
                invoice.setCrudStatus(new CrudStatus(1));

                if (controllerName.equals("RoomTableInvoice")) {
                    RoomTable roomId = new RoomTable();
                    for (TempBillDetails temp : RoomBillInvoiceUIController.roomBillinvoiceUIController.saveTempBillDetails) {
                        Invoiceitems invoiceItem = new Invoiceitems();
                        invoiceItem.setItemId(temp.getItemId());
                        invoiceItem.setDiscount(BigDecimal.ZERO);
                        invoiceItem.setRtnQty(BigDecimal.ZERO);
                        invoiceItem.setRefundAmt(BigDecimal.ZERO);
                        invoiceItem.setInvoiceid(invoice);
                        invoiceItem.setQty(BigDecimal.valueOf(temp.getQty().doubleValue()));
                        invoiceItem.setUnitPrice(temp.getPrice());
                        invoiceItem.setSubTotal(temp.getSubtotal());
                        invoiceItems.add(invoiceItem);
                        roomId = temp.getRoomTableId();

                    }
                    /* Config */
                    invoice.setGrandTotal(BigDecimal.valueOf(serviceCharges / 0.1));
                    InvoiceDao.deleteTempBillDetails(roomId.getId());
                    roomId.setCustomerId(null);
                    CommonDao.updateObject(roomId);
                    invoice.setServiceCharge(BigDecimal.valueOf(serviceCharges));
                    invoice.setInvoiceitemsList(invoiceItems);
                    CommonDao.insertObject(invoice);
                    RoomBillInvoiceUIController.stage.close();
                    RoomBillInvoiceUIController.roomBillinvoiceUIController.resetForm();
                } else {
                    CommonDao.insertObject(invoice);
                    InvoiceUIController.stage.close();
                    InvoiceUIController.invoiceUIController.resetForm();
                }

                String path = System.getProperty("user.dir") + "\\reports\\Invoice.jrxml";

                PrinterJob pj = PrinterJob.getPrinterJob();
                PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
                System.out.println("Number of printers configured: " + printServices.length);
                for (PrintService printer : printServices) {

                    System.out.println("Printer: " + printer.getName());
                    if (printer.getName().equals("Microsoft XPS Document Writer")) {

                        try {
                            pj.setPrintService(printer);
                            HashMap parameter = new HashMap();
                            parameter.put("invoiceId", invoice.getId());
                            JasperReport jasperreports = JasperCompileManager.compileReport(path);
                            JasperPrint jasperprint1 = JasperFillManager.fillReport(jasperreports, parameter, DBManager.JDBC_CONNECTION);
                            JasperPrintManager.printReport(jasperprint1, false);
                            System.out.println(path + "----" + jasperreports.getCompilerClass());

                        } catch (PrinterException ex) {
                        }
                    }
                }

                Alert information = new Alert(Alert.AlertType.INFORMATION);
                information.setTitle("Information Message");
                information.setContentText("Invoice :- " + invoice.getInvNo() + " Successfully saved!");
                information.showAndWait();

            } else if (!getInvoiceBalance() && timesOfErrMsg % 2 == 1) {
                CommonFeature.ERROR_ALERT.setContentText("Invalid amount!");
                CommonFeature.ERROR_ALERT.showAndWait();

            }
        } catch (NullPointerException ex) {
            CommonFeature.printException("Exception invoiceBalance  btnPayAP " + ex.getMessage());
            System.out.println("Exception from invoiceBalance btnPayAP \n" + ex.getMessage());
        } finally {
            timesOfErrMsg++;
        }
    }

    @FXML
    private void paneBalanceAP(KeyEvent event) {
        if (controllerName.equals("Invoice") && event.getCode() == KeyCode.ESCAPE) {
            InvoiceUIController.stage.close();
        } else if (controllerName.equals("RoomTableInvoice") && event.getCode() == KeyCode.ESCAPE) {
            RoomBillInvoiceUIController.stage.close();
        }
    }

    private void txtCustomerKR(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER) {
            txtCash.requestFocus();
        }
    }

    @FXML
    private void radioPayTypeAP(ActionEvent event) {
        if (radioCard.isSelected()) {
            txtCash.setText("");
            txtCash.setDisable(true);
            payAmt = netTotal;
            balance = 0.0;
            lblBalance.setText("0.0");

        } else {
            txtCash.setText("");
            txtCash.setDisable(false);
            payAmt = 0.0;
            balance = 0.0;
            lblBalance.setText("0.0");
            txtCash.requestFocus();
        }
        getInvoiceBalance();
    }

    @FXML
    private void txtDiscountKR(KeyEvent event) {
        getInvoiceBalance();
        txtCash.setText("");
        if (event.getCode().equals(KeyCode.ENTER)) {
            txtCash.requestFocus();
        }

    }

}
