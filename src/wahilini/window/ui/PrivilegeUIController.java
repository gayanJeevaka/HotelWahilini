package wahilini.window.ui;

import wahilini.window.dao.CommonDao;
import wahilini.window.entity.Employee;
import wahilini.window.entity.Module;
import wahilini.window.entity.Privilege;
import wahilini.window.entity.SysUser;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrivilegeUIController implements Initializable {

    private ObservableList<Privilege> privileges;
    @FXML
    private ComboBox<Employee> cmbEmployee;
    @FXML
    private TableView<Privilege> tbPrivilege;
    @FXML
    private TableColumn<?, ?> colModule;
    @FXML
    private TableColumn<Privilege, NumberTextField> colPrivilege;
    @FXML
    private Button btnChange;
    @FXML
    private Button btnClear;
    @FXML
    private ComboBox<String> cmbOperation;
    private HashMap parameter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetForm();
    }

    private void resetForm() {
        parameter = new HashMap();
        cmbOperation.setItems(FXCollections.observableArrayList("Create U/AC", "Reset U/AC", "Change Privilege", "Delete U/AC"));
        tbPrivilege.getItems().clear();
        cmbEmployee.setItems(CommonDao.getObjectObservableList("Employee.findAll"));
        privileges = FXCollections.observableArrayList();
        fillPrivilegeTB(privileges);
        cmbOperation.getSelectionModel().select(-1);
        cmbEmployee.getSelectionModel().select(-1);

    }

    private void fillPrivilegeTB(ObservableList<Privilege> privileges) {
        colModule.setCellValueFactory(new PropertyValueFactory("moduleId"));
        colPrivilege.setCellValueFactory((TableColumn.CellDataFeatures<Privilege, NumberTextField> param) -> {
            NumberTextField txtPrivilege = new NumberTextField();
            txtPrivilege.setText(param.getValue().getPrivileges().toString());
            txtPrivilege.setOnKeyReleased(evt -> {
                String value = txtPrivilege.getText();
                if (value.matches("[0-4]{1}")) {
                    txtPrivilege.setStyle(CommonFeature.COL_VALID);
                    param.getValue().setPrivileges(Integer.valueOf(value));
                } else {
                    txtPrivilege.setStyle(CommonFeature.COL_INVALID);
                    param.getValue().setPrivileges(0);
                }

            });
            return new SimpleObjectProperty(txtPrivilege);
        });
        tbPrivilege.setItems(privileges);

    }

    @FXML
    private void btnChangeAP(ActionEvent event) throws Exception {
        String operation = cmbOperation.getSelectionModel().getSelectedItem();
        Employee employee = cmbEmployee.getSelectionModel().getSelectedItem();
        ObservableList<SysUser> sysUsers;
        if (operation != null && employee != null) {
            sysUsers = CommonDao.getObjectObservableList("SysUser.findByEmployeeId", parameter);
            switch (operation) {

                case "Create U/AC": {
                    if (sysUsers.size() == 0) {
                        Alert confirmAlert = CommonFeature.getConfirmAlert("Create U/AC");
                        confirmAlert.setContentText("Do you want to create an U/AC for Employee:- " + employee.getName());
                        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                            SysUser user = new SysUser();
                            user.setEmployeeId(employee);
                            user.setUserName(employee.getNic());
                            user.setPassword(employee.getNic());
                            List<Privilege> privlegeses = new ArrayList();
                            ObservableList<Module> modeList = CommonDao.getObjectObservableList("Module.findAll");

                            for (Module modeule : modeList) {
                                Privilege privilege = new Privilege();
                                privilege.setPrivileges(0);
                                privilege.setSysUserId(user);
                                privilege.setModuleId(modeule);
                                privlegeses.add(privilege);
                            }
                            user.setPrivilegeList(privlegeses);
                            CommonDao.insertObject(user);
                            resetForm();
                            break;
                        }
                    } else {
                        CommonFeature.ERROR_ALERT.setHeaderText("Privilege & User A/C - Save");
                        CommonFeature.ERROR_ALERT.setContentText("This Employee already has an U/AC");
                        CommonFeature.ERROR_ALERT.showAndWait();
                        break;
                    }
                    break;
                }

                case "Change Privilege": {
                    System.out.println("You are here right now");
                    if (sysUsers.size() == 1) {
                        Alert confirmAlert = CommonFeature.getConfirmAlert("Change Privilege");

                        confirmAlert.setContentText("Do you want to change privileges of Employee:- " + employee.getName());
                        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                            for (Privilege privilege : tbPrivilege.getItems()) {
                                CommonDao.updateObject(privilege);
                            }
                            resetForm();
                        }
                        break;
                    } else {
                        CommonFeature.ERROR_ALERT.setHeaderText("Privilege & User A/C - Save");
                        CommonFeature.ERROR_ALERT.setContentText("This Employee doesn't has an U/AC to change");
                        CommonFeature.ERROR_ALERT.showAndWait();
                        break;
                    }

                }

                case "Reset U/AC": {

                    if (sysUsers.size() == 1) {
                        Alert confirmAlert = CommonFeature.getConfirmAlert("Reset U/AC");
                        confirmAlert.setContentText("Do you want to reset U/AC of Employee:- " + employee.getName());

                        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                            SysUser user = (SysUser) CommonDao.getObjectObservableList("SysUser.findByEmployeeId", parameter).get(0);
                            user.setUserName(employee.getNic());
                            user.setPassword(employee.getNic());
                            CommonDao.updateObject(user);
                            resetForm();
                        }
                        break;
                    } else {
                        CommonFeature.ERROR_ALERT.setHeaderText("Privilege & User A/C - Save");
                        CommonFeature.ERROR_ALERT.setContentText("This Employee doesn't has an U/AC to reset!");
                        CommonFeature.ERROR_ALERT.showAndWait();
                        break;
                    }

                }

                case "Delete U/AC": {
                    if (sysUsers.size() == 1) {
                        Alert confirmAlert = CommonFeature.getConfirmAlert("Delete U/AC");
                        confirmAlert.setContentText("Do you want to delete U/AC of Employee:- " + employee.getName());

                        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
                            SysUser user = (SysUser) CommonDao.getObjectObservableList("SysUser.findByEmployeeId", parameter).get(0);
                            CommonDao.deleteObject(user);
                            resetForm();
                        }
                        break;
                    } else {
                        CommonFeature.ERROR_ALERT.setHeaderText("Privilege & User A/C - Save");
                        CommonFeature.ERROR_ALERT.setContentText("This Employee doesn't has an U/AC to delete!");
                        CommonFeature.ERROR_ALERT.showAndWait();
                        break;
                    }

                }
            }
        } else {
            CommonFeature.ERROR_ALERT.setHeaderText("Privilege & User A/C - Save");
            CommonFeature.ERROR_ALERT.setContentText("Please Select both an Employee & an Operation");
            CommonFeature.ERROR_ALERT.showAndWait();
        }

    }

    @FXML
    private void cmbEmployeeAP(ActionEvent event) throws Exception {

        Employee employee = cmbEmployee.getSelectionModel().getSelectedItem();
        if (employee != null) {
            parameter.put("employeeId", employee);
            privileges = CommonDao.getObjectObservableList("Privilege.findByEmployeeId", parameter);
            fillPrivilegeTB(privileges);
        }

    }

    @FXML
    private void btnClearAP(ActionEvent event) throws Exception {
        Alert confirmAlert = CommonFeature.getConfirmAlert("Privilege Form-Clear");
        confirmAlert.setContentText("Do you want to clear this form?");
        if (confirmAlert.showAndWait().get() == ButtonType.YES) {
            resetForm();
        }
    }

}
