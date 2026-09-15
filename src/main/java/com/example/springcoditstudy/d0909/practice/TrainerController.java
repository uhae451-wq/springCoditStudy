package com.example.springcoditstudy.d0909.practice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping
    public String postTrainer(@RequestParam(value = "email",required = false,defaultValue = "asd")String email,
                              @RequestParam(value = "name",required = false)String name,
                              @RequestParam(value = "region",required = false)String region,
                              Model model)
    {
/*        model.addAttribute("email",email);
        model.addAttribute("name",name);
        model.addAttribute("region",region);*/
        Trainer trainer = trainerService.createTrainer(name,email,region);
        model.addAttribute("trainer",trainer.getId());
        model.addAttribute("email",trainer.getEmail());
        model.addAttribute("name",trainer.getName());
        model.addAttribute("region",trainer.getRegion());

        return "test";
    }

    @GetMapping("{trainer-id}")
    public String getTrainer(@PathVariable("trainer-id") long trainerId,Model model){
        model.addAttribute("trianer",trainerService.getTrainer(trainerId));
        return "test";
    }

    @GetMapping
    public String getTrainers(Model model){
        List<Trainer> allTrainers = trainerService.getAllTrainer();
        model.addAttribute("trainers",allTrainers);
        return "test";
    }
}
