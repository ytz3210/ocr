package com.el.controller;

import com.el.exception.MyException;
import com.el.exception.UserException;
import com.el.to.ResponseTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rolandz on 2016/9/29.
 */
@ControllerAdvice
public class BaseRestController {
    protected static Logger LOG = LoggerFactory.getLogger(BaseRestController.class);

    private Map<String, String> map = new HashMap<>();

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseTO handleException(Throwable t) {
        LOG.warn(t.getMessage(), t);
        map.put("fileName", "");
        return error(t.getMessage(), map);
    }
    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseTO multipartException(Throwable t) {
        LOG.warn(t.getMessage(), t);
        map.put("fileName", "");
        return error("您未选择文件，请选择文件后重新上传！", map);
    }
    @ExceptionHandler({UserException.class})
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView handleDocumentException(Throwable t) {
        LOG.warn(t.getMessage(), t);
        ModelAndView mv = new ModelAndView();
        String str = t.getMessage();
        if (str.contains(" ")) {
            map.put("fileName", str.substring(str.indexOf(" ")));
            mv.addObject("msg", error(str.substring(0, str.indexOf(" ")), map));
        }else {
            map.put("fileName","");
            mv.addObject("msg", error(str, map));
        }
        mv.setViewName("fileupload");
        return mv;
    }

    @ExceptionHandler({MyException.class})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseTO documentException(Throwable t) {
        LOG.warn(t.getMessage(), t);
        String str = t.getMessage();
        if(str.contains(" ")){
            map.put("fileName", str.substring(str.indexOf(" ")));
            return error(str.substring(0, str.indexOf(" ")), map);
        }else {
            map.put("fileName","");
            return error(str,map);
        }
    }

    protected ResponseTO success(Object o) {
        return ResponseTO.init(0, o, null);
    }

    protected ResponseTO success() {
        return ResponseTO.init(0, null, null);
    }

    protected ResponseTO error(String message, Object o) {
        return ResponseTO.init(1, o, message);
    }
}
