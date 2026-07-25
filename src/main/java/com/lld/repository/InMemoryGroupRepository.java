package com.lld.repository;

import com.lld.enitity.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryGroupRepository implements GroupRepository {
    private final Map<String, Group> groups;

    public InMemoryGroupRepository() {
        this.groups = new ConcurrentHashMap<>();
    }

    @Override
    public void addGroup(Group group) {
        if (group == null || group.getGroupId() == null) {
            throw new IllegalArgumentException("Group and groupId cannot be null");
        }
        groups.put(group.getGroupId(), group);
    }

    @Override
    public Optional<Group> getGroupById(String groupId) {
        return Optional.ofNullable(groups.get(groupId));
    }

    @Override
    public List<Group> getAllGroups() {
        return new ArrayList<>(groups.values());
    }
}
