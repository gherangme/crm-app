package service;

import model.TaskModel;
import repository.TasksRepository;
import repository.UsersRepository;

import java.sql.Date;
import java.util.List;

public class TaskService {
    public List<TaskModel> getAllTasks() {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.getAllTasks();
    }

    public int addNewTask(String name, Date start_date, Date end_date, int user_id, String projectName) {
        TasksRepository tasksRepository = new TasksRepository();

        return tasksRepository.addNewTask(name, start_date, end_date, user_id, projectName);
    }

    public boolean deleteTaskById(int id) {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.deleteTaskById(id) >= 1;
    }

    public boolean getTaskByUserId(int id) {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.getTaskByUserId(id) > 0;
    }

    public TaskModel getTaskEditByIdTask(int id) {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.getTaskEditByIdTask(id);
    }

    public int updateTask(int id, int idUser, int idProject, String nameTask, String startDate, String endDate) {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.updateTask(id, idUser, idProject, nameTask, startDate, endDate);
    }

    public List<Integer> getAllStatusTasksByProjectId(int idProject) {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.getAllStatusTasksByProjectId(idProject);
    }

    public List<TaskModel> getAllTaskByProjectId(int idProject) {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.getAllTaskByProjectId(idProject);
    }
}
