package com.spring.secjwt.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.spring.secjwt.vo.BoardVo;


@Mapper
public interface BoardDao {

    public List<BoardVo> boardsallselect(BoardVo  searchvo);

    public Integer boardsallselectlistcount(BoardVo  searchvo);

    public Integer getListcount();

    public Integer getListmax();

    public BoardVo boarddetail(Integer boardnum);

    public Integer boardinsert(BoardVo searchvo);

    public Integer boarddetaildelete(int boardnum);

    public Integer boardupdateok(BoardVo updatevo);

}
