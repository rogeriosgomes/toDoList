package br.com.inovatte.todolist.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRespository extends JpaRepository<TaskModel, UUID> {

    TaskModel findByTitle(String title);

    List<TaskModel> findByIdUser(UUID idUser);
}
