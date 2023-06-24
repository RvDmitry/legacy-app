package my.home.legacyapp.controller;

import lombok.RequiredArgsConstructor;
import my.home.legacyapp.dto.BusinessDto;
import my.home.legacyapp.entity.BusinessType;
import my.home.legacyapp.service.BusinessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping("/value/{id}")
    public ResponseEntity<BusinessDto> getById(@PathVariable("id") Long id) {
        var dto = businessService.getById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/values/created")
    public ResponseEntity<Page<BusinessDto>> pageByCreatedAt(
            @RequestParam("dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            Pageable pageable) {
        var page = businessService.pageByCreatedDate(dateFrom, dateTo, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/values/types")
    public ResponseEntity<Page<BusinessDto>> pageByType(@RequestParam("types") Set<BusinessType> types, Pageable pageable) {
        var page = businessService.pageByType(types, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/value")
    public ResponseEntity<BusinessDto> addValue(@RequestBody BusinessDto dto) {
        var savedDto = businessService.save(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @PatchMapping("/value")
    public ResponseEntity<BusinessDto> editValue(@RequestBody BusinessDto dto) {
        var updatedDto = businessService.update(dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/value/{id}")
    public ResponseEntity<String> deleteValue(@PathVariable("id") Long id) {
        businessService.delete(id);
        return new ResponseEntity<>(String.format("Value with id [%d] was delete", id), HttpStatus.OK);
    }
}
