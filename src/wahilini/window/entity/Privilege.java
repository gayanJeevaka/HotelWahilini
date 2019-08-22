package wahilini.window.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "privilege")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Privilege.findAll", query = "SELECT p FROM Privilege p"),
    @NamedQuery(name = "Privilege.findByEmployeeId", query = "SELECT p FROM Privilege p WHERE p.sysUserId.employeeId =:employeeId"),
    @NamedQuery(name = "Privilege.findByPrivileges", query = "SELECT p FROM Privilege p WHERE p.privileges = :privileges")})
public class Privilege implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "privileges")
    private Integer privileges;
    @JoinColumn(name = "sys_user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SysUser sysUserId;
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Module moduleId;

    public Privilege() {
    }

    public Privilege(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Integer privileges) {
        this.privileges = privileges;
    }

    public SysUser getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(SysUser sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Module getModuleId() {
        return moduleId;
    }

    public void setModuleId(Module moduleId) {
        this.moduleId = moduleId;
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
        if (!(object instanceof Privilege)) {
            return false;
        }
        Privilege other = (Privilege) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Privilege[ id=" + id + " ]";
    }

}
