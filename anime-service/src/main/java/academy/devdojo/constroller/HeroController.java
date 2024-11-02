package academy.devdojo.constroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/heroes")
public class HeroController {
    private static final List<String> HEROES = List.of("Guts", "Zoro", "Kakashi","Goku");

    @GetMapping
    public List<String> listAllHeroes() {
        return HEROES;
    }

    @GetMapping("filter")
    public List<String> listAllHeroesParam(@RequestParam(required = false) String name) {
        return HEROES.stream().filter(hero -> hero.equalsIgnoreCase(name)).toList();
    }

    @GetMapping("filterList")
    public List<String> listAllHeroesParamList(@RequestParam(defaultValue = "") List<String> names) {
        return HEROES.stream().filter(names::contains).toList();
    }

    @GetMapping("{name}")
    public String findByName(@PathVariable String name) {
        return HEROES
                .stream()
                .filter(hero -> hero.equalsIgnoreCase(name))
                .findFirst().orElse("");
    }

    @GetMapping("{name}/animes/{animeId}")
    public String findByNameAndAnime(@PathVariable String name, @PathVariable(name = "animeId") int id) {
        return HEROES
                .stream()
                .filter(hero -> hero.equalsIgnoreCase(name))
                .findFirst().orElse("");
    }
}
