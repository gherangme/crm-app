package service;

import model.UserModel;
import repository.UsersRepository;

import java.util.List;

public class UserService {
    public int addUser(String fullName, String email, String password, int role_id, String avatar) {
        UsersRepository usersRepository = new UsersRepository();
        return usersRepository.addUser(fullName, email, password, role_id, avatar);
    }

    public List<UserModel> getAllUsers() {
        UsersRepository usersRepository = new UsersRepository();
        return usersRepository.getAllUsers();
    }

    public boolean deleteUserById(int id) {
        UsersRepository usersRepository = new UsersRepository();
        return usersRepository.deleteUserById(id) >= 1;
    }

    public UserModel getUserByEmail(String email) {
        UsersRepository usersRepository = new UsersRepository();
        return usersRepository.getUserByEmail(email);
    }

    public boolean updateUser(String email, String password, String fullName, String avatar, int roleId) {
        UsersRepository usersRepository = new UsersRepository();
        return usersRepository.updateUser(email, password, fullName, avatar, roleId) >= 1;
    }
}
