package com.spring.secjwt.controller;

import java.util.HashMap;
import java.util.Map;
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
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;



@RestController
@RequestMapping(value = "/secjwt")
@Slf4j
public class BoardController {
    
    @Autowired
    BoardService boardService;

    @Autowired
    Boardfileupload boardfileupload;

    @RequestMapping(value = "/board", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView  boardsallselect()
    {
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("board");
        return mv;
    }

     // 게시판 목록 조회  ajax비동기 통신전달
	@RequestMapping(value = "/boardsallselectajax", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView boardsallselectajax(@ModelAttribute("searchVO") BoardVo searchVO, @RequestBody BoardVo searchVO2 )  {

        searchVO.setSearchKeyword(searchVO2.getSearchKeyword());
        searchVO.setPageIndex(searchVO2.getPageIndex());

        Pagination pagination = new Pagination();
        pagination.setCurrentPageNo(searchVO.getPageIndex()); //현재 페이지 번호
        pagination.setRecordCountPerPage(searchVO.getPageUnit()); //한 페이지당 게시되는 게시물 수
        pagination.setPageSize(searchVO.getPageSize()); //페이지 리스트에 게시되는 페이지 수
        searchVO.setFirstIndex(pagination.getFirstRecordIndex()); //페이징 sql의 조건절에 사용되는 시작 rownum
        searchVO.setRecordCountPerPage(pagination.getRecordCountPerPage()); //한 페이지당 게시되는 게시물 수

        int totCnt = boardService.getListcount(); //토탈 셀렉 수
       
        Integer totCnt2 = boardService.boardsallselectlistcount(searchVO2); //토탈 검색 셀렉 수
        pagination.setTotalRecordCount(totCnt2);  //토탈 페이지리스트 수

		ModelAndView mv = new ModelAndView();
		Map<String, Object> boardMap = new HashMap<String, Object>();
        boardMap = boardService.boardsallselectajax(searchVO); //테이블셀렉
        
        Map<String, Object> totalPagewrite = new HashMap<String, Object>();
		totalPagewrite.put("totalPageCnt", (int)Math.ceil(totCnt / (double)searchVO.getPageUnit())); //
        totalPagewrite.put("totCnt",totCnt); //총게시글수

        mv.addObject("totalPagewrite", totalPagewrite); //총게시물
		mv.addObject("boardMap", boardMap);  //테이블 게시글 셀렉에 이용
        mv.addObject("pagination",pagination); //페이징 스크립트에이용 이용
		mv.setViewName("jsonView"); //클라이언트로
		return mv;
	}

    @GetMapping("/boardinsert")  //게시글 인설트 페이지
    public ModelAndView boardinsert(HttpSession session)
    {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("boardinsert");
        return mv;
    }

    @PostMapping("/boardinsert")  //게시글 인설트  + 파일첨부 확인
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


    @GetMapping("/boarddetail")  //게시글 디테일 창
    public ModelAndView boarddetail(@RequestParam Integer boardnum, Model model)
    {
        ModelAndView mv = new ModelAndView();
        BoardVo board =  boardService.boarddetail(boardnum);
        log.info(""+ board.getFiles() +"파일이름확인");
        model.addAttribute("board", board);
        mv.setViewName("boarddetail");
        return mv; 
    }

    @GetMapping("/boardupdate")  //게시글 update 창
    public ModelAndView boardupdate(@RequestParam Integer boardnum, Model model)
    {
        ModelAndView mv = new ModelAndView();
        BoardVo board =  boardService.boarddetail(boardnum);
        model.addAttribute("board", board);
        mv.setViewName("boardupdate");
        return mv; 
    }

    @PutMapping("/boardupdate")  //게시글 update alter 창
    public ModelAndView boardupdateok(@ModelAttribute BoardVo updatevo)
    {

        ModelAndView mv = new ModelAndView();
        int intI =  boardService.boardupdate(updatevo);
        mv.setViewName("redirect:boarddetail?boardnum="+updatevo.getBoardnum());
        return mv; 
    }
 
}
