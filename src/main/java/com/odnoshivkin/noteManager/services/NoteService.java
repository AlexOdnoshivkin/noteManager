package com.odnoshivkin.noteManager.services;

import com.odnoshivkin.noteManager.models.Note;
import com.odnoshivkin.noteManager.models.NoteDto;
import com.odnoshivkin.noteManager.models.paging.Paged;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NoteService  {
    NoteDto saveNote(NoteDto noteDto);

    NoteDto putNote(NoteDto noteDto);

    List<NoteDto> getAllNote();

    NoteDto getNoteById(Long noteId);

    void deleteNote(Long noteId);

    Paged<Note> getPage(int pageNumber, int size);
}
