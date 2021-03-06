package com.parrit.configurations;

import com.parrit.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ProjectDetailsService implements UserDetailsService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectDetailsService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return projectRepository.findByName(name)
                .map(project -> new User(project.getName(), project.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("Keeaa!? That’s not your project name."));
    }
}
