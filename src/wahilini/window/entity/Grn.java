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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "grn")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grn.findAll", query = "SELECT g FROM Grn g ORDER BY g.id DESC"),
    @NamedQuery(name = "Grn.findByDate", query = "SELECT g FROM Grn g WHERE g.grnDate BETWEEN :startDate AND :endDate")})
public class Grn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "grn_date")
    @Temporal(TemporalType.DATE)
    private Date grnDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Supplier supplierId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grnId")
    private List<GrnItems> grnItemsList;
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Employee employeeId;

    @Column(name = "supplier_name")
    private String supplierName;
    @JoinColumn(name = "crud_status_id")
    @ManyToOne
    private CrudStatus crudStatus;

    public Grn() {
    }

    public Grn(Integer id) {
        this.id = id;
    }

    public CrudStatus getCrudStatus() {
        return crudStatus;
    }

    public void setCrudStatus(CrudStatus crudStatus) {
        this.crudStatus = crudStatus;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getGrnDate() {
        return grnDate;
    }

    public Employee getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Employee employeeId) {
        this.employeeId = employeeId;
    }

    public void setGrnDate(Date grnDate) {
        this.grnDate = grnDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Supplier getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Supplier supplierId) {
        this.supplierId = supplierId;
    }

    @XmlTransient
    public List<GrnItems> getGrnItemsList() {
        return grnItemsList;
    }

    public void setGrnItemsList(List<GrnItems> grnItemsList) {
        this.grnItemsList = grnItemsList;
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
        if (!(object instanceof Grn)) {
            return false;
        }
        Grn other = (Grn) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Grn[ id=" + id + " ]";
    }

}
