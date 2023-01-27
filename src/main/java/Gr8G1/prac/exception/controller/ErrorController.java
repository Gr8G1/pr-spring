package Gr8G1.prac.exception.controller;

import Gr8G1.prac.exception.dto.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping(
  value = "/api/error",
  produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class ErrorController {
  @GetMapping
  public ResponseEntity<?> getErrorApi(
    @Positive @RequestParam Integer code
  ) {
    if (code == 500) throw new RuntimeException("알 수 없는 예외!!!");

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/{id}")
  public ResponseEntity<?> postErrorApi(
    @Positive @PathVariable("id") Long id,
    @Valid @RequestBody ErrorDto.Post requestBody
  ) {
    log.info("id={}", id);
    log.info("requestBody={}", requestBody);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
