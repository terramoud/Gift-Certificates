package com.epam.esm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @PostMapping("/d")
    public ResponseEntity<SomeObject> setPage4(@RequestBody SomeObject inputSomeObject) {
        return new ResponseEntity<>(inputSomeObject, HttpStatus.SEE_OTHER);
    }
}
