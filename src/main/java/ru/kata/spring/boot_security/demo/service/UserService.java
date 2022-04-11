package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    RoleRepository roleRepository;
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public void setBcryptPasswordEncoder( PasswordEncoder passwordEncoder) {
        this.bCryptPasswordEncoder = passwordEncoder;
    }

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;

    }

    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List <User> findAll(){
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
       return userRepository.findByUsername(username);
    }

    //Сохраняем ЮЗЕРА предварительно проверив уникальность username
    //Если username свободно добавляем ЮЗЕРУ  роль и закодируем пароль, после добавляем в базу
    public boolean saveUser (User user ) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if(userFromDB != null) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set <Role> roleSet = new HashSet<>();
        roleSet.add(roleRepository.getOne(2L));
        user.setRoles(roleSet);
        userRepository.save(user);
        return true;
    }

    public void delete(Long id) {
            userRepository.deleteById(id);
    }

    public void edit (User user, Long id) {
        User toBeEdit  = userRepository.findById(id).orElse(null);
        toBeEdit.setUsername(user.getUsername());
        toBeEdit.setPassword(user.getPassword());
        toBeEdit.setSurname(user.getSurname());
        toBeEdit.setEmail(user.getEmail());

    }

}
