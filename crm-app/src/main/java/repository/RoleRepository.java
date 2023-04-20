package repository;

import config.MysqlConfig;
import listenum.RoleColumn;
import model.RoleModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleRepository {

    // Lấy toàn bộ danh sách roles
    public List<RoleModel> getAllRoles() {
        List<RoleModel> listRoles = new ArrayList<>();
        Connection connection = MysqlConfig.getConnection();
        String query = "select * from roles";

        try {
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();
            while (resultSet.next()) {
                RoleModel roleModel = new RoleModel();
                roleModel.setId(resultSet.getInt(RoleColumn.ID.getValue()));
                roleModel.setRoleName(resultSet.getString(RoleColumn.NAME.getValue()));
                roleModel.setDescription(resultSet.getString(RoleColumn.DESCRIPTION.getValue()));

                listRoles.add(roleModel);
            }
        } catch (Exception e) {
            System.out.println("Lỗi câu query getAllRoles " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return listRoles;
    }

    // Lấy role bằng id
    public RoleModel getRoleModelById(int id) {
        RoleModel roleModel = new RoleModel();
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT * FROM roles AS r where r.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                roleModel.setId(resultSet.getInt("id"));
                roleModel.setRoleName(resultSet.getString("name"));
                roleModel.setDescription(resultSet.getString("description"));
            }

        } catch (Exception e) {
            System.out.println("Loi cau truy van getRoleModelById " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {

                }
            }
        }

        return roleModel;
    }

    // Xoá  role bằng id
    public int deleteRoleById(int id) {
        int isDeleteSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String query = "delete from roles r where r.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            isDeleteSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error delete role by id " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isDeleteSuccess;
    }

    // Thêm role
    public int addNewRole(String name, String desc) {
        int isSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String queryCheck = "select count(*) as count from roles r where r.name = ?";
        String query = "insert into roles(name,description) value(?,?)";
        try {
            PreparedStatement statementCheck = connection.prepareStatement(queryCheck);
            statementCheck.setString(1, name);
            ResultSet resultSet = statementCheck.executeQuery();
            while (resultSet.next()) {
                isSuccess = resultSet.getInt("count");
                if (isSuccess > 0) {
                    return -1;
                }
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, desc);

            isSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error delete role by id " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isSuccess;
    }

    // Update role
    public int updateRole(int id, String name, String desc) {
        int isSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String queryCheck = "select count(*) as count from roles r where r.name = ?";
        String query = "update roles set name = ?, description = ? where id = ?";

        try {
            PreparedStatement statementCheck = connection.prepareStatement(queryCheck);
            statementCheck.setString(1, name);
            ResultSet resultSet = statementCheck.executeQuery();
            while (resultSet.next()) {
                isSuccess = resultSet.getInt("count");
                if (isSuccess > 0) {
                    return -1;
                }
            }

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, desc);
            statement.setInt(3, id);

            isSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error update role by id " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isSuccess;
    }

    // Kiểm tra relationship data của role
    public int checkRole(int idRole) {
        int isSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT u.role_id\n" +
                "FROM roles AS r\n" +
                "LEFT JOIN users AS u ON u.role_id = r.id\n" +
                "WHERE r.id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idRole);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String role = resultSet.getString("role_id");
                if (role != null) {
                    isSuccess++;
                }
            }
        } catch (Exception e) {
            System.out.println("Error update role by id " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isSuccess;
    }

}
