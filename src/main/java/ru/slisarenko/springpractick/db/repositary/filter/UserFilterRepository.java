package ru.slisarenko.springpractick.db.repositary.filter;

import ru.slisarenko.springpractick.db.entity.User;
import ru.slisarenko.springpractick.dto.PersonalInfo;
import ru.slisarenko.springpractick.dto.UserFilterParams;

import java.util.List;

public interface UserFilterRepository {

    List<User> filterUserSearch(UserFilterParams filter);

    List<PersonalInfo> searchUsersByCompanyAndRole(UserFilterParams filter);
}
