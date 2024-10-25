package com.example.timetracker.api.security.service;

import com.example.timetracker.api.dto.UserDtoUpdate;
import com.example.timetracker.api.exception.NotFoundException;
import com.example.timetracker.api.exception.SameUserInDataBase;
import com.example.timetracker.api.exception.UserHasProjectsException;
import com.example.timetracker.api.exception.UserReqEmailException;
import com.example.timetracker.api.security.entity.UserSecurity;
import com.example.timetracker.api.dto.UserSecurityDto;
import com.example.timetracker.api.security.entity.dtoFactories.UserSecurityDtoFactory;
import com.example.timetracker.api.security.repository.UserSecurityRepository;
import com.example.timetracker.store.entity.Project;
import com.example.timetracker.store.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserSecurityService {
    private final UserSecurityRepository userSecurityRepository;
    private final UserSecurityDtoFactory userSecurityDtoFactory;
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;

    @Autowired
    public UserSecurityService(UserSecurityRepository userSecurityRepository,
                               UserSecurityDtoFactory userSecurityDtoFactory,
                               PasswordEncoder passwordEncoder,
                               ProjectRepository projectRepository) {
        this.userSecurityRepository = userSecurityRepository;
        this.userSecurityDtoFactory = userSecurityDtoFactory;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
    }

    public List<UserSecurityDto> getAllUsers() {
        return userSecurityRepository.findAll()
                .stream()
                .map(userSecurityDtoFactory::makeUserSecurityFactory)
                .collect(Collectors.toList());
    }

    public Optional<UserSecurityDto> getInfoAboutCurrentUser(String login) {
        Optional<UserSecurity> userSecurityOptional = userSecurityRepository.findByLogin(login);

        if (userSecurityOptional.isEmpty()) {
            return Optional.empty();
        }
        UserSecurity userSecurity = userSecurityOptional.get();

        return Optional.of(userSecurityDtoFactory.makeUserSecurityFactory(userSecurity));
    }

    public UserSecurity getUserById(Long user_id) {
        return getUserOrThrowException(user_id);
    }

    private UserSecurity getUserOrThrowException(Long id) {
        return userSecurityRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User with id ->" + id + " doesn't exist"));
    }

    public UserSecurity createUser(UserSecurity user) {
        if (userSecurityRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new SameUserInDataBase("User with login " + user.getLogin() + " already exists");
        }
        // код для шифрования пароля и сохранения пользователя
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userSecurityRepository.save(user);
    }


    public boolean updateUser(UserDtoUpdate userDtoUpdate) {
        UserSecurity userSecurityFromDB = userSecurityRepository.findById(userDtoUpdate.getId())
                .orElseThrow(() -> new NotFoundException("User with ID ->" + userDtoUpdate.getId() + "doesn't exist"));

        boolean updated = false;

        if (userDtoUpdate.getLogin() != null && !userDtoUpdate.getLogin().isEmpty()) {
            userSecurityFromDB.setLogin(userDtoUpdate.getLogin());
            updated = true;
        }

        if (userDtoUpdate.getPassword() != null && !userDtoUpdate.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userDtoUpdate.getPassword());
            userSecurityFromDB.setPassword(encodedPassword);
            updated = true;
        }

        if (updated) {
            userSecurityRepository.saveAndFlush(userSecurityFromDB);
        }

        return updated;
    }

    public void deleteUser(Long id) {
        Optional<UserSecurity> user = userSecurityRepository.findById(id);

        if (user.isEmpty()) {
            throw new NotFoundException("User with ID " + id + " not found.");
        }

        List<Project> userProjects = projectRepository.findByUserSecurityId(id);
        if (!userProjects.isEmpty()) {
            throw new UserHasProjectsException("Unable to delete the user because they have associated projects.");
        }
        userSecurityRepository.deleteById(id);
    }
}
