package Gr8G1.prac.section.member;

import Gr8G1.prac.section.member.dto.MemberPostDto;
import Gr8G1.prac.section.member.dto.MemberPatchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping(value = "/v1/members", produces = {MediaType.APPLICATION_JSON_VALUE})
public class MemberController {
  @PostMapping
  public ResponseEntity<?> postMember(@Valid @RequestBody MemberPostDto memberPostDto) {
    return new ResponseEntity<>(memberPostDto, HttpStatus.CREATED);
  }

  @PatchMapping("/{member-id}")
  public ResponseEntity<?> patchMember(
      @PathVariable("member-id") long memberId,
      @RequestBody MemberPatchDto memberPatchDto
  ) {
    memberPatchDto.setMemberId(memberId);
    memberPatchDto.setName("홍길동");

    // No need Business logic

    return new ResponseEntity<>(memberPatchDto, HttpStatus.OK);
  }

  @GetMapping("/{member-id}")
  public ResponseEntity<?> getMember(@PathVariable("member-id") @Min(1) long memberId) {
    System.out.println("# memberId: " + memberId);

    // not implementation
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<?> getMembers() {
    System.out.println("# get Members");

    // not implementation
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // 회원 정보 삭제
  @DeleteMapping("/{member-id}")
  public ResponseEntity<?> deleteMember(@PathVariable("member-id") long memberId) {
    // No need business logic

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
