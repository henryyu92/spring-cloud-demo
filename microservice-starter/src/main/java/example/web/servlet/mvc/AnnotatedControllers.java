package example.web.servlet.mvc;

import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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


    public static class Account{}
}
