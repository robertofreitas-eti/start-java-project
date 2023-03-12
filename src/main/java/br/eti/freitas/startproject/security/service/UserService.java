package br.eti.freitas.startproject.security.service;

import java.util.List;

import br.eti.freitas.startproject.security.model.User;

public interface UserService {

	User getUser(String username);

	List<User> getUsers();

	User getById(long id);

	User createUser(User user);

	void updateUser(long id, User userDetails);

	void deleteById(long id);

	void addPrivilegeToUser(String userName, String privilegeName);

}
