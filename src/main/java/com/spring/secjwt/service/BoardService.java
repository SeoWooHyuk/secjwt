package com.spring.secjwt.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.secjwt.dao.BoardDao;
import com.spring.secjwt.vo.BoardVo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BoardService {

    @Autowired
    BoardDao bd;

    //ajax 보드게시판 불러오기 비동기 처리
    public Map<String, Object> boardsallselectajax(BoardVo searchVO) {

        List<BoardVo> boardList=bd.boardsallselect(searchVO);
        Map<String, Object> boardMap = new HashMap<>();
        boardMap.put("boardList", boardList);
        return boardMap;
    }


    public Integer boardsallselectlistcount(BoardVo searchVO)
    {
        return bd.boardsallselectlistcount(searchVO);
    }

    public Integer getListcount()
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

    public Integer boardupdateok(BoardVo updatevo)
    {
        Integer intI = bd.boardupdateok(updatevo);
        return intI;
    }
    





    
}
