package likelion.be.areteum.global.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name="TestController", description = "테스트용 컨트롤러")
public class TestController {
    @Operation(summary = "테스트")
    @GetMapping("/")
    @ResponseBody
    public String test(){
        return "Hello, World";
    }

}
