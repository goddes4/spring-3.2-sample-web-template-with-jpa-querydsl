package net.octacomm.sample.dao;

import net.octacomm.sample.domain.User;

public interface UserRepository extends DefaultRepository<User, String> {

	User findByIdAndName(User user);

}
