package com.spring.secjwt.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.spring.secjwt.service.BoardService;
import com.spring.secjwt.vo.BoardVo;
import com.spring.secjwt.vo.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;



@RestController
@RequestMapping(value = "/secjwt")
@Slf4j
public class BoardController {
    
    @Autowired
    BoardService boardService;

    @RequestMapping(value = "/board", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView  boardsallselect()
    {
        
        int totCnt = boardService.getListcount(); //토탈 셀렉 수
        
        log.info(""+totCnt +"파일");
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

    
}
