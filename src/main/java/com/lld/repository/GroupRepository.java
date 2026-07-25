package com.lld.repository;

import com.lld.enitity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupRepository {
    void addGroup(Group group);
    Optional<Group> getGroupById(String groupId);
    List<Group> getAllGroups();
}
