package com.lld.service;

import com.lld.repository.GroupRepository;
import com.lld.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SplitWiseService {
    private final UserRepository userRepository;
    private final GroupRepository groupService;
    private SplitStrategy splitStrategy;
    //this will cater all query
}
