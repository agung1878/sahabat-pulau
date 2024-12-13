package org.sahabat.pulau.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.sahabat.pulau.helper.PropertiesHelper;
import org.sahabat.pulau.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.FileNameMap;
import java.net.URLConnection;

@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {

    private FileNameMap fileNameMap = URLConnection.getFileNameMap();

    @Autowired private FileService fileService;
    @Autowired private PropertiesHelper propertiesHelper;

    @GetMapping("/{folder}/{prefix}/{name}")
    public ResponseEntity<byte[]> viewFile(@PathVariable String folder,@PathVariable String prefix, @PathVariable String name){
        try {
            byte[] data = fileService.load(getFolder(folder),prefix, name);
            if(data == null) {return ResponseEntity.notFound().build();}

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");
            headers.add(HttpHeaders.CONTENT_TYPE, fileNameMap.getContentTypeFor(name));
            return ResponseEntity.ok().headers(headers).body(data);
        } catch (Exception err) {
            log.error(err.getMessage(), err);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/download/{folder}/{prefix}/{name}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String folder,@PathVariable String prefix, @PathVariable String name, HttpServletResponse response){
        try {

            byte[] data = fileService.load(getFolder(folder),prefix, name);
            if(data == null) {return ResponseEntity.notFound().build();}

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment");
            headers.add(HttpHeaders.CONTENT_TYPE, fileNameMap.getContentTypeFor(name));

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception err) {
            log.error("ERROR DOWNLOADING FILE", err);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private String getFolder(String folder){
        if("uploaded".equalsIgnoreCase(folder)){
            return propertiesHelper.getUploadFolder();
        }else{
            return propertiesHelper.getReportFolder();
        }
    }
}
