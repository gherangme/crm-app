package service;

import model.UserModel;
import repository.TasksRepository;
import repository.UsersRepository;

import java.util.List;

public class HomeService {

    public UserModel getUserByEmail(String email) {
        UsersRepository usersRepository = new UsersRepository();
        return usersRepository.getUserByEmail(email);
    }

    public List<Integer> getTaskStatusByUserId(int id) {
        TasksRepository tasksRepository = new TasksRepository();
        return tasksRepository.getAllStatusTasksByUserId(id);
    }

}