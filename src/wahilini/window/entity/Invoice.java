package wahilini.window.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "invoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i ORDER BY i.id DESC"),
    @NamedQuery(name = "Invoice.findByInvNo", query = "SELECT i FROM Invoice i WHERE i.invNo =:invNo"),
    @NamedQuery(name = "Invoice.findAllByDate", query = "SELECT i FROM Invoice i where i.invDate BETWEEN :startDate AND :endDate ORDER BY i.id DESC"),
    @NamedQuery(name = "Invoice.findAllByDateSize", query = "SELECT i FROM Invoice i where i.invDate=:invDate"),
    @NamedQuery(name = "Invoice.findAllByDate&Employee", query = "SELECT i FROM Invoice i where i.employeeId =:employeeId AND i.invDate=:invDate AND i.crudStatus.id=1"),
    @NamedQuery(name = "Invoice.findAllByDateAndRefundAmt", query = "SELECT i FROM Invoice i where i.refundAmount >0.0 AND (i.invDate BETWEEN :startDate AND :endDate) ORDER BY i.id DESC")})
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "inv_no")
    private String invNo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "grand_total")
    private BigDecimal grandTotal;
    @Column(name = "total_discount")
    private BigDecimal totalDiscount;
    @Column(name = "net_total")
    private BigDecimal netTotal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoiceid", orphanRemoval = true)
    private List<Invoiceitems> invoiceitemsList;
    @JoinColumn(name = "customer_Id", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Customer customerId;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeeId;
    @Column(name = "inv_date")
    @Temporal(TemporalType.DATE)
    private Date invDate;
    @Column(name = "paid_amount")
    private BigDecimal paidAmount;
    @Column(name = "balance_amount")
    private BigDecimal balanceAmount;
    @Column(name = "inv_time")
    private Time invTime;
    @Column(name = "sale_cost")
    private BigDecimal saleCost;
    @Column(name = "refund_amount")
    private BigDecimal refundAmount;
    @Column(name = "refund_pay_amount")
    private BigDecimal refundPayAmount;
    @Column(name = "pay_type")
    private String paymentType;

    @Column(name = "service_charge")
    private BigDecimal serviceCharge;
    @JoinColumn(name = "crud_status_id")
    @ManyToOne
    private CrudStatus crudStatus;

    @JoinColumn(name = "room_table_id", referencedColumnName = "id")
    @ManyToOne
    private RoomTable roomTableId;

    public RoomTable getRoomTableId() {
        return roomTableId;
    }

    public void setRoomTableId(RoomTable roomTableId) {
        this.roomTableId = roomTableId;
    }
    

    @Transient
    private Integer index;

    public String getPaymentType() {
        return paymentType;
    }

    public CrudStatus getCrudStatus() {
        return crudStatus;
    }

    public void setCrudStatus(CrudStatus crudStatus) {
        this.crudStatus = crudStatus;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getRefundPayAmount() {
        return refundPayAmount;
    }

    public void setRefundPayAmount(BigDecimal refundPayAmount) {
        this.refundPayAmount = refundPayAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Invoice() {
    }

    public BigDecimal getSaleCost() {
        return saleCost;
    }

    public void setSaleCost(BigDecimal saleCost) {
        this.saleCost = saleCost;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Time getInvTime() {
        return invTime;
    }

    public void setInvTime(Time invTime) {
        this.invTime = invTime;
    }

    public Invoice(Integer id) {
        this.id = id;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balAmount) {
        this.balanceAmount = balAmount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(BigDecimal netTotal) {
        this.netTotal = netTotal;
    }

    @XmlTransient
    public List<Invoiceitems> getInvoiceitemsList() {
        return invoiceitemsList;
    }

    public void setInvoiceitemsList(List<Invoiceitems> invoiceitemsList) {
        this.invoiceitemsList = invoiceitemsList;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return invNo;
    }

}
