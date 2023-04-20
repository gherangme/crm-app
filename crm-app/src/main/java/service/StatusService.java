package service;

import model.StatusModel;
import repository.StatusRepository;

import java.util.List;

public class StatusService {

    public List<StatusModel> getAllStatus() {
        StatusRepository statusRepository = new StatusRepository();
        return statusRepository.getAllStatus();
    }

}
