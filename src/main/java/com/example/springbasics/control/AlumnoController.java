package com.example.springbasics.control;

import com.example.springbasics.repo.AlumnoRepo;
import model.Alumno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//https://blog.codmind.com/mi-primer-api-rest-con-spring-boot/

@RestController
@RequestMapping("")
public class AlumnoController {

    @Autowired
    AlumnoRepo aulumnoRepo;

    @GetMapping("/alumnado/")
    public List<Alumno> list() {
        return aulumnoRepo.findAll();
    }


    @GetMapping("/alumnado/{id}")
    public ResponseEntity<Alumno> getAlumnosByID(@PathVariable Long id) {
        var alumno = aulumnoRepo.findById(id);
        if (alumno.isPresent()) {
            return new ResponseEntity<>(alumno.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("alumnado/suspensos/{modulo}")
    public List<Alumno> getSuspensoByModulo(@PathVariable String modulo) {

        if (modulo.toLowerCase().equals("ad")) {
            var alumno = aulumnoRepo.findByAdSuspensos();
            return alumno;
        } else if (modulo.toLowerCase().equals("di")) {
            var alumno = aulumnoRepo.findByIdSuspensos();
            return alumno;
        } else {
            return new ArrayList<>();
        }
    }
}