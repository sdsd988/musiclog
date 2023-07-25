package com.musiclog.repository;

import com.musiclog.domain.Session;
import com.musiclog.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session,Long> {

}
