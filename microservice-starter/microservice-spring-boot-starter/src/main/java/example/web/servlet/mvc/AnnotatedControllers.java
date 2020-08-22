package example.web.servlet.mvc;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class AnnotatedControllers {

    @GetMapping(value = "/header", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public void headerMethod(){

    }

    @GetMapping
    public String setupForm(@RequestParam("petId") int petId, Model model) {
        model.addAttribute("pet", petId);
        return "petForm";
    }

    @GetMapping("/demo")
    public void handle(
            @RequestHeader("Accept-Encoding") String encoding,
            @RequestHeader("Keep-Alive") long keepAlive) {
        //...
    }

    /**
     * Note that using @ModelAttribute is optional (for example, to set its attributes).
     * By default, any argument that is not a simple value type (as determined by BeanUtils#isSimpleProperty)
     * and is not resolved by any other argument resolver is treated as if it were annotated with @ModelAttribute.
     * @param account
     * @param bindingResult
     * @return
     */
    @PutMapping("/accounts/{account}")
    public String save(@ModelAttribute("account") Account account, BindingResult bindingResult) {
        // ...

        return account.toString();
    }

    @PostMapping("/form")
    public String handleFormUpload(@RequestParam("name") String name,
                                   @RequestParam("file") MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            // store the bytes somewhere
            return "redirect:uploadSuccess";
        }
        return "redirect:uploadFailure";
    }

    /**
     * You can use the @RequestBody annotation to have the request body read
     * and deserialized into an Object through an HttpMessageConverter
     * @param account
     */
    @PostMapping("/accounts")
    public void handle(@RequestBody Account account) {
        // ...
    }

    /**
     *  use @RequestBody in combination with javax.validation.
     *  Valid or Spring’s @Validated annotation,both of which cause Standard Bean Validation to be applied.
     *  By default, validation errors cause a MethodArgumentNotValidException,
     *  which is turned into a 400 (BAD_REQUEST) response.Alternatively, you can handle validation errors
     *  locally within the controller through an Errors or BindingResult argument
     * @param account
     * @param result
     */
    @PostMapping("/accounts")
    public void handle(@Valid @RequestBody Account account, BindingResult result) {
        // ...
    }

    /**
     * HttpEntity is more or less identical to using @RequestBody
     * but is based on a container object that exposes request headers and body.
     * @param entity
     */
    @PostMapping("/accounts")
    public void handle(HttpEntity<Account> entity) {
        // ...
    }

    /**
     *  use the @ResponseBody annotation on a method to have the return
     *  serialized to the response body through an HttpMessageConverter.
     * @return
     */
    @GetMapping("/accounts/{id}")
    @ResponseBody
    public Account handle() {
        // ...
        return new Account();
    }

    /**
     * ResponseEntity is like @ResponseBody but with status and headers.
     * @return
     */
    @GetMapping("/something")
    public ResponseEntity<String> handleResponseEntity() {
        String body = "..." ;
        String etag = "..." ;
        return ResponseEntity.ok().eTag(etag).body(body);
    }



    public static class Account{}
}
