package academy.devdojo.service;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserHardCodedRepository;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserHardCodedRepository repository;

    private List<User> userList;

    @InjectMocks
    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var users = service.findAll(null);
        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findAll returns list with found object when firstName exists")
    @Order(2)
    void findByName_ReturnsFoundUserInList_WhenFirstNameIsFound() {
        var user = userList.getFirst();
        var expectedUsersFound = singletonList(user);

        BDDMockito.when(repository.findByFirstName(user.getFirstName())).thenReturn(expectedUsersFound);

        var usersFound = service.findAll(user.getFirstName());
        Assertions.assertThat(usersFound).containsAll(expectedUsersFound);
    }

    @Test
    @DisplayName("findAll returns empty list when firstName is not found")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenFirstNameIsNotFound() {
        var firstName = "not-found";
        BDDMockito.when(repository.findByFirstName(firstName)).thenReturn(emptyList());

        var users = service.findAll(firstName);
        Assertions.assertThat(users).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns an user with given id")
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() {
        var expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

        var users = service.findByIdOrThrowNotFound(expectedUser.getId());

        Assertions.assertThat(users).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when user is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var expectedUser = userList.getFirst();
        BDDMockito.when(repository.findById(expectedUser.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedUser.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates an user")
    @Order(6)
    void save_CreatesUser_WhenSuccessful() {
        var userToSave = userUtils.newUserToSave();

        BDDMockito.when(repository.save(userToSave)).thenReturn(userToSave);

        var savedProducer = service.save(userToSave);

        Assertions.assertThat(savedProducer).isEqualTo(userToSave).hasNoNullFieldsOrProperties();
    }
}