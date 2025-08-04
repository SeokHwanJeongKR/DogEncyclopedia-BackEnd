package com.est.mungpe.pedia.contorller;


import com.est.mungpe.pedia.domain.PediaEditRequest;
import com.est.mungpe.pedia.dto.*;
import com.est.mungpe.pedia.service.PediaEditRequestService;
import com.est.mungpe.pedia.service.PediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("api/pedia")
@RequiredArgsConstructor
public class PediaController {

    private final PediaService pediaService;
    private final PediaEditRequestService pediaEditRequestService;

    // 글 작성
    @PostMapping
    public ResponseEntity<PediaBasicResponse> createPost
            (@RequestPart("data") PediaBasicRequest request, @RequestPart("images") List<MultipartFile> images) throws IOException {

        log.info("Create Pedia 컨트롤러 도착");

        PediaBasicResponse result = pediaService.createPedia(request, images);
        log.info("result = {}", result);


        return  ResponseEntity.ok(result);
    }

    //글 수정
    @PutMapping(value = "{postId}", consumes = "multipart/form-data")
    public ResponseEntity<PediaBasicResponse> updatePost
            (@PathVariable Long postId, @RequestPart("data") PediaBasicRequest request, @RequestPart("images")List<MultipartFile> images) throws IOException {
        log.info("Update Pedia Update 컨트롤러 도착");

        PediaBasicResponse result = pediaService.updatePedia(postId, request, images);
        log.info("result = {}", result);

        return  ResponseEntity.ok(result);
    }



    //글 삭제
    @DeleteMapping(value = "{postId}")
    public ResponseEntity<PediaBasicResponse> deletePost(@PathVariable Long postId) {

        log.info("Delete Pedia 컨트롤러 도착");

        PediaBasicResponse result = pediaService.deletePedia(postId);
        log.info("result = {}", result);

        return  ResponseEntity.ok(result);
    }

    //특정 글 조회
    @GetMapping(value = "{postId}")
    public ResponseEntity<GetPediaResponse> getPost(@PathVariable Long postId) {

        log.info("Get Pedia 컨트롤러 도착");

        GetPediaResponse result = pediaService.getPedia(postId);
        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    //탑 12조회
    @GetMapping(path = "/top12")
    public ResponseEntity<Top12PediaResponse> getTop12Pedia() {

        log.info("Get Top2 컨트롤러 도착");

        Top12PediaResponse result = pediaService.getTop12Pedia();
        log.info("result = {}", result);


        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/all/{page}")
    public ResponseEntity<GetAllPediaResponse> getAllPedia(@PathVariable int page) {

        log.info("Get all 컨트롤러 도착");

        GetAllPediaResponse result = pediaService.getAllPedia(page);
        log.info("result = {}", result);


        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/editRequest")
    public ResponseEntity<PediaBasicResponse> createEditRequest(@RequestPart("data") PediaEditRequestDto request) {

        log.info("Create EditRequest 컨트롤러 도착");

        PediaBasicResponse result = pediaEditRequestService.saveEditRequest(request);

        log.info("result = {}", result);


        return ResponseEntity.ok(result);

    }

    @GetMapping(path = "/editRequest/all/{page}")
    public ResponseEntity<GetAllPediaEditRequestResponse> getAllPediaEditRequest(@PathVariable int page) {

        log.info("Get All Edit Request 컨트롤러 도착");

        GetAllPediaEditRequestResponse result = pediaEditRequestService.getAllEditRequest(page);
        log.info("result = {}", result);


        return ResponseEntity.ok(result);
    }

    @GetMapping("/editRequest/{id}")
    public ResponseEntity<GetEditRequestDto> getEditRequest(@PathVariable Long id) {

        log.info("Get Edit Request 컨트롤러 도착");

        GetEditRequestDto result = pediaEditRequestService.getEditRequest(id);
        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/editRequest/accept")
    public ResponseEntity<PediaBasicResponse> responseUpateAboutEditRequest(@RequestPart("data") EditRequestAcceptRequest request) {

        log.info("Post Edit Request Accept 컨트롤러 도착");
        log.info("request.getRequestId() = {}", request.getRequestId());
        log.info("request.getNewInfo() = {}", request.getNewInfo());

        PediaBasicResponse result = pediaService.responseUpateAboutEditRequest(request);
        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "/editRequest/accept/{requestId}")
    public ResponseEntity<PediaBasicResponse> responseUpateAboutEditRequest(@PathVariable long requestId) {

        log.info("Post Edit Request Delete 컨트롤러 도착");

        PediaBasicResponse result = pediaService.deleteEditRequest(requestId);
        log.info("result = {}", result);

        return ResponseEntity.ok(result);
    }





}
