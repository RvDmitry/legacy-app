package my.home.legacyapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.home.legacyapp.dto.BusinessDto;
import my.home.legacyapp.entity.BusinessType;
import my.home.legacyapp.service.BusinessService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    @GetMapping("/value/{id}")
    public ResponseEntity<BusinessDto> getById(@PathVariable("id") Long id,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        log.info("User [{}] gets value by Id [{}]", userDetails.getUsername(), id);
        var dto = businessService.getById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/values/created")
    public ResponseEntity<Page<BusinessDto>> pageByCreatedAt(
            @RequestParam("dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("User [{}] gets page values by creation date", userDetails.getUsername());
        var page = businessService.pageByCreatedDate(dateFrom, dateTo, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/values/types")
    public ResponseEntity<Page<BusinessDto>> pageByType(@RequestParam("types") Set<BusinessType> types,
                                                        Pageable pageable,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        log.info("User [{}] gets page values by types", userDetails.getUsername());
        var page = businessService.pageByType(types, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/value")
    public ResponseEntity<BusinessDto> addValue(@RequestBody BusinessDto dto,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        log.info("User [{}] adds value [{}]", userDetails.getUsername(), dto);
        var savedDto = businessService.save(dto);
        return new ResponseEntity<>(savedDto, HttpStatus.CREATED);
    }

    @PatchMapping("/value")
    public ResponseEntity<BusinessDto> editValue(@RequestBody BusinessDto dto,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        log.info("User [{}] changes value [{}]", userDetails.getUsername(), dto);
        var updatedDto = businessService.update(dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/value/{id}")
    public ResponseEntity<String> deleteValue(@PathVariable("id") Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        log.info("User [{}] deletes value by id [{}]", userDetails.getUsername(), id);
        businessService.delete(id);
        return new ResponseEntity<>(String.format("Value with id [%d] was delete", id), HttpStatus.OK);
    }
}
