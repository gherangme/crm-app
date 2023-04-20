package repository;

import config.MysqlConfig;
import model.ProjectModel;
import model.StatusModel;
import model.TaskModel;
import model.UserModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TasksRepository {

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

    // Lấy toàn bộ danh sách tasks
    public List<TaskModel> getAllTasks() {
        List<TaskModel> list = new ArrayList<>();
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT t.id, t.name, t.start_date, t.end_date, j.name\n" +
                "AS jobs, u.fullname AS users, s.name AS status\n" +
                "FROM tasks AS t \n" +
                "LEFT JOIN jobs AS j ON t.job_id = j.id\n" +
                "LEFT JOIN status AS s ON t.status_id = s.id\n" +
                "LEFT JOIN users AS u ON t.user_id = u.id";

        try {
            ResultSet resultSet = connection.prepareStatement(query).executeQuery();

            while (resultSet.next()) {
                TaskModel taskModel = new TaskModel();
                taskModel.setId(resultSet.getInt("id"));
                taskModel.setName(resultSet.getString("name"));
                taskModel.setStartDate(dateFormat(resultSet.getString("start_date")));
                taskModel.setEndDate(dateFormat(resultSet.getString("end_date")));

                UserModel userModel = new UserModel();
                userModel.setFullName(resultSet.getString("users"));
                taskModel.setUserModel(userModel);

                ProjectModel projectModel = new ProjectModel();
                projectModel.setName(resultSet.getString("jobs"));
                taskModel.setProjectModel(projectModel);

                StatusModel statusModel = new StatusModel();
                statusModel.setName(resultSet.getString("status"));
                taskModel.setStatusModel(statusModel);

                list.add(taskModel);
            }
        } catch (Exception e) {
            System.out.println("Lỗi câu truy vấn getAllTask " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return list;
    }

    // Thêm task
    public int addNewTask(String name, Date start_date, Date end_date, int user_id, String projectName) {
        int isSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String queryProjectName = "select * from jobs u where u.name = ?";
        String query = "insert into tasks(name,start_date,end_date,user_id,job_id,status_id) value(?,?,?,?,?,1);";

        try {
            PreparedStatement statement = connection.prepareStatement(queryProjectName);
            statement.setString(1, projectName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idProjectName = resultSet.getInt("id");
                statement = connection.prepareStatement(query);
                statement.setString(1, name);
                statement.setDate(2, start_date);
                statement.setDate(3, end_date);
                statement.setInt(4, user_id);
                statement.setInt(5, idProjectName);
            }

            isSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi câu query addNewTask " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isSuccess;
    }


    // Xoá task
    public int deleteTaskById(int id) {
        int isDeleteSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String query = "delete from tasks u where u.id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            isDeleteSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error deleteTaskById" + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isDeleteSuccess;
    }

    // Lấy toàn bộ status task của user
    public List<Integer> getAllStatusTasksByUserId(int id) {
        List<Integer> list = new ArrayList<>();
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT t.status_id FROM tasks as t WHERE t.user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getInt("status_id"));
            }
        } catch (Exception e) {
            System.out.println("Error getAllStatusTasksByUserId " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return list;
    }

    // Lấy toàn bộ status task của project
    public List<Integer> getAllStatusTasksByProjectId(int idProject) {
        List<Integer> list = new ArrayList<>();
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT t.status_id FROM tasks as t WHERE t.job_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idProject);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                list.add(resultSet.getInt("status_id"));
            }
        } catch (Exception e) {
            System.out.println("Error getAllStatusTasksByUserId " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return list;
    }

    // Lấy list task của user
    public List<TaskModel> getListTasksByUserId(int id) {
        List<TaskModel> list = new ArrayList<>();
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT t.id, t.name, t.start_date, t.end_date, t.status_id, j.name\n" +
                "AS jobs, u.fullname AS users, s.name AS status\n" +
                "FROM tasks AS t \n" +
                "LEFT JOIN jobs AS j ON t.job_id = j.id\n" +
                "LEFT JOIN status AS s ON t.status_id = s.id\n" +
                "LEFT JOIN users AS u ON t.user_id = u.id\n" +
                "WHERE u.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                TaskModel taskModel = new TaskModel();
                taskModel.setId(resultSet.getInt("id"));
                taskModel.setName(resultSet.getString("name"));
                taskModel.setStartDate(dateFormat(resultSet.getString("start_date")));
                taskModel.setEndDate(dateFormat(resultSet.getString("end_date")));

                UserModel userModel = new UserModel();
                userModel.setFullName(resultSet.getString("users"));

                ProjectModel projectModel = new ProjectModel();
                projectModel.setName(resultSet.getString("jobs"));

                StatusModel statusModel = new StatusModel();
                statusModel.setName(resultSet.getString("status"));
                statusModel.setId(resultSet.getInt("status_id"));

                taskModel.setUserModel(userModel);
                taskModel.setProjectModel(projectModel);
                taskModel.setStatusModel(statusModel);

                list.add(taskModel);
            }
        } catch (Exception e) {
            System.out.println("Error getAllStatusTasksByUserId " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return list;
    }

    // Lấy thông tin task bằng id task
    public TaskModel getTaskByIdTask(int id) {
        TaskModel taskModel = new TaskModel();
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT t.id, j.name AS job_name, t.name, t.start_date, t.end_date, t.status_id " +
                "FROM tasks AS t LEFT JOIN jobs AS j ON t.job_id = j.id WHERE t.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                taskModel.setId(resultSet.getInt("id"));
                taskModel.setName(resultSet.getString("name"));
                taskModel.setStartDate(dateFormat(resultSet.getString("start_date")));
                taskModel.setEndDate(dateFormat(resultSet.getString("end_date")));

                ProjectModel projectModel = new ProjectModel();
                projectModel.setName(resultSet.getString("job_name"));
                taskModel.setProjectModel(projectModel);

                StatusModel statusModel = new StatusModel();
                statusModel.setId(resultSet.getInt("status_id"));
                taskModel.setStatusModel(statusModel);
            }
        } catch (Exception e) {
            System.out.println("Error query getTaskById " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return taskModel;
    }

    // Cập nhật status task
    public int updateStatusOfTask(int idTask, int idStatus) {
        int result = -1;
        Connection connection = MysqlConfig.getConnection();
        String query = "UPDATE tasks SET status_id = ? WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idStatus);
            statement.setInt(2, idTask);

            result = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error query updateStatusOfTask " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return result;
    }

    // Lấy thông tin task bằng id user
    public int getTaskByUserId(int id) {
        int isSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT * FROM tasks WHERE user_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                isSuccess++;
            }
        } catch (Exception e) {
            System.out.println("Error query getTaskByUserId " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isSuccess;
    }

    // Lấy thông tin task edit
    public TaskModel getTaskEditByIdTask(int id) {
        TaskModel taskModel = new TaskModel();
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT t.id, t.name, t.start_date, t.end_date, t.user_id, t.job_id, j.name\n" +
                "AS jobs\n" +
                "FROM tasks AS t \n" +
                "LEFT JOIN jobs AS j ON t.job_id = j.id\n" +
                "WHERE t.id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                taskModel.setId(id);
                taskModel.setName(resultSet.getString("name"));
                taskModel.setStartDate(resultSet.getString("start_date"));
                taskModel.setEndDate(resultSet.getString("end_date"));

                UserModel userModel = new UserModel();
                userModel.setId(resultSet.getInt("user_id"));
                taskModel.setUserModel(userModel);

                ProjectModel projectModel = new ProjectModel();
                projectModel.setId(resultSet.getInt("job_id"));
                projectModel.setName(resultSet.getString("jobs"));
                taskModel.setProjectModel(projectModel);
            }
        } catch (Exception e) {
            System.out.println("Error query getTaskByUserId " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return taskModel;
    }

    // Cập nhật task
    public int updateTask(int id, int idUser, int idProject, String nameTask, String startDate, String endDate) {
        int isSuccess = 0;
        Connection connection = MysqlConfig.getConnection();
        String query = "UPDATE tasks SET name = ?, start_date = ?, end_date = ?,\n" +
                "user_id = ?, job_id = ?\n" +
                "WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, nameTask);
            statement.setString(2, startDate);
            statement.setString(3, endDate);
            statement.setInt(4, idUser);
            statement.setInt(5, idProject);
            statement.setInt(6, id);

            isSuccess = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error update task by id " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return isSuccess;
    }

    // Lấy toàn bộ danh sách task bằng id project
    public List<TaskModel> getAllTaskByProjectId(int idProject) {
        List<TaskModel> list = new ArrayList<>();
        Connection connection = MysqlConfig.getConnection();
        String query = "SELECT t.name, t.start_date, t.end_date, u.fullname, t.user_id, u.avatar, t.status_id\n" +
                "FROM tasks AS t\n" +
                "LEFT JOIN users AS u ON t.user_id = u.id\n" +
                "WHERE job_id = ? ORDER BY user_id";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, idProject);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                TaskModel taskModel = new TaskModel();
                taskModel.setName(resultSet.getString("name"));
                taskModel.setStartDate(dateFormat(resultSet.getString("start_date")));
                taskModel.setEndDate(dateFormat(resultSet.getString("end_date")));

                UserModel userModel = new UserModel();
                userModel.setId(resultSet.getInt("user_id"));
                userModel.setFullName(resultSet.getString("fullname"));
                userModel.setAvatar(resultSet.getString("avatar"));
                taskModel.setUserModel(userModel);

                StatusModel statusModel = new StatusModel();
                statusModel.setId(resultSet.getInt("status_id"));
                taskModel.setStatusModel(statusModel);
                list.add(taskModel);
            }
        } catch (Exception e) {
            System.out.println("Error query getTaskByUserId " + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }

        return list;
    }

}
