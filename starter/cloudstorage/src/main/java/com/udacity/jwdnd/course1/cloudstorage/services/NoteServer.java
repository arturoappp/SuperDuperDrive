package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServer {
    @Autowired
    NoteMapper noteMapper;

    public int createNote(Note note) {

        if (noteMapper.getNote(note.getNoteTitle()) != null) {
            throw new RuntimeException("Note already exist!");
        }
        return noteMapper.save(note);
    }

    public Note findById(int id) {
        return noteMapper.findById(id);
    }

    public List<Note> findAllByUserId(int userId) {
        return noteMapper.findAll(userId);
    }

    public int update(Note note) {
        return noteMapper.update(note);
    }

    public void delete(int id) {
        noteMapper.delete(id);
    }
}
