package wahilini.window.entity;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "room_table")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RoomTable.findAll", query = "SELECT r FROM RoomTable r"),
    @NamedQuery(name = "RoomTable.findById", query = "SELECT r FROM RoomTable r WHERE r.id = :id"),
    @NamedQuery(name = "RoomTable.findByName", query = "SELECT r FROM RoomTable r WHERE r.name = :name"),
    @NamedQuery(name = "RoomTable.findByIsRoom", query = "SELECT r FROM RoomTable r WHERE r.isRoom = true"),
    @NamedQuery(name = "RoomTable.findByIsTable", query = "SELECT r FROM RoomTable r WHERE r.isRoom = false")})
public class RoomTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "is_room")
    private Boolean isRoom;
    @OneToMany(mappedBy = "roomTableId")
    private List<TempBillDetails> tempBillDetailsList;

    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private Customer customerId;

    public RoomTable() {
    }

    public RoomTable(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsRoom() {
        return isRoom;
    }

    public void setIsRoom(Boolean isRoom) {
        this.isRoom = isRoom;
    }

    @XmlTransient
    public List<TempBillDetails> getTempBillDetailsList() {
        return tempBillDetailsList;
    }

    public void setTempBillDetailsList(List<TempBillDetails> tempBillDetailsList) {
        this.tempBillDetailsList = tempBillDetailsList;
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
        if (!(object instanceof RoomTable)) {
            return false;
        }
        RoomTable other = (RoomTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
