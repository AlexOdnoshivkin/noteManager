package com.odnoshivkin.note_manager.services;

import com.odnoshivkin.note_manager.models.note.Note;
import com.odnoshivkin.note_manager.models.note.NoteDto;
import com.odnoshivkin.note_manager.models.paging.Paged;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@Sql(scripts = {"/schema.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class NoteServiceImplTest {

    private final EntityManager em;

    private final NoteService noteService;

    private NoteDto noteDto;

    @AfterEach
    void afterEach() {
        em.createNativeQuery("truncate table notes");
    }

    @BeforeEach
    void createEntity() {
        noteDto = new NoteDto();
        noteDto.setTitle("Test Title");
        noteDto.setContent("Test Note Content");
    }

    @Test
    void saveNoteTest() {
        NoteDto savedNote = noteService.saveNote(noteDto);
        noteDto.setId(savedNote.getId());

        assertEquals(noteDto, savedNote);
    }

    @Test
    void putNoteWhenNoteInDb() {
        NoteDto savedNote = noteService.saveNote(noteDto);

        NoteDto noteToUpdate = new NoteDto();
        noteToUpdate.setId(savedNote.getId());
        noteToUpdate.setTitle("Updated Title");
        noteToUpdate.setContent("Updated Note Content");

        NoteDto updatedNote = noteService.putNote(noteToUpdate);

        assertEquals(noteToUpdate, updatedNote);
    }

    @Test
    void putNoteWhenNoteNotFoundThenThrowException() {
        NoteDto noteToUpdate = new NoteDto();
        noteToUpdate.setId(0L);
        noteToUpdate.setTitle("Updated Title");
        noteToUpdate.setContent("Updated Note Content");
        NotFoundException thrown = Assertions
                .assertThrows(NotFoundException.class, () ->
                        noteService.putNote(noteToUpdate));

        assertEquals("Заметка с id 0 не найдена в базе данных", thrown.getMessage());
    }

    @Test
    void getAllNoteTest() {
        NoteDto savedNote = noteService.saveNote(noteDto);
        List<NoteDto> trueResult = List.of(savedNote);

        List<NoteDto> result = noteService.getAllNote();

        assertEquals(1, result.size());
        assertEquals(trueResult,result);
    }

    @Test
    void getNoteByIdWhenNoteInDb() {
        NoteDto savedNote = noteService.saveNote(noteDto);

        NoteDto result = noteService.getNoteById(savedNote.getId());

        assertEquals(savedNote, result);
    }

    @Test
    void getNoteByIdWhenNoteNotFoundThenThrowException() {
        NotFoundException thrown = Assertions
                .assertThrows(NotFoundException.class, () ->
                        noteService.getNoteById(0L));

        assertEquals("Заметка с id 0 не найдена в базе данных", thrown.getMessage());
    }

    @Test
    void deleteNoteWhenNoteInDb() {
        NoteDto savedNote = noteService.saveNote(noteDto);

        noteService.deleteNote(savedNote.getId());

        NotFoundException thrown = Assertions
                .assertThrows(NotFoundException.class, () ->
                        noteService.getNoteById(savedNote.getId()));

        assertEquals("Заметка с id " + savedNote.getId() + " не найдена в базе данных", thrown.getMessage());
    }

    @Test
    void deleteNoteWhenNoteNotFoundThenThrowException() {
        NotFoundException thrown = Assertions
                .assertThrows(NotFoundException.class, () ->
                        noteService.deleteNote(0L));

        assertEquals("Заметка с id 0 не найдена в базе данных", thrown.getMessage());
    }

    @Test
    void getPageTest() {
        noteService.saveNote(noteDto);

        Paged<Note> notePaged = noteService.getPage(1, 5);

        assertEquals(1, notePaged.getPage().getTotalElements());
    }

}