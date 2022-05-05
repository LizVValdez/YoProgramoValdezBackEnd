package com.Portfolio.YoProgramoValdez.controller;

import com.Portfolio.YoProgramoValdez.DTO.Mensaje;
import com.Portfolio.YoProgramoValdez.entity.Habilidad;
import com.Portfolio.YoProgramoValdez.service.HabilidadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habilidad")
@CrossOrigin(origins = "http://localhost:4200")

public class HabilidadController {

    @Autowired
    HabilidadService habilidadService;

    @GetMapping("/lista")
    public ResponseEntity<List<Habilidad>> getListaHabilidad(){
        List<Habilidad> lista = habilidadService.getList();
        return new ResponseEntity<List<Habilidad>>(lista, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nueva")
    public ResponseEntity<?> create(@RequestBody Habilidad habilidad){
        if(StringUtils.isBlank(habilidad.getNombreHabilidad()))
            return new ResponseEntity(new Mensaje("El Nombre de la Habilidad es Obligatorio"), HttpStatus.BAD_REQUEST);
        if((Integer)habilidad.getPorcentajeHabilidad() == null || habilidad.getPorcentajeHabilidad()==0)
            return new ResponseEntity(new Mensaje("El procentaje de la Habilidad es Obligatorio"), HttpStatus.BAD_REQUEST);
        habilidadService.save(habilidad);
        return new ResponseEntity(new Mensaje("Nueva Habilidad Agregada"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> update(@RequestBody Habilidad habilidad, @PathVariable("id") Long id){
        if(!habilidadService.existsHabilidadId(id))
            return new ResponseEntity(new Mensaje("No Existe la Habilidad Indicada"), HttpStatus.NOT_FOUND);
        if((Integer)habilidad.getPorcentajeHabilidad() == null || habilidad.getPorcentajeHabilidad()==0)
            return new ResponseEntity(new Mensaje("El procentaje de la Habilidad es Obligatorio"), HttpStatus.BAD_REQUEST);

        Habilidad habUpdate = habilidadService.getHabilidadId(id).get();
        habUpdate.setNombreHabilidad(habilidad.getNombreHabilidad());
        habUpdate.setPorcentajeHabilidad(habilidad.getPorcentajeHabilidad());
        habilidadService.save(habUpdate);

        return new ResponseEntity(new Mensaje("Habilidad Actualizada"), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(!habilidadService.existsHabilidadId(id))
            return new ResponseEntity(new Mensaje("No Existe la Habilidad Indicada"), HttpStatus.NOT_FOUND);
        habilidadService.delete(id);
        return new ResponseEntity(new Mensaje("Habilidad Eliminada"), HttpStatus.OK);
    }
}