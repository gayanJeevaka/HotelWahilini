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
@Table(name = "grn_items")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrnItems.findAll", query = "SELECT g FROM GrnItems g"),
  @NamedQuery(name = "GrnItems.findByGrnId", query = "SELECT g FROM GrnItems g WHERE g.grnId = :grnId")})
public class GrnItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;
    @Column(name = "qty")
    private BigDecimal qty;
    @Column(name = "sub_total")
    private BigDecimal subTotal;
    @JoinColumn(name = "item_Id", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Item itemId;
    @JoinColumn(name = "grn_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Grn grnId;
    @Transient
    private Integer index;

    public GrnItems() {
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public GrnItems(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        double rdUpValue = Double.valueOf(CommonFeature.TO_DECI_FORMAT.format(subTotal.doubleValue()));
        this.subTotal = BigDecimal.valueOf(rdUpValue);
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
    }

    public Grn getGrnId() {
        return grnId;
    }

    public void setGrnId(Grn grnId) {
        this.grnId = grnId;
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
        if (!(object instanceof GrnItems)) {
            return false;
        }
        GrnItems other = (GrnItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.GrnItems[ id=" + id + " ]";
    }

}
