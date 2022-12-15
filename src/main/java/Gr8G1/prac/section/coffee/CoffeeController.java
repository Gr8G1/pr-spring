package Gr8G1.prac.section.coffee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/coffees")
public class CoffeeController {
  @PostMapping
  public ResponseEntity<Map<String, String>> postMember(
      @RequestParam("engName") String engName,
      @RequestParam("koName") String koName,
      @RequestParam("price") String price
  ) {
    Map<String, String> coffee = new HashMap<>();

    coffee.put("engName", engName);
    coffee.put("koName", koName);
    coffee.put("price", price);

    log.info("engName={} koName={} price={}", engName, koName, price);

    return new ResponseEntity<>(coffee, HttpStatus.CREATED);
  }

  @GetMapping("/{coffee-name}")
  public ResponseEntity<String> getCoffee(@PathVariable("coffee-name") String coffeeName ) {
    System.out.println("# coffeeName: " + coffeeName);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<String> getCoffee() {
    System.out.println("# get Coffee");

    // not implementation
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
