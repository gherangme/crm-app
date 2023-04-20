package service;

import model.ProjectModel;
import repository.JobsRepository;

import java.sql.Date;
import java.util.List;

public class ProjectService {
    public List<ProjectModel> getAllProjects() {
        JobsRepository jobsRepository = new JobsRepository();
        return jobsRepository.getAllProjects();
    }

    public int addNewProject(String name, Date startDate, Date endDate) {
        JobsRepository jobsRepository = new JobsRepository();
        return jobsRepository.addNewProject(name, startDate, endDate);
    }

    public boolean deleteProjectByid(int id) {
        JobsRepository jobsRepository = new JobsRepository();
        return jobsRepository.deleteProjectById(id) >= 1;
    }

    public ProjectModel getProjectById(int id) {
        JobsRepository jobsRepository = new JobsRepository();
        return jobsRepository.getProjectById(id);
    }

    public int updateProject(int id, String name, String startDate, String endDate) {
        JobsRepository jobsRepository = new JobsRepository();
        return jobsRepository.updateProject(id, name, startDate, endDate);
    }

    public boolean checkJob(int idJob) {
        JobsRepository jobsRepository = new JobsRepository();
        return jobsRepository.checkJob(idJob);
    }
}
