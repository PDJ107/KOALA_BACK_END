package in.koala.controller;

import in.koala.annotation.Auth;
import in.koala.annotation.Xss;
import in.koala.domain.Notice;
import in.koala.domain.response.CustomBody;
import in.koala.service.HistoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @Xss
    @Auth
    @ApiOperation(value = "히스토리 조회",
            notes = "사용자가 받은 알림에 대한 전체 내역을 보여준다.",
            authorizations = @Authorization(value = "Bearer +accessToken"))
    @GetMapping(value = "/history")
    public ResponseEntity<List<Notice>> getEveryNotice(@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum){

        List<Notice> result = historyService.getEveryNotice(pageNum);

        if(result.isEmpty()){
            return new ResponseEntity(CustomBody.of("알림이 없습니다.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity(CustomBody.of(result, HttpStatus.OK), HttpStatus.OK);
        }
    }

    @Xss
    @Auth
    @ApiOperation(value = "히스토리 - 삭제", notes = "사용자가 받은 알림에 대한 전체 내역에서 알림 삭제", authorizations = @Authorization(value = "Bearer +accessToken"))
    @PatchMapping(value = "/history")
    public ResponseEntity deleteNotice(@RequestParam("notice-id") List<Integer> noticeList){

        if(noticeList.isEmpty()){
            return new ResponseEntity(CustomBody.of("알림을 선택해야합니다.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        else{
            historyService.deleteNotice(noticeList);
            return new ResponseEntity(CustomBody.of("선택한 알림을 삭제하였습니다.", HttpStatus.OK), HttpStatus.OK);
        }
    }

    @Xss
    @Auth
    @ApiOperation(value = "키워드 목록 페이지 - 알림 읽음 처리", notes = "키워드 목록에서 하나의 키워드를 선택한 후 나온 알림에 대해서 \n 클릭시 알림 읽음 처리", authorizations = @Authorization(value = "Bearer +accessToken"))
    @PutMapping(value = "/history")
    public ResponseEntity noticeRead(@RequestParam(name = "notice-id") String noticeId){

        if(historyService.noticeRead(noticeId)){
            return new ResponseEntity(CustomBody.of("알림을 읽었습니다.", HttpStatus.OK), HttpStatus.OK);
        }
        else{
            return new ResponseEntity(CustomBody.of("알림읽는것을 실패하였습니다.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}