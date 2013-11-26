package net.octacomm.sample.service;

import net.octacomm.sample.domain.User;
import net.octacomm.sample.exceptions.InvalidPasswordException;
import net.octacomm.sample.exceptions.NotFoundUserException;

public interface LoginService {

	boolean login(User user) throws NotFoundUserException, InvalidPasswordException;
}
