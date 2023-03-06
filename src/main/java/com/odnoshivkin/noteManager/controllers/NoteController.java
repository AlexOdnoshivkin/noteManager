package com.odnoshivkin.noteManager.controllers;

import com.odnoshivkin.noteManager.models.NoteDto;
import com.odnoshivkin.noteManager.services.NoteService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping({"/", "index"})
@RequiredArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public String main(Model model, @RequestParam(name = "id", required = false) Long id) {
        if (id == null) {
            model.addAttribute("note", new NoteDto());
        } else {
            model.addAttribute("note", noteService.getNoteById(id));
        }
        log.info("Выведена страница редактирования с атрибутами:{}", model);
        return "index";
    }

    @GetMapping("/back")
    public String back(Model model, HttpServletRequest request) {
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping
    public String save(@Valid NoteDto noteDto, Model model) {
        NoteDto savedNote = noteService.saveNote(noteDto);
        model.addAttribute("note", savedNote);
        log.info("Сохранена заметка в базе данных:{}", savedNote);
        return "saved";
    }

    @PutMapping
    public String putNote(@Valid NoteDto noteDto, Model model) {
        NoteDto updatedNote = noteService.putNote(noteDto);
        model.addAttribute("note", updatedNote);
        log.info("Сохранены именения в заметке в базе данных:{}", updatedNote);
        return "saved";
    }

    @DeleteMapping
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

    @GetMapping("/note")
    public String getNoteById(Model model, @RequestParam(name = "id") Long id) {
        NoteDto noteDto = noteService.getNoteById(id);
        model.addAttribute("note", noteDto);
        log.info("Получена заметка: {}", noteDto);
        return "saved";
    }


}
