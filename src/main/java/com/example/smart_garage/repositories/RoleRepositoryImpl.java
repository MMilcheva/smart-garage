package com.example.smart_garage.repositories;

import com.example.smart_garage.models.Role;
import com.example.smart_garage.repositories.contracts.RoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl extends AbstractCRUDRepository<Role> implements RoleRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        super(Role.class, sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Role> getAllRoles(Optional<String> search) {
        if (search.isEmpty()) {
            return getAll();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Role> list = session.createQuery(" from Role where " +
                    "roleName like :roleName ");
            list.setParameter("roleName", "%" + search.get() + "%");
//TODO maybe something should be fixed additionally
            return list.list();
        }
    }

    @Override
    public List<Role> get() {
        try (Session session = sessionFactory.openSession()) {
            Query<Role> query = session.createQuery("from Role", Role.class);
            return query.list();
        }
    }
}
