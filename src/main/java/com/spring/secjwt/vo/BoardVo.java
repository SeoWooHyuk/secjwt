package com.spring.secjwt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "게시물 리스트 응답DTO")
@Data
public class BoardVo extends PageVo {
    @Schema(description = " 게시글번호")
    private int boardnum;
    private String id;
    private String title;
    private String writes;
    private String date;
    private String files;
    private String searchKeyword; //검색키워드 생성
}
