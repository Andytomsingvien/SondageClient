package fr.simplon.sondageclient.controller; /**

 Le contrôleur SondageController gère les requêtes HTTP liées aux sondages.
 */
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

import java.util.List;

@Controller
public class SondageController {
    private RestTemplate restTemplate;

    /**
     * Affiche la page d'accueil contenant la liste de tous les sondages.
     *
     * @param model le modèle utilisé pour stocker les sondages à afficher
     * @return la vue index affichant la liste des sondages
     */
    @GetMapping("/")
    public String index(Model model){
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8080/rest/sondages";
        ResponseEntity<List<Sondage>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Sondage>>() {});
        List<Sondage> sondages = response.getBody();

        model.addAttribute("sondages", sondages);
        return "index";
    }

    /**
     * Affiche les détails d'un sondage donné.
     *
     * @param model le modèle utilisé pour stocker les détails du sondage à afficher
     * @param id l'identifiant du sondage à afficher
     * @return la vue sondage affichant les détails du sondage
     */
    @GetMapping("/sondages/{id}")
    public String getSondage(Model model, @PathVariable long id){
        this.restTemplate = new RestTemplate();
        String url = "http://localhost:8080/rest/sondages/{id}";
        ResponseEntity<Sondage> response = restTemplate.getForEntity(url, Sondage.class, id);
        Sondage sondage = response.getBody();

        model.addAttribute("sondage", sondage);

        return "sondage";
    }

    /**
     * Affiche le formulaire pour ajouter un nouveau sondage.
     *
     * @param model le modèle utilisé pour stocker le nouveau sondage à ajouter
     * @return la vue form pour ajouter un nouveau sondage
     */
    @GetMapping("/sondages/form/add")
    public String formSondage(Model model)
    {
        Sondage sondage = new Sondage();
        model.addAttribute("sondage", sondage);
        return "form";
    }

    /**
     * Affiche le formulaire pour modifier un sondage donné.
     *
     * @param model le modèle utilisé pour stocker le sondage à modifier
     * @param id l'identifiant du sondage à modifier
     * @return la vue edit pour modifier le sondage
     */
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
    /**
     * Supprime un sondage donné.
     *
     * @param model le modèle utilisé pour stocker les informations de redirection après suppression du sondage
     * @param id l'identifiant du sondage à supprimer
     * @return la vue index redirigeant vers la liste des sondages
     */
    @PostMapping("/sondages/del/{id}")
    public String delSondage(Model model, @PathVariable long id)
    {
        this.restTemplate = new RestTemplate();
    String url = "http://localhost:8080/rest/sondages/{id}";
    restTemplate.delete(url, id);

    return "redirect:/";
}

    /**
     * Modifie les informations d'un sondage donné.
     *
     * @param sondage le sondage modifié
     * @param id l'identifiant du sondage à modifier
     * @return la vue index redirigeant vers la liste des sondages
     */
    @PostMapping("/sondages/maj/{id}")
    public String updateFruit(@ModelAttribute("sondage") Sondage sondage, @PathVariable long id){

        String url = "http://localhost:8080/rest/sondages/{id}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Sondage> request = new HttpEntity<>(sondage, headers);
        ResponseEntity<Sondage> response = restTemplate.exchange(url, HttpMethod.PUT, request, Sondage.class, id);

        return "redirect:/";
    }

    /**
     * Ajoute un nouveau sondage.
     *
     * @param sondage le nouveau sondage à ajouter
     * @return la vue index redirigeant vers la liste des sondages
     */
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