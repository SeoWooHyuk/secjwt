package com.spring.secjwt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.secjwt.dao.BoardDao;
import com.spring.secjwt.vo.BoardVo;
import com.spring.secjwt.vo.Pagination;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardService {

    @Autowired
    BoardDao bd;

    //ajax 보드게시판 불러오기 비동기 처리
    public Map<String, Object> boardsallselectajax(BoardVo searchVO) {

        int totCnt = bd.getListcount(); //토탈 셀렉 수
        int totCnt2 = bd.boardsallselectlistcount(searchVO); //토탈 검색 셀렉 수
        int totalPageCnt = (int)Math.ceil(totCnt / (double)searchVO.getPageUnit());

        Pagination pagination = new Pagination();
        pagination.setCurrentPageNo(searchVO.getPageIndex()); //현재 페이지 번호
        pagination.setRecordCountPerPage(searchVO.getPageUnit()); //한 페이지당 게시되는 게시물 수
        pagination.setPageSize(searchVO.getPageSize()); //페이지 리스트에 게시되는 페이지 수
        pagination.setTotalRecordCount(totCnt2); 
        searchVO.setFirstIndex(pagination.getFirstRecordIndex()); //페이징 sql의 조건절에 사용되는 시작 rownum
        searchVO.setRecordCountPerPage(pagination.getRecordCountPerPage()); //한 페이지당 게시되는 게시물 수

        List<BoardVo> boardList=bd.boardsallselect(searchVO);
        Map<String, Object> boardMap = new HashMap<>();
        boardMap.put("boardList", boardList);
        boardMap.put("totalPageCnt", totalPageCnt);
        boardMap.put("totCnt", totCnt);
        boardMap.put("pagination", pagination);
        return boardMap;
    }

    public Integer boardsallselectlistcount(BoardVo searchVO)
    {
        return bd.boardsallselectlistcount(searchVO);
    }

    public  Integer getListcount()
    {
      return bd.getListcount();
    }

    public Integer getListmax()
    {
       return bd.getListmax();
    }


    public BoardVo boarddetail(int boardnum)
    {
        BoardVo board = bd.boarddetail(boardnum);
        return board;
    }

    public Integer boardinsert(BoardVo searchVO)
    {
        Integer intI = bd.boardinsert(searchVO);
        return intI;
    }

    public Integer boarddetaildelete(int boardnum)
    {
        int intI = bd.boarddetaildelete(boardnum);
        return intI;
    }

    public Integer boardupdate(BoardVo updatevo)
    {
        System.out.println("확인");
        Integer intI = bd.boardupdate(updatevo);
        return intI;
    }
    





    
}
