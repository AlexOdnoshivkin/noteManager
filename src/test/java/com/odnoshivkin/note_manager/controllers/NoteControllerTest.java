package com.odnoshivkin.note_manager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odnoshivkin.note_manager.models.note.Note;
import com.odnoshivkin.note_manager.models.note.NoteDto;
import com.odnoshivkin.note_manager.models.paging.Paged;
import com.odnoshivkin.note_manager.models.paging.Paging;
import com.odnoshivkin.note_manager.services.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Autowired
    private NoteController noteController;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(noteController)
                .build();
    }

    @Test
    void welcomePageTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("welcomePage"));
    }

   /* @Test
   void backTest() throws Exception {
        mockMvc.perform(get("/back")
                        .header("Referer", "/"))
                .andExpect(status().isOk());
    }*/

    @Test
    void noteRedactor() throws Exception {
        mockMvc.perform(get("/note?id=1"))
                .andExpect(status().isOk())
                .andExpect(view().name("redactor"));
    }

    @Test
    void saveTest() throws Exception {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(1L);
        noteDto.setTitle("Test Title");
        noteDto.setContent("Test Note Content");

        when(noteService.saveNote(any())).thenReturn(noteDto);

        mockMvc.perform(post("/note")
                        .param("id", "1")
                        .param("title", "Test Title")
                        .param("content", "Test Note Content"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("note", noteDto))
                .andExpect(view().name("saved"));
    }

    @Test
    void putNoteTest() throws Exception {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(1L);
        noteDto.setTitle("Test Title");
        noteDto.setContent("Test Note Content");

        when(noteService.putNote(any())).thenReturn(noteDto);

        mockMvc.perform(put("/note")
                        .param("id", "1")
                        .param("title", "Test Title")
                        .param("content", "Test Note Content"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("note", noteDto))
                .andExpect(view().name("saved"));
    }

    @Test
    void deleteNote() throws Exception {
        doNothing().when(noteService).deleteNote(anyLong());

        mockMvc.perform(delete("/note")
                        .param("id", "1"))
                .andExpect(redirectedUrl("/notes"));
    }

    @Test
    void getAllNotesTitleTest() throws Exception {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test Title");
        note.setContent("Test Note Content");
        Page<Note> notePage = new PageImpl<>(List.of(note));
        Paged<Note> paged = new Paged<>(notePage, Paging.of(1, 1, 1));

        when(noteService.getPage(anyInt(), anyInt())).thenReturn(paged);

        mockMvc.perform(get("/notes")
                        .param("pageNumber", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("notes", paged))
                .andExpect(view().name("notesTitleList"));
    }

    @Test
    void getNoteByIdTest() throws Exception {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(1L);
        noteDto.setTitle("Test Title");
        noteDto.setContent("Test Note Content");

        when(noteService.getNoteById(anyLong())).thenReturn(noteDto);

        mockMvc.perform(get("/note/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("note", noteDto))
                .andExpect(view().name("saved"));
    }
}