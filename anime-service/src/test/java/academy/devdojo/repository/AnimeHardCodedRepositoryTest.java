package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodedRepositoryTest {

    @InjectMocks
    private AnimeHardCodedRepository respository;

    @Mock
    private AnimeData animeData;

    private List<Anime> animesList;

    @BeforeEach
    void init() {
        var fullMetal = Anime.builder().id(1L).name("Full Metal Brotherhood").build();
        var steinsGate = Anime.builder().id(2L).name("Steins Gate").build();
        var mashle = Anime.builder().id(3L).name("Mashle").build();
        animesList = new ArrayList<>(List.of(fullMetal, steinsGate, mashle));
    }

    @Test
    @DisplayName("findAll returns a list with all animes")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var animes = respository.findAll();
        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(animesList.size()).hasSameElementsAs(animesList);
    }

    @Test
    @DisplayName("findById returns an anime with given id")
    @Order(2)
    void findById_ReturnsAnimeById_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var expectedAnime = animesList.getFirst();
        var animes = respository.findById(expectedAnime.getId());
        Assertions.assertThat(animes).isPresent().contains(expectedAnime);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var animes = respository.findByName(null);
        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    @Order(4)
    void findByName_ReturnsFoundAnimeInList_WhenNameIsFound() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var expectedAnime = animesList.getFirst();
        var animes = respository.findByName(expectedAnime.getName());
        Assertions.assertThat(animes).hasSize(1).contains(expectedAnime);
    }

    @Test
    @DisplayName("save creates an anime")
    @Order(5)
    void save_CreatesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var animeToSave = Anime.builder().id(99L).name("Pokemon").build();
        var anime = respository.save(animeToSave);

        Assertions.assertThat(anime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();

        var animeSavedOptional = respository.findById(animeToSave.getId());

        Assertions.assertThat(animeSavedOptional).isPresent().contains(animeToSave);
    }

    @Test
    @DisplayName("delete removes an anime")
    @Order(6)
    void delete_RemoveAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);

        var animeToDelete = animesList.getFirst();
        respository.delete(animeToDelete);

        var animes = respository.findAll();

        Assertions.assertThat(animes).isNotEmpty().doesNotContain(animeToDelete);
    }

    @Test
    @DisplayName("update updates an anime")
    @Order(7)
    void update_UpdatesAnime_WhenSuccessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animesList);
        var animeToUpdate = this.animesList.getFirst();
        animeToUpdate.setName("Hellsing");

        respository.update(animeToUpdate);

        Assertions.assertThat(this.animesList).contains(animeToUpdate);

        var animeUpdatedOptional = respository.findById(animeToUpdate.getId());

        Assertions.assertThat(animeUpdatedOptional).isPresent();
        Assertions.assertThat(animeUpdatedOptional.get().getName()).isEqualTo(animeToUpdate.getName());
    }
}