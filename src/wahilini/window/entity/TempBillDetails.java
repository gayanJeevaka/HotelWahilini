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

@Entity
@Table(name = "temp_bill_details")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TempBillDetails.findAll", query = "SELECT t FROM TempBillDetails t"),
    @NamedQuery(name = "TempBillDetails.findById", query = "SELECT t FROM TempBillDetails t WHERE t.id = :id"),
    @NamedQuery(name = "TempBillDetails.findByItemId", query = "SELECT t FROM TempBillDetails t WHERE t.itemId = :itemId"),
    @NamedQuery(name = "TempBillDetails.findByQty", query = "SELECT t FROM TempBillDetails t WHERE t.qty = :qty"),
    @NamedQuery(name = "TempBillDetails.findByPrice", query = "SELECT t FROM TempBillDetails t WHERE t.price = :price"),
    @NamedQuery(name = "TempBillDetails.findBySubtotal", query = "SELECT t FROM TempBillDetails t WHERE t.subtotal = :subtotal"),
    @NamedQuery(name = "TempBillDetails.findByRoomTableId", query = "SELECT t FROM TempBillDetails t WHERE t.roomTableId = :roomTableId"),})
public class TempBillDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne
    private Item itemId;
   
    @Column(name = "qty")
    private Integer qty;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    @JoinColumn(name = "room_table_id", referencedColumnName = "id")
    @ManyToOne
    private RoomTable roomTableId;

    @Transient
    private int index;

    @Column(name = "order_no")
    private Integer orderNo;

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public TempBillDetails() {
    }

    public TempBillDetails(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public RoomTable getRoomTableId() {
        return roomTableId;
    }

    public void setRoomTableId(RoomTable roomTableId) {
        this.roomTableId = roomTableId;
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
        if (!(object instanceof TempBillDetails)) {
            return false;
        }
        TempBillDetails other = (TempBillDetails) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "wahilini.window.entity.TempBillDetails[ id=" + id + " ]";
    }

}
