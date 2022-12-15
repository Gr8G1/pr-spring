package Gr8G1.prac.section.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
  @PostMapping
  public String postOrder(
    @RequestParam("memberId") long memberId,
    @RequestParam("coffeeId") long coffeeId
  ) {
    log.info("memberId={} coffeeId={}", memberId, coffeeId);

    return "{\"" +
        "memberId\":\"" + memberId + "\"," +
        "\"coffeeId\":\"" + coffeeId + "\"" +
        "}";
  }

  @GetMapping("/{order-id}")
  public String getOrder(@PathVariable("order-id") long orderId) {
    System.out.println("# orderId: " + orderId);

    // not implementation
    return null;
  }

  @GetMapping
  public String getOrders() {
    System.out.println("# get Orders");

    // not implementation
    return null;
  }
}
