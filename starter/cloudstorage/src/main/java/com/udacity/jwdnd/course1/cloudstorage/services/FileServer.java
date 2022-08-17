package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileServer {
    @Autowired
    FileMapper fileMapper;

    public int saveFile(MultipartFile multipartFile, int userId) {

        if (multipartFile.isEmpty()) {
            throw new RuntimeException("Please select a file!");
        }

        if (fileMapper.getFile(multipartFile.getOriginalFilename()) != null) {
            throw new RuntimeException("File already exist!");
        }

        try {
            File file = new File(null
                    , multipartFile.getOriginalFilename()
                    , multipartFile.getContentType()
                    , String.valueOf(multipartFile.getSize())
                    , userId
                    , multipartFile.getInputStream().readAllBytes()
            );

            return fileMapper.save(file);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<File> findAllByUserId(int userId) {
        return fileMapper.findAll(userId);
    }

    public File findFileById(int id) {
        return fileMapper.findById(id);
    }

    public void delete(int id) {
        fileMapper.delete(id);
    }
}
