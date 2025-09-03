package com.dsi.inventairecds.controller;

import com.dsi.inventairecds.model.Equipement;
import com.dsi.inventairecds.repository.EquipementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller // contrôleur Spring MVC
@RequestMapping("/")
public class EquipementController {

    @Autowired
    private EquipementRepository equipementRepository;

    @GetMapping
    // Ajoute la liste au modèle (model.addAttribute) pour être affichée dans la vue equipements.html
    public String listEquipements(Model model) {
        model.addAttribute("equipements", equipementRepository.findAll());
        return "equipements";
    }

    @GetMapping("/add")
    // Crée un objet vide Equipement pour remplir le formulaire.
    //Retourne la vue add-equipement.html.
    public String addEquipementForm(Model model) {
        model.addAttribute("equipement", new Equipement());
        return "add-equipement";
    }

    @PostMapping("/add")
    // @ModelAttribute récupère automatiquement les données du formulaire et les convertit en objet Equipement
    public String addEquipementSubmit(@ModelAttribute Equipement equipement) {
        equipementRepository.save(equipement);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
        //@PathVariable récupère l’id depuis l’URL.
        //Cherche l’équipement correspondant dans la base.
        //Si trouvé → l’ajoute au modèle pour préremplir le formulaire.
        //Retourne la vue edit-equipement.html.
    public String editEquipementForm(@PathVariable("id") Long id, Model model) {
        Equipement equipement = equipementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid equipment Id:" + id));
        model.addAttribute("equipement", equipement);
        return "edit-equipement";
    }

    @PostMapping("/edit/{id}")
    //Associe les nouvelles données au bon id (on force avec equipement.setId(id)).
    //Sauvegarde la modification.
    //Redirection vers la liste.
    public String editEquipementSubmit(@PathVariable("id") Long id, @ModelAttribute Equipement equipement) {
        equipement.setId(id); // Assurez-vous que l'ID est bien défini
        equipementRepository.save(equipement);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    //Supprime l’équipement par son id.
    //Redirection vers la liste.
    public String deleteEquipement(@PathVariable("id") Long id) {
        equipementRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Equipement> tousLesEquipements = equipementRepository.findAll();

        // 1. Calcul du nombre total par type
        Map<String, Long> countByType = tousLesEquipements.stream()
                .collect(Collectors.groupingBy(Equipement::getType, Collectors.counting()));
        model.addAttribute("countByType", countByType);

        // 2. Calcul du pourcentage de PCs migrés
        long totalPCs = tousLesEquipements.stream()
                .filter(e -> "PC".equalsIgnoreCase(e.getType()))
                .count();
        long pcsMigres = tousLesEquipements.stream()
                .filter(e -> "PC".equalsIgnoreCase(e.getType()) && "Migré Windows 11".equalsIgnoreCase(e.getEtatActuel()))
                .count();
        double pourcentageMigration = (totalPCs > 0) ? ((double) pcsMigres / totalPCs) * 100 : 0;
        model.addAttribute("pourcentageMigration", String.format("%.2f", pourcentageMigration));
        model.addAttribute("totalPCs", totalPCs);
        model.addAttribute("pcsMigres", pcsMigres);

        // 3. Liste des équipements en panne
        List<Equipement> equipementsEnPanne = tousLesEquipements.stream()
                .filter(e -> "En panne".equalsIgnoreCase(e.getEtatActuel()))
                .collect(Collectors.toList());
        model.addAttribute("equipementsEnPanne", equipementsEnPanne);

        return "dashboard";
    }
}
