package pizza.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pizza.model.RegistrationForm;
import pizza.repositories.jpa.JpaUserRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/register")
public class RegistrationController {


    private final JpaUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(JpaUserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(@ModelAttribute("form") @Valid RegistrationForm form, Errors errors,
                                      HttpServletRequest request) {
        if (errors.hasErrors()) {
            return "/registration";
        }
        userRepo.save(form.toUser(passwordEncoder));
        autoLoginUser(form, request);
        return "redirect:/";
    }

    //auto login after registration
    private void autoLoginUser(RegistrationForm form, HttpServletRequest request) {
        String username = form.getUsername();
        String password = form.getPassword();
        try {
            request.login(username, password);
        }catch (ServletException e) {
            log.error("Error while logging");
        }
    }

    @ModelAttribute(name = "form")
    public RegistrationForm getRegForm() {
        return new RegistrationForm();
    }
}
