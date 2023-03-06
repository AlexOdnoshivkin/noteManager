package com.odnoshivkin.noteManager.services;

import com.odnoshivkin.noteManager.models.Note;
import com.odnoshivkin.noteManager.models.NoteDto;
import com.odnoshivkin.noteManager.models.NoteMapper;
import com.odnoshivkin.noteManager.models.paging.Paged;
import com.odnoshivkin.noteManager.models.paging.Paging;
import com.odnoshivkin.noteManager.repositories.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    @Override
    public NoteDto saveNote(NoteDto noteDto) {
        Note note = NoteMapper.mapNoteDtoToNote(noteDto);
        NoteDto savedNote = NoteMapper.mapNoteToNoteDto(noteRepository.save(note));
        log.debug("Заметка сохранена в базе данных {}", savedNote);
        return savedNote;
    }

    @Override
    public NoteDto putNote(NoteDto noteDto) {
        Optional<Note> noteOptional = noteRepository.findById(noteDto.getId());
        if (noteOptional.isEmpty()) {
            throw new NotFoundException("Заметка с id " + noteDto.getId() + " не найдена в базе данных");
        }
        NoteDto updatedNote = NoteMapper.mapNoteToNoteDto(noteRepository.save(NoteMapper.mapNoteDtoToNote(noteDto)));
        log.debug("Заметка обновлена в базе данных: {}", updatedNote);
        return updatedNote;
    }

    @Override
    public List<NoteDto> getAllNote() {
        List<NoteDto> result = noteRepository.findAll().stream()
                .map(NoteMapper::mapNoteToNoteDto)
                .collect(Collectors.toList());
        log.debug("Получен список всех заметок из базы данных");
        return result;
    }

    @Override
    public NoteDto getNoteById(Long noteId) {
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if (noteOptional.isEmpty()) {
            throw new NotFoundException("Заметка с id " + noteId + " не найдена в базе данных");
        }
        NoteDto noteDto = NoteMapper.mapNoteToNoteDto(noteOptional.get());
        log.debug("Получена заметка из базы данны {}", noteDto);
        return noteDto;
    }

    @Override
    public void deleteNote(Long noteId) {
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if (noteOptional.isEmpty()) {
            throw new NotFoundException("Заметка с id " + noteId + " не найдена в базе данных");
        }
        noteRepository.deleteById(noteId);
        log.debug("Заметка с id: {} удалена из базы данных", noteId);
    }

    @Override
    public Paged<Note> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Note> notePage = noteRepository.findAll(request);
        return new Paged<>(notePage, Paging.of(notePage.getTotalPages(), pageNumber, size));
    }
}
