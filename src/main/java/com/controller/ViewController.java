package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 视图控制器，用于跳转指定页面
 */
@Controller
@RequestMapping("/view")
public class ViewController {

    @GetMapping("/{path}")
    public String view(@PathVariable String path){
        System.out.println("访问页面:[ "+path+" ]");

        return path;
    }

    @GetMapping("/{path1}/{path2}")
    public String view(@PathVariable String path1,@PathVariable String path2){
        String path = path1+"/"+path2;
        System.out.println("访问页面:[ "+path+" ]");

        return path;
    }

    @GetMapping("/{path1}/{path2}/{path3}")
    public String view(@PathVariable String path1,@PathVariable String path2,@PathVariable String path3){
        String path = path1+"/"+path2+"/"+path3;
        System.out.println("访问页面:[ "+path+" ]");

        return path;
    }

}
