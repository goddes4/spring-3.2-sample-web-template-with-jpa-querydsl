package net.octacomm.sample.service;

import net.octacomm.sample.dao.UserRepository;
import net.octacomm.sample.domain.User;
import net.octacomm.sample.exceptions.InvalidPasswordException;
import net.octacomm.sample.exceptions.NotFoundUserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public boolean login(User user) throws NotFoundUserException, InvalidPasswordException {
		if (userRepository.findOne(user.getId()) == null) {
			throw new NotFoundUserException();
		}
		
		if (userRepository.findByIdAndName(user) == null) {
			throw new InvalidPasswordException();
		}
		return true;
	}

}
