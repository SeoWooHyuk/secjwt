package com.spring.secjwt.controller;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class DownloadController  {
    
@Value("${resource}")
private String upload; //업로드된 파일 저장 경로

@GetMapping("/download")
  public void download(HttpServletResponse response, @RequestParam String filename) throws IOException {
    log.info(""+ upload +"파일");

    String path = upload+filename;
    log.info(""+ path +"패스");
    System.out.println("제발 읽어죠");
    
    byte[] fileByte = FileUtils.readFileToByteArray(new File(path));

    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(filename, "UTF-8")+"\";");
    response.setHeader("Content-Transfer-Encoding", "binary");

    response.getOutputStream().write(fileByte);
    response.getOutputStream().flush();
    response.getOutputStream().close();
  }
    
}
