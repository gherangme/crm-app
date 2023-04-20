package service;

import model.TaskModel;
import repository.TasksRepository;

import java.util.List;

public class ProfileService {

    public List<TaskModel> getListTasksByUserId(int id) {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.getListTasksByUserId(id);
    }

    public TaskModel getTaskById(int id) {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.getTaskByIdTask(id);
    }

    public int updateStatusOfTask(int idTask, int idStatus) {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.updateStatusOfTask(idTask, idStatus);
    }

}
