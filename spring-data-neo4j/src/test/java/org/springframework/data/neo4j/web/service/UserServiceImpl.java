/*
 * Copyright (c)  [2011-2016] "Pivotal Software, Inc." / "Neo Technology" / "Graph Aware Ltd."
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with
 * separate copyright notices and license terms. Your use of the source
 * code for these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 *
 */

package org.springframework.data.neo4j.web.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.web.domain.User;
import org.springframework.data.neo4j.web.repo.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author Michal Bachman
 * @author Mark Angrish
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User getUserByUuid(UUID uuid) {
		return  userRepository.findOne(uuid);
	}

	@Override
	public Collection<User> getNetwork(User user) {
		Set<User> network = new TreeSet<>(new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				return u1.getName().compareTo(u2.getName());
			}
		});
		buildNetwork(user, network);
		network.remove(user);
		return network;
	}

	private void buildNetwork(User user, Set<User> network) {
		for (User friend : user.getFriends()) {
			if (!network.contains(friend)) {
				network.add(friend);
				buildNetwork(friend, network);
			}
		}
	}
}
