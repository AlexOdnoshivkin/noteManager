package com.odnoshivkin.note_manager.services;

import com.odnoshivkin.note_manager.models.note.Note;
import com.odnoshivkin.note_manager.models.note.NoteDto;
import com.odnoshivkin.note_manager.models.paging.Paged;

import java.util.List;

public interface NoteService {
    NoteDto saveNote(NoteDto noteDto);

    NoteDto putNote(NoteDto noteDto);

    List<NoteDto> getAllNote();

    NoteDto getNoteById(Long noteId);

    void deleteNote(Long noteId);

    Paged<Note> getPage(int pageNumber, int size);
}
