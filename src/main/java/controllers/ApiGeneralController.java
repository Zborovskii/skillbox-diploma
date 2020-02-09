package controllers;

import main.model.GeneralBlogInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiGeneralController {

    @GetMapping(name = "/api/init")
    public ResponseEntity getGeneralBlogInfo() {

        return new ResponseEntity(new GeneralBlogInfo(), HttpStatus.OK);
    }



}
