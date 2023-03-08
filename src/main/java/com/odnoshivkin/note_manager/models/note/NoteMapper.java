package com.odnoshivkin.note_manager.models.note;

public class NoteMapper {
    // Приватный конструктор для обеспечения неинстанцируемости класса
    private NoteMapper() {};

    public static Note mapNoteDtoToNote(NoteDto noteDto) {
        Note note = new Note();
        note.setId(noteDto.getId());
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        return note;
    }

    public static NoteDto mapNoteToNoteDto(Note note) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(note.getId());
        noteDto.setTitle(note.getTitle());
        noteDto.setContent(note.getContent());
        return noteDto;
    }
}
