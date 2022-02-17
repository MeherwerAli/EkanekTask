package com.task.backend.repository;

import com.task.backend.model.FileModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileRepository extends CrudRepository<FileModel, Integer> {
    public List<FileModel> findByUser_Id(Integer userId);
}
