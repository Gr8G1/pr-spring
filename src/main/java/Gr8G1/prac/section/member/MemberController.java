package Gr8G1.prac.section.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/v1/members", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MemberController {
  @PostMapping
  public ResponseEntity<Map<String, String>> postMember(
    @RequestHeader("user-agent") String userAgent,
    @RequestParam("email") String email,
    @RequestParam("name") String name,
    @RequestParam("phone") String phone
  ) {
    Map<String, String> member = new HashMap<>();
    member.put("email", email);
    member.put("name", name);
    member.put("phone", phone);

    log.info("userAgent={} email={} name={} phone={}", userAgent, email, name, phone);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Client-Geo-Location", "Korea, Seoul");

    return new ResponseEntity<>(member, headers, HttpStatus.CREATED);
  }

  @GetMapping("/{member-id}")
  public ResponseEntity<String> getMember(@PathVariable("member-id") long memberId) {
    System.out.println("# memberId: " + memberId);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<String> getMembers() {
    System.out.println("# get Members");

    // not implementation
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
