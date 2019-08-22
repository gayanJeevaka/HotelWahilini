package wahilini.window.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "damage_return")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DamageReturn.findAll", query = "SELECT d FROM DamageReturn d order by d.rtnDateTime DESC"),
    @NamedQuery(name = "DamageReturn.findById", query = "SELECT d FROM DamageReturn d WHERE d.id = :id"),
    @NamedQuery(name = "DamageReturn.findByRtnDateTime", query = "SELECT d FROM DamageReturn d WHERE d.rtnDateTime BETWEEN :startDate AND :endDate")})
public class DamageReturn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description")
    private String description;
    @Column(name = "rtn_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rtnDateTime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "damageReturnId")
    private List<ReturnItems> returnItemsList;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeeId;

    @Transient
    private int index;

    public DamageReturn() {
    }

    public DamageReturn(Integer id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRtnDateTime() {
        return rtnDateTime;
    }

    public void setRtnDateTime(Date rtnDateTime) {
        this.rtnDateTime = rtnDateTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @XmlTransient
    public List<ReturnItems> getReturnItemsList() {
        return returnItemsList;
    }

    public void setReturnItemsList(List<ReturnItems> returnItemsList) {
        this.returnItemsList = returnItemsList;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
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
        if (!(object instanceof DamageReturn)) {
            return false;
        }
        DamageReturn other = (DamageReturn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DamageReturn[ id=" + id + " ]";
    }

}
