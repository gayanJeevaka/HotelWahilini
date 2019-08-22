package wahilini.window.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "cheque_payment")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChequePayment.findAll", query = "SELECT s FROM ChequePayment s"),
    @NamedQuery(name = "ChequePayment.findById", query = "SELECT s FROM ChequePayment s WHERE s.id = :id"),
    @NamedQuery(name = "ChequePayment.findByChqno", query = "SELECT s FROM ChequePayment s WHERE s.chqno LIKE :chqno"),
    @NamedQuery(name = "ChequePayment.findByInvdate", query = "SELECT s FROM ChequePayment s WHERE s.invDate BETWEEN :startDate AND :endDate"),
    @NamedQuery(name = "ChequePayment.findByInvoiceID", query = "SELECT s FROM ChequePayment s WHERE s.invoiceId = :invoiceId"),
    @NamedQuery(name = "ChequePayment.findByChqamount", query = "SELECT s FROM ChequePayment s WHERE s.chqamount = :chqamount")})
public class ChequePayment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "chq_no")
    private String chqno;
    @Column(name = "chq_date")
    @Temporal(TemporalType.DATE)
    private Date chqdate;
    @Column(name = "inv_date")
    @Temporal(TemporalType.DATE)
    private Date invDate;
    @Column(name = "chq_amount")
    private BigDecimal chqamount;
    
    private boolean status;

    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Invoice invoiceId;

    public ChequePayment() {
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    public ChequePayment(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChqno() {
        return chqno;
    }

    public void setChqno(String chqno) {

        this.chqno = chqno;

    }

    public Date getChqdate() {
        return chqdate;
    }

    public void setChqdate(Date chqdate) {

        this.chqdate = chqdate;
    }

    public BigDecimal getChqamount() {
        return chqamount;
    }

    public void setChqamount(double chqamount) {

        this.chqamount = BigDecimal.valueOf(chqamount);

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
        if (!(object instanceof ChequePayment)) {
            return false;
        }
        ChequePayment other = (ChequePayment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return chqno;
    }

}
