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
@Table(name = "return_items")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReturnItems.findAll", query = "SELECT r FROM ReturnItems r"),
    @NamedQuery(name = "ReturnItems.findById", query = "SELECT r FROM ReturnItems r WHERE r.id = :id"),
    @NamedQuery(name = "ReturnItems.findByDamageRtnId", query = "SELECT r FROM ReturnItems r WHERE r.damageReturnId = :damageReturnId")})
public class ReturnItems implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qty")
    private BigDecimal qty;
    @Column(name = "unit_cost")
    private BigDecimal unitCost;
    @Column(name = "sub_total")
    private BigDecimal subTotal;
    @JoinColumn(name = "damage_return_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DamageReturn damageReturnId;

    @JoinColumn(name = "Item_Id", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Item itemId;
    @Transient
    private int index;

    public ReturnItems() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
    }

    public ReturnItems(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        double rdUpValue = Double.valueOf(CommonFeature.TO_DECI_FORMAT.format(subTotal.doubleValue()));
        this.subTotal = BigDecimal.valueOf(rdUpValue);
    }

    public DamageReturn getDamageReturnId() {
        return damageReturnId;
    }

    public void setDamageReturnId(DamageReturn damageReturnId) {
        this.damageReturnId = damageReturnId;
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
        if (!(object instanceof ReturnItems)) {
            return false;
        }
        ReturnItems other = (ReturnItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReturnItems[ id=" + id + " ]";
    }

}
