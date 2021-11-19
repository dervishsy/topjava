package ru.javawebinar.topjava.service;

import org.junit.Test;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;

public abstract class AbstractDataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void createWithException() {
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "  ", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())));
    }
}