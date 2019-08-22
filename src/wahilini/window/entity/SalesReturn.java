package wahilini.window.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "sales_return")
@NamedQueries({
    @NamedQuery(name = "SalesReturn.findAll", query = "SELECT s FROM SalesReturn s"),
  
    @NamedQuery(name = "SalesReturn.findAllByDate&Emplyee", query = "SELECT s FROM SalesReturn s WHERE s.employeeId=:employeeId AND s.rtnDate =:rtnDate")
   })
public class SalesReturn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "refund_amount")
    private BigDecimal refundAmount;
    @Column(name = "rtn_date")
    @Temporal(TemporalType.DATE)
    private Date rtnDate;
    @Column(name = "rtn_time")
    private Time rtnTime;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeeId;
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Invoice invoiceId;
    @Transient
    private Integer index;

    public SalesReturn() {
    }

    public Time getRtnTime() {
        return rtnTime;
    }

    public void setRtnTime(Time rtnTime) {
        this.rtnTime = rtnTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public SalesReturn(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Date getRtnDate() {
        return rtnDate;
    }

    public void setRtnDate(Date rtnDate) {
        this.rtnDate = rtnDate;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public Invoice getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Invoice invoiceId) {
        this.invoiceId = invoiceId;
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
        if (!(object instanceof SalesReturn)) {
            return false;
        }
        SalesReturn other = (SalesReturn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SalesReturn[ id=" + id + " ]";
    }

}
