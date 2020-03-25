package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.impl.TagServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("tags")
public class TagController {

    private TagServiceImpl service;

    @Autowired
    public TagController(TagServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTag(@PathVariable Long id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> getTags(@RequestParam(defaultValue = "1", required = false) int pageNum,
                                                @RequestParam(defaultValue = "5", required = false) int pageSize) {
        return new ResponseEntity<>(service.getAll(pageNum, pageSize), HttpStatus.OK);
    }

    @GetMapping("/frequently-used")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<TagDTO> getFrequentlyUsedOrder() {
        return new ResponseEntity<>(service.getFrequentlyUsedOrders(), HttpStatus.OK);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<TagDTO> addTag(@RequestBody TagDTO tagDTO) {
        TagDTO local = service.addTag(tagDTO);
        return ResponseEntity
                .created(buildLocation(String.valueOf(local.getId())))
                .body(local);
    }

    @DeleteMapping("/{id}")
    @Secured("ROLE_ADMIN")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable Long id) {
        service.deleteTag(id);
    }

    private URI buildLocation(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{fileName}")
                .buildAndExpand(fileName)
                .toUri();
    }
}
