package com.thiagosena.currencyconverter.repository;

import com.thiagosena.currencyconverter.model.Transaction;
import com.thiagosena.currencyconverter.model.User;
import io.quarkus.test.junit.QuarkusTest;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class UserRepositoryTest {

	private User originalUser;

	@BeforeEach
	void setUp() {
		originalUser = new User("Gohan Filho de Goku");
	}

	@Test
	@Transactional
	void whenFindUserById_ThenReturnOne() {
		originalUser.persistAndFlush();

		User persistedUser = User.getById(originalUser.id);

		assertNotNull(persistedUser);
		assertEquals(originalUser.id, persistedUser.id);
		assertEquals(originalUser.name, persistedUser.name);
	}

	@Test
	@Transactional
	void whenUserPersistsNameFieldWithNull_ThenReturnFail() {
		originalUser.name = null;
		PersistenceException ex = Assertions.assertThrows(PersistenceException.class, () -> originalUser.persistAndFlush());
		assertNotNull(ex);
		assertTrue(ex.getCause() instanceof ConstraintViolationException);
	}

	@Test
	@Transactional
	void whenUserPersistsNameFieldWithBlank_ThenReturnFail() {
		originalUser.name = "";
		PersistenceException ex = Assertions.assertThrows(PersistenceException.class, () -> originalUser.persistAndFlush());
		assertNotNull(ex);
		assertTrue(ex.getCause() instanceof ConstraintViolationException);
	}

	@Test
	@Transactional
	void whenListAllUsers_ThenReturnAll() {
		Transaction.deleteAll();
		User.deleteAll();
		originalUser.persist();
		List<User> users = User.getAll();
		assertEquals(1, users.size());
	}
}
