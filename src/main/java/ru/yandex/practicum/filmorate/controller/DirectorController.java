package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.Collection;
@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public Collection<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirector(@PathVariable int id) {
        return directorService.getDirector(id);
    }

    @PostMapping
    public Director create(@Valid @RequestBody Director director) {
        directorService.addDirector(director);
        return director;
    }

    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable int id) {
        directorService.deleteDirector(id);
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        directorService.updateDirector(director);
        return director;
    }
}
