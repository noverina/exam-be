package porto.exam.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptionsController {
    @RequestMapping(method = RequestMethod.OPTIONS, path = "/**")
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }
}