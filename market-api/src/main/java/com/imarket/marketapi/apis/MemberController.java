package com.imarket.marketapi.apis;

import com.imarket.marketapi.apis.dto.MemberDto;
import com.imarket.marketapi.apis.response.MultiResponse;
import com.imarket.marketapi.apis.response.SingleResponse;
import com.imarket.marketdomain.domain.Member;
import com.imarket.marketdomain.service.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/${api.version}/members", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class MemberController {
    private MemberService memberService;
    private ModelMapper modelMapper;

    @Autowired
    public MemberController(MemberService memberService, ModelMapper modelMapper) {
        this.memberService = memberService;
        this.modelMapper = modelMapper;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<SingleResponse<MemberDto>> postMember(@Valid @RequestBody MemberDto.Post body) {
        Member member = memberService.saveMember(modelMapper.map(body, Member.class));
        // TODO implementation: 이메일 전송

        return ResponseEntity.ok(new SingleResponse(HttpStatus.OK, modelMapper.map(member, MemberDto.class)));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{member-id}")
    public ResponseEntity<SingleResponse<MemberDto>> getMember(@PathVariable("member-id") long memberId) {
        Member member = memberService.findMemberById(memberId);
        return ResponseEntity.ok(new SingleResponse<>(HttpStatus.OK, modelMapper.map(member, MemberDto.class)));
    }

    // TODO: 요구 사항이 조금 모호해서 구체적인 요구사항을 정의할 필요가 있음.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<MultiResponse<MemberDto>> getAllMembers(@RequestParam("page") int page,
                                                                  @RequestParam("size") int size,
                                                                  MemberDto.Search searchDto) {
        Page<Member> memberPage = memberService.searchMember(searchDto.getEmail(),
                searchDto.getName(), page, size);
        List<MemberDto> memberDtoList = memberPage.getContent()
                .stream()
                .map(member -> {
                    MemberDto memberDto = new MemberDto();
                    return memberDto.toMemberDto(member);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(new MultiResponse<>(HttpStatus.OK, memberDtoList, memberPage));
    }
}
