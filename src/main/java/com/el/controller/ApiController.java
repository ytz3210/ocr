package com.el.controller;

import com.el.exception.MyException;
import com.el.exception.UserException;
import com.el.service.ParserService;
import com.el.to.ResponseTO;
import com.el.util.XMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.util.Map;

@Controller
public class ApiController extends BaseRestController {

    @Autowired
    ThreadPoolTaskExecutor executor;

    @Resource
    public ParserService parserService;

    private Logger logger = LoggerFactory.getLogger(ApiController.class);
    /**
     * 用户上传文件 解析
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/parse")
    public ModelAndView parseContent(@RequestParam("file") MultipartFile file, ModelAndView mv) throws UserException,IOException {
        String fileName = file.getOriginalFilename();
        if ("".equals(fileName) || fileName == null) {
            throw new UserException("未选择文件，请重新提交");
        }
        try{
            Map<String, Object> content = parserService.parseText(fileName, file.getInputStream());
            content.put("fileName", fileName);
            mv.addObject("msg", success(content));
            mv.setViewName("fileupload");
        } catch (IOException e){
            logger.error(e.getMessage(),e);
            throw new IOException(e.getMessage()+" "+fileName);
        }catch (Exception e){
            throw new UserException(e.getMessage()+" "+fileName);
        }
        return mv;
    }

    @GetMapping("/parse")
    public String redirectHome() {
        return "redirect:/";
    }

    @GetMapping("/api")
    public String toApi(HttpServletRequest request) {
        String path = request.getRequestURL().toString();
        System.out.println(path);
        XMLParser.parse(path);
        return "api-test";
    }


    /**
     * 开发者自己上传文件 解析
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/api/parseForm")
    public ResponseTO upload(@RequestParam("file") MultipartFile file) throws MyException, IOException {
        String filename = file.getOriginalFilename();
        if ("".equals(filename) || filename == null) {
            throw new MyException("未选择文件，请重新提交");
        }
        try{
            Map<String, Object> content = parserService.parseText(filename, file.getInputStream());
            content.put("fileName", filename);
            return success(content);
        }catch (IOException e){
            logger.error(e.getMessage(),e);
            throw new IOException(e.getMessage()+" "+filename);
        }catch (Exception e){
            throw new MyException(e.getMessage()+" "+filename);
        }

    }

}


