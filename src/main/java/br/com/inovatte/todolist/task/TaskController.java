package br.com.inovatte.todolist.task;

import br.com.inovatte.todolist.filter.FilterTaskAuth;
import br.com.inovatte.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskRespository respository;
    @Autowired
    FilterTaskAuth filterTaskAuth;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){

        var task = respository.findByTitle(taskModel.getTitle());
        var currentDate = LocalDateTime.now();

        if(task != null){

            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa já existe");
        }

        if(taskModel.getStartAt().isBefore(currentDate) || taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data de inicio incorreta");
        }

        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);
        var taskCreated = respository.save(taskModel);

        return  ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);
    }

    @GetMapping("/lista")
    public List<TaskModel> list(HttpServletRequest request){

        var idUser = (UUID) request.getAttribute("idUser");
        System.out.println(idUser);
        var tasks = respository.findByIdUser(idUser);

        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request,@PathVariable UUID id){

        var task = this.respository.findById(id).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        var idUser = request.getAttribute("idUser");

        if(! task.getIdUser().equals(idUser)){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(taskModel, task);
        this.respository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }




}
