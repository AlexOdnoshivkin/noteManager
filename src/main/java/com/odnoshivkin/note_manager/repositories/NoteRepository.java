package com.odnoshivkin.note_manager.repositories;

import com.odnoshivkin.note_manager.models.note.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("select n from Note as n order by n.id")
    List<Note> findAll();

    @Override
    Optional<Note> findById(Long id);
}
