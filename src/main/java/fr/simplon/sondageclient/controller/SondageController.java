package fr.simplon.sondageclient.controller;

import fr.simplon.sondageclient.entity.Sondage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;

@Controller
public class SondageController {

    private RestTemplate restTemplate;

    @GetMapping("/")
    public String index(Model model){
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8080/rest/sondages";
        ResponseEntity<List<Sondage>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Sondage>>() {});
        List<Sondage> sondages = response.getBody();

        model.addAttribute("sondages", sondages);
        return "index";
    }

    @GetMapping("/sondages/{id}")
    public String getSondage(Model model, @PathVariable long id){
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8080/rest/sondages/{id}";
        ResponseEntity<Sondage> response = restTemplate.getForEntity(url, Sondage.class, id);
        Sondage sondage = response.getBody();

        model.addAttribute("sondage", sondage);

        return "sondage";
    }

    @GetMapping("/sondages/form/add")
    public String formSondage(Model model)
    {
        Sondage sondage = new Sondage();
        model.addAttribute("sondage", sondage);
        return "form";
    }

    @GetMapping("/sondages/maj/{id}")
    public String majSondage(Model model, @PathVariable long id)
    {
            this.restTemplate = new RestTemplate();
        String url = "http://localhost:8080/rest/sondages/{id}";
        ResponseEntity<Sondage> response = restTemplate.getForEntity(url, Sondage.class, id);
        Sondage sondage = response.getBody();

        model.addAttribute("sondage", sondage);
        return "edit";
    }



    @PostMapping("/sondages/del/{id}")
    public String delSondage(Model model, @PathVariable long id)
    {
        this.restTemplate = new RestTemplate();

        String url = "http://localhost:8080/rest/sondages/{id}";
        restTemplate.delete(url, id);

        return "redirect:/";
    }

    @PostMapping("/sondages/maj/{id}")
    public String updateFruit(@ModelAttribute("sondage") Sondage sondage, @PathVariable long id){

        String url = "http://localhost:8080/rest/sondages/{id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sondage> request = new HttpEntity<>(sondage, headers);
        ResponseEntity<Sondage> response = restTemplate.exchange(url, HttpMethod.PUT, request, Sondage.class, id);

        return "redirect:/";
    }

    @PostMapping("/sondages/form/add")
    public String addSondage(@ModelAttribute("sondage") Sondage sondage){
        this.restTemplate = new RestTemplate();

        String url = "http://localhost:8080/rest/sondages";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sondage> request = new HttpEntity<>(sondage, headers);
        ResponseEntity<Sondage> response = restTemplate.postForEntity(url, request, Sondage.class);

        return "redirect:/";
    }

}
