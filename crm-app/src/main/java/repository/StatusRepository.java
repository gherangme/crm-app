package repository;

import config.MysqlConfig;
import model.StatusModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StatusRepository {

    // Lấy toàn bộ danh sách status
    public List<StatusModel> getAllStatus() {
        List<StatusModel> list = new ArrayList<>();
        String query = "SELECT * FROM status";
        Connection connection = MysqlConfig.getConnection();

        try {
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();
            while (resultSet.next()) {
                StatusModel statusModel = new StatusModel();
                statusModel.setId(resultSet.getInt("id"));
                statusModel.setName(resultSet.getString("name"));
                list.add(statusModel);
            }
        } catch (Exception e) {
            System.out.println("Error query getAllStatus " + e.getMessage());
        }
        return list;
    }

}
