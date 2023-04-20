package repository;

import config.MysqlConfig;
import model.ProjectModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JobsRepository {

    // Format date
    private String dateFormat(String oldDate) {
        String newDate = "";
        try {
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oldDate);
            newDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
        } catch (Exception e) {
            System.out.println("An error occurred when change date format | " + e.getMessage());
            e.printStackTrace();
        }

        return newDate;
    }

    // Lấy toàn bộ danh sách project
    public List<ProjectModel> getAllProjects() {
        List<ProjectModel> listProjects = new ArrayList<>();
        Connection connection = MysqlConfig.getConnection();
        String query = "select * from jobs";

        try {
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();

            while (resultSet.next()) {
                ProjectModel projectModel = new ProjectModel();
                projectModel.setId(resultSet.getInt("id"));
                projectModel.setName(resultSet.getString("name"));
                projectModel.setStartDate(dateFormat(resultSet.getString("start_date")));
                projectModel.setEndDate(dateFormat(resultSet.getString("end_date")));

                listProjects.add(projectModel);
            }
        } catch (Exception e) {
            System.out.println("Lỗi câu query getAllProjects " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return listProjects;
    }

    // Thêm project
    public int addNewProject(String name, Date startDate, Date endDate) {
        int isSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String queryCheck = "select count(*) as count from jobs j where j.name = ?";
        String query = "insert into jobs(name,start_date,end_date) value(?,?,?)";

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
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);

            isSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi câu query addNewProject " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isSuccess;
    }

    // Xoá project
    public int deleteProjectById(int id) {
        int isSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String query = "delete from jobs u where u.id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            isSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi câu query deleteProjectById " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isSuccess;
    }

    // Lấy thông tin project
    public ProjectModel getProjectById(int id) {
        ProjectModel projectModel = new ProjectModel();
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT * FROM jobs WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                projectModel.setId(id);
                projectModel.setName(resultSet.getString("name"));
                projectModel.setStartDate(resultSet.getString("start_date"));
                projectModel.setEndDate(resultSet.getString("end_date"));
            }
        } catch (Exception e) {
            System.out.println("Lỗi câu query selectProjectById " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return projectModel;
    }

    // Cập nhật project
    public int updateProject(int id, String name, String startDate, String endDate) {
        int isSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String queryCheck = "select count(*) as count from jobs j where j.name = ?";
        String query = "update jobs set name = ?, start_date = ?, end_date = ?\n" +
                "where id = ?";

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
            statement.setString(2, startDate);
            statement.setString(3, endDate);
            statement.setInt(4, id);

            isSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error update project by id " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isSuccess;
    }

    // Kiểm tra relationship data của job
    public boolean checkJob(int idJob) {
        boolean isSuccess = false;
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT t.job_id\n" +
                "        FROM jobs AS j\n" +
                "        LEFT JOIN tasks AS t ON j.id = t.job_id\n" +
                "        WHERE j.id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idJob);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String job = resultSet.getString("job_id");
                if (job != null) {
                    isSuccess = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error update project by id " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isSuccess;
    }

}
