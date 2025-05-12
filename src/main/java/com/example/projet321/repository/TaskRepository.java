package com.example.projet321.repository;

import com.example.projet321.entity.Task;        // ✅ Entité correcte
import com.example.projet321.entity.User;        // ✅ User si vous filtrez par User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedUser(User user);
}
