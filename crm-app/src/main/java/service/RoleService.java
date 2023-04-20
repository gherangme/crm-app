package service;

import model.RoleModel;
import model.UserModel;
import repository.RoleRepository;
import repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;


public class RoleService {

    public List<RoleModel> getAllRoles() {
        RoleRepository roleRepository = new RoleRepository();
        return roleRepository.getAllRoles();
    }

    public boolean deleteRoleById(int id) {
        RoleRepository roleRepository = new RoleRepository();
        return roleRepository.deleteRoleById(id) >= 1;
    }


    public int addNewRole(String name, String desc) {
        RoleRepository roleRepository = new RoleRepository();
        return roleRepository.addNewRole(name, desc);
    }

    public RoleModel getRoleModelById(int id) {
        RoleRepository roleRepository = new RoleRepository();
        return roleRepository.getRoleModelById(id);
    }

    public int updateRole(int id, String name, String desc) {
        RoleRepository roleRepository = new RoleRepository();
        return roleRepository.updateRole(id, name, desc);
    }

    public boolean checkRole(int idRole) {
        RoleRepository roleRepository = new RoleRepository();
        return roleRepository.checkRole(idRole) > 0;
    }

}
