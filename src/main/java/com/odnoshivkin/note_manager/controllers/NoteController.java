package com.odnoshivkin.note_manager.controllers;

import com.odnoshivkin.note_manager.models.note.NoteDto;
import com.odnoshivkin.note_manager.services.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/")
    public String welcomePage() {
        log.info("Выведена страница приветствия");
        return "welcomePage";
    }

    @GetMapping("/back")
    public String back(HttpServletRequest request) {
        log.info("Задействована кнопка 'назад'");
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/note")
    public String noteRedactor(Model model, @RequestParam(name = "id", required = false) Long id) {
        if (id == null) {
            model.addAttribute("note", new NoteDto());
        } else {
            model.addAttribute("note", noteService.getNoteById(id));
        }
        log.info("Выведена страница редактирования с атрибутами:{}", model);
        return "redactor";
    }

    @PostMapping("/note")
    public String save(@Valid NoteDto noteDto, Model model) {
        NoteDto savedNote = noteService.saveNote(noteDto);
        model.addAttribute("note", savedNote);
        log.info("Сохранена заметка в базе данных:{}", savedNote);
        return "saved";
    }

    @PutMapping("/note")
    public String putNote(@Valid NoteDto noteDto, Model model) {
        NoteDto updatedNote = noteService.putNote(noteDto);
        model.addAttribute("note", updatedNote);
        log.info("Сохранены именения в заметке в базе данных:{}", updatedNote);
        return "saved";
    }

    @DeleteMapping("/note")
    public String deleteNote(@RequestParam(name = "id") Long id) {
        noteService.deleteNote(id);
        log.info("Удалена заметка с id {}", id);
        return "redirect:/notes";
    }

    @GetMapping("/notes")
    public String getAllNotesTitle(@RequestParam(value = "pageNumber", required = false, defaultValue = "1")
                                   int pageNumber,
                                   @RequestParam(value = "size", required = false, defaultValue = "5") int size,
                                   Model model) {
        model.addAttribute("notes", noteService.getPage(pageNumber, size));
        log.info("Выведены все заметки");
        return "notesTitleList";
    }

    @GetMapping("/note/{id}")
    public String getNoteById(Model model, @PathVariable(name = "id") Long id) {
        NoteDto noteDto = noteService.getNoteById(id);
        model.addAttribute("note", noteDto);
        log.info("Получена заметка: {}", noteDto);
        return "saved";
    }
}
