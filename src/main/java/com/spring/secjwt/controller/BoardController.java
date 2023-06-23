package com.spring.secjwt.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.spring.secjwt.config.Boardfileupload;
import com.spring.secjwt.service.BoardService;
import com.spring.secjwt.vo.BoardVo;
import com.spring.secjwt.vo.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;



@Tag(name = "게시판(BoardController)", description = "게시판 API")
@RestController
@RequestMapping(value = "/secjwt")
@Slf4j
public class BoardController {
    
    @Autowired
    BoardService boardService;

    @Autowired
    Boardfileupload boardfileupload;

    @GetMapping(value = "/board")
    public ModelAndView  boardsallselect()
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("board");
        return mv;
    }

     // 게시판 목록 조회  ajax비동기 통신전달
	@PostMapping(value = "/board")
	public ModelAndView boardsallselectajax(@RequestBody BoardVo searchVO) throws Exception {

		ModelAndView mv = new ModelAndView();
		Map<String, Object> boardMap =  boardService.boardsallselectajax(searchVO); //테이블셀렉
		mv.addObject("boardMap", boardMap);  //테이블 게시글 셀렉에 이용
		mv.setViewName("jsonView"); //클라이언트로
		return mv;
	}

    @Operation(summary = "게시글 조회", description = "boardnum 이용하여 posts 레코드를 조회합니다.", responses = {
    @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = BoardVo.class))),
    @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value ="/board/{boardnum}")  //게시글 디테일 창
    public ModelAndView boarddetail(@RequestParam Integer boardnum, Model model) throws Exception
    {
        ModelAndView mv = new ModelAndView();
        BoardVo board =  boardService.boarddetail(boardnum);
        model.addAttribute("board", board);
        mv.setViewName("boarddetail");
        return mv; 
    }

    @GetMapping("/write")  //게시글 인설트 페이지
    public ModelAndView boardinsert(HttpSession session) throws Exception
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("boardinsert");
        return mv;
    }

    @PostMapping("/write")  //게시글 인설트  + 파일첨부 확인
    public ModelAndView viewsinsertok(@ModelAttribute BoardVo searchVO, @RequestParam("file") MultipartFile file) throws Exception
    {
        ModelAndView mv = new ModelAndView();
        Integer maxnum = 0; 
        String img = boardfileupload.uploadFile(file);

        if(boardService.getListmax() != null){   //오토인크리먼트 역활 처리
            maxnum =boardService.getListmax() + 1;
        }
        else { maxnum = 1; }

        searchVO.setBoardnum(maxnum);
        searchVO.setFiles(img);
        int intI = boardService.boardinsert(searchVO); 
        mv.setViewName("redirect:boarddetail?boardnum="+searchVO.getBoardnum());
        return mv;
    }


   

    @GetMapping("/update")  //게시글 update 창
    public ModelAndView boardupdate(@RequestParam Integer boardnum, Model model)throws Exception
    {
        ModelAndView mv = new ModelAndView();
        BoardVo board =  boardService.boarddetail(boardnum);
        model.addAttribute("board", board);
        mv.setViewName("boardupdate");
        return mv; 
    }

    @PutMapping("/update")  //게시글 update alter 창
    public ModelAndView boardupdateok(@ModelAttribute BoardVo updatevo)throws Exception
    {

        ModelAndView mv = new ModelAndView();
        int intI =  boardService.boardupdate(updatevo);
        mv.setViewName("redirect:boarddetail?boardnum="+updatevo.getBoardnum());
        return mv; 
    }
 
}
