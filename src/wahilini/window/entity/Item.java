package wahilini.window.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i"),
    @NamedQuery(name = "Item.findById", query = "SELECT i FROM Item i WHERE i.id = :id"),
    @NamedQuery(name = "Item.findByItemName", query = "SELECT i FROM Item i WHERE i.itemName like :itemName"),
    @NamedQuery(name = "Item.findByCost", query = "SELECT i FROM Item i WHERE i.cost = :cost"),
    @NamedQuery(name = "Item.findByRetailPrice", query = "SELECT i FROM Item i WHERE i.retailPrice = :retailPrice")
})
public class Item implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cost")
    private BigDecimal cost;
    @Column(name = "retail_Price")
    private BigDecimal retailPrice;
    @Column(name = "stock")
    private BigDecimal stock;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "item_name")
    private String itemName;
    @JoinColumn(name = "item_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ItemType itemTypeId;

    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Category categoryId;
    @Column(name = "rop")
    private Integer rop;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemId")
    private List<Invoiceitems> invoiceitemsList;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "is_kot_item")
    private boolean isKotItem;
    
    private String remarks;

    public Item() {
    }

    public boolean isIsKotItem() {
        return isKotItem;
    }

    public void setIsKotItem(boolean isKotItem) {
        this.isKotItem = isKotItem;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getRop() {
        return rop;
    }

    public void setRop(Integer rop) {
        this.rop = rop;
    }

    public List<Invoiceitems> getInvoiceitemsList() {
        return invoiceitemsList;
    }

    public void setInvoiceitemsList(List<Invoiceitems> invoiceitemsList) {
        this.invoiceitemsList = invoiceitemsList;
    }

    public BigDecimal getStock() {
        return stock;
    }

    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    public Item(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public ItemType getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(ItemType itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public boolean setCategoryId(Category categoryId) {
        boolean validity = categoryId != null;
        if (validity) {
            this.categoryId = categoryId;
        }
        return validity;
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
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return itemName;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

}
