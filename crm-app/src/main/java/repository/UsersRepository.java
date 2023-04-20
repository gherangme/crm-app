package repository;

import config.MysqlConfig;
import model.RoleModel;
import model.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersRepository {

    // Lấy user bằng email và password
    public int countUsersByEmailAndPassword(String email, String password) {
        Connection connection = MysqlConfig.getConnection();
        int count = 0;
        String query = "select count(*) as count from users u where u.email=? and u.password=?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    // Lấy thông tin user bằng email
    public UserModel getUserByEmail(String email) {
        UserModel userModel = new UserModel();
        Connection connection = MysqlConfig.getConnection();
        String query = "select * from users u where u.email=?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userModel.setId(resultSet.getInt("id"));
                userModel.setEmail(resultSet.getString("email"));
                userModel.setPassword(resultSet.getString("password"));
                userModel.setFullName(resultSet.getString("fullname"));
                userModel.setAvatar(resultSet.getString("avatar"));

                RoleModel roleModel = new RoleModel();
                roleModel.setId(resultSet.getInt("role_id"));
                userModel.setRoleModel(roleModel);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return userModel;
    }

    // Thêm user
    public int addUser(String fullName, String email, String password, int role_id, String avatar) {
        int count = 0;
        Connection connection = MysqlConfig.getConnection();
        String queryCheck = "select count(*) as count from users u where u.email = ?";
        String query = "insert into users(email,password,fullname,role_id,avatar) value(?,?,?,?,?)";
        try {
            PreparedStatement statementCheck = connection.prepareStatement(queryCheck);
            statementCheck.setString(1, email);
            ResultSet resultSet = statementCheck.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt("count");
                if (count > 0) {
                    return -1;
                }
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.setInt(4, role_id);
            statement.setString(5, avatar);
            count = statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error addUser " + e.getMessage());
        }

        return count;
    }

    // Lấy toàn bộ danh sách users
    public List<UserModel> getAllUsers() {
        List<UserModel> listUsers = new ArrayList<>();
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT u.id, u.email, u.fullname, r.name AS role_name FROM users AS u " +
                "LEFT JOIN roles AS r ON u.role_id = r.id ORDER BY u.role_id;";

        try {
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();
            while (resultSet.next()) {
                UserModel userModel = new UserModel();
                userModel.setId(resultSet.getInt("id"));
                userModel.setEmail(resultSet.getString("email"));
                userModel.setFullName(resultSet.getString("fullname"));

                RoleModel roleModel = new RoleModel();
                roleModel.setRoleName(resultSet.getString("role_name"));
                userModel.setRoleModel(roleModel);

                listUsers.add(userModel);
            }
        } catch (Exception e) {
            System.out.println("Lỗi câu query getAllUsers " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return listUsers;
    }

    // Xoá user
    public int deleteUserById(int id) {
        int isDeleteSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String query = "delete from users where id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            isDeleteSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error delete user by id " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isDeleteSuccess;
    }

    // Cập nhật user
    public int updateUser(String email, String password, String fullName, String avatar, int roleId) {
        int isUpdateSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String query = "update users set password = ?, fullname = ?, avatar = ?, role_id = ? " +
                "where email = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, password);
            statement.setString(2, fullName);
            statement.setString(3, avatar);
            statement.setInt(4, roleId);
            statement.setString(5, email);

            isUpdateSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error update user by id " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isUpdateSuccess;
    }
}
