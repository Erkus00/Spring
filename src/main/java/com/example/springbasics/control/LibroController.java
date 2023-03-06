package com.example.springbasics.control;

import com.example.springbasics.repo.LibroRepo;
import com.example.springbasics.repo.AutorRepo;
import model.Autor;
import model.Libro;
import model.LibroData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//https://blog.codmind.com/mi-primer-api-rest-con-spring-boot/

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    LibroRepo libroRepo;
    @Autowired
    AutorRepo autorRepo;

    @GetMapping("/all")
    public List<Libro> list() {
        return libroRepo.findAll();
    }

    @GetMapping("/simpleAll")
    public List<LibroData> simpleList() {
        List<Libro> lista = list();
        List<LibroData> lista_simple = new ArrayList<>();
        for (Libro libro : lista) {
            var lib = new LibroData();
            lib.setId(libro.getId());
            lib.setNombre(libro.getTitulo());
            lista_simple.add(lib);
        }
        return lista_simple;
    }

    @GetMapping("/info/{titulo}")
    public ResponseEntity<Libro> getLibroByTittle(@PathVariable String titulo) {
        var libro = libroRepo.findByTitulo(titulo);
        if (libro.isPresent()) {
            return new ResponseEntity<>(libro.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/allWithCategory/{categoria}")
    public List<Libro> getLibroByCat(@PathVariable String categoria) {
        return libroRepo.findByCategoria(categoria);

    }

    @GetMapping("/allWithAutor/{autor}")
    public List<Libro> getLibroByAut(@PathVariable String autor) {
        var aut = autorRepo.findByNombre(autor);
        return aut.map(value -> libroRepo.findByAutor(value)).orElse(null);
    }


    @PostMapping("/{id}")
    public ResponseEntity<Libro> post(@PathVariable Long id, @RequestBody Libro in) {
        Autor aut = autorRepo.getReferenceById(id);
        in.setAutor(aut);
        libroRepo.save(in);
        return new ResponseEntity<>(in, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Libro> delete(@PathVariable Long id) {
        if (libroRepo.existsById(id)) {
            Libro deleted = libroRepo.getReferenceById(id);
            libroRepo.deleteById(id);
            return new ResponseEntity<>(deleted, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarYmantenerAutor(@PathVariable("id") Long id, @RequestBody Libro libro) {
        Libro recuperado = libroRepo.getReferenceById(id);
        recuperado.setTitulo(libro.getTitulo());
        recuperado.setCategoria(libro.getCategoria());
        recuperado.setISBN(libro.getISBN());
        recuperado.setEdicion(libro.getEdicion());

        libroRepo.save(recuperado);

        return new ResponseEntity<>(libro, HttpStatus.OK);
    }

    @PutMapping("/{id_libro}/{id_author}")
    public ResponseEntity<Libro> actualizarConAutor(@PathVariable("id_libro") Long id, @PathVariable("id_author") Long id_auth, @RequestBody Libro libro) {
        Libro recuperado = libroRepo.getReferenceById(id);
        Autor author = autorRepo.getReferenceById(id_auth);
//        recuperado.setId(id);
        recuperado.setTitulo(libro.getTitulo());
        recuperado.setAutor(author);
        recuperado.setCategoria(libro.getCategoria());
        recuperado.setISBN(libro.getISBN());
        recuperado.setEdicion(libro.getEdicion());
        autorRepo.save(author);
        libroRepo.save(recuperado);
        return new ResponseEntity<>(libro, HttpStatus.OK);
    }
}