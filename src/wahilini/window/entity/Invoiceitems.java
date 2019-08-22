package wahilini.window.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import wahilini.window.ui.CommonFeature;

@Entity
@Table(name = "invoice_items")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoiceitems.findAll", query = "SELECT i FROM Invoiceitems i"),
    @NamedQuery(name = "Invoiceitems.findById", query = "SELECT i FROM Invoiceitems i WHERE i.invoiceid = :invoice"),
    @NamedQuery(name = "Invoiceitems.findByInvNo", query = "SELECT i FROM Invoiceitems i WHERE i.invoiceid.id = :invNo AND i.invoiceid.crudStatus.id = 1")})
public class Invoiceitems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "sub_total")
    private BigDecimal subTotal;
    @Column(name = "qty")
    private BigDecimal qty;
    @Column(name = "discount")
    private BigDecimal discount;
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Invoice invoiceid;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Item itemId;
    @Column(name = "rtn_qty")
    private BigDecimal rtnQty;
    @Column(name = "refund_amount")
    private BigDecimal refundAmt;

    @Transient
    private Integer index;
    @Transient
    private double tempRefundQty;

    public Integer getIndex() {
        return index;
    }

    public double getTempRefundQty() {
        return tempRefundQty;
    }

    public void setTempRefundQty(double tempRefundQty) {
        this.tempRefundQty = tempRefundQty;
    }

    public BigDecimal getRefundAmt() {
        return refundAmt;
    }

    public void setRefundAmt(BigDecimal refundAmt) {
        this.refundAmt = refundAmt;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Invoiceitems() {
    }

    public BigDecimal getRtnQty() {
        return rtnQty;
    }

    public void setRtnQty(BigDecimal rtnQty) {
        this.rtnQty = rtnQty;
    }

    public Invoiceitems(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        double rdUpValue = Double.valueOf(CommonFeature.TO_DECI_FORMAT.format(subTotal.doubleValue()));
        this.subTotal = BigDecimal.valueOf(rdUpValue);
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        double rdUpValue = Double.valueOf(CommonFeature.TO_DECI_FORMAT.format(discount.doubleValue()));
        this.discount = BigDecimal.valueOf(rdUpValue);
    }

    public Invoice getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(Invoice invoiceid) {
        this.invoiceid = invoiceid;
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
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
        if (!(object instanceof Invoiceitems)) {
            return false;
        }
        Invoiceitems other = (Invoiceitems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Invoiceitems[ id=" + id + " ]";
    }

}
