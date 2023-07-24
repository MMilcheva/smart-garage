package com.example.smart_garage.repositories;

import com.example.smart_garage.exceptions.EntityNotFoundException;
import com.example.smart_garage.models.UserFilterOptions;
import com.example.smart_garage.models.User;
import com.example.smart_garage.repositories.contracts.DiscountRepository;
import com.example.smart_garage.repositories.contracts.UserRepository;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Repository
public class UserRepositoryImpl extends AbstractCRUDRepository<User> implements UserRepository {
    private final DiscountRepository discountRepository;

    private final JavaMailSender javaMailSender;


    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory, DiscountRepository discountRepository, JavaMailSender javaMailSender) {
        super(User.class, sessionFactory);
        this.discountRepository = discountRepository;
        this.javaMailSender = javaMailSender;
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<User> getAll(Optional<String> search) {

        if (search.isEmpty()) {
            return getAllUsers();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery(" from User where " +
                    "firstName like :firstName or email like :email or username like :username");
            query.setParameter("firstName", "%" + search.get() + "%");
            query.setParameter("email", "%" + search.get() + "%");
            query.setParameter("username", "%" + search.get() + "%");
            return query.list();
        }
    }

    private List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> list = session.createQuery(" from User ", User.class);
            return list.list();
        }
    }
    @Override
    public User getById(long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.save(user);
        }

    }
    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    @Transactional
    public void updatePassword(Long userId, String newPassword) {
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, userId);
        user.setPassword(newPassword);
        session.update(user);
    }


    @Override
    public void delete(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(getById(id));
            session.getTransaction().commit();
        }
    }

    @Override
    public int getNumberOfUsers() {
        try (Session session = sessionFactory.openSession()) {
            Query<Object> query = session.createQuery("select count(*) from User", Object.class);

            return query.list().get(0) == null ? 0 : query.list().stream()
                    .mapToInt((o -> Integer.parseInt("" + o)))
                    .boxed().toList().get(0);
        }
    }

    @Override
    public User getByName(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "username", username);
            }

            return result.get(0);
        }
    }

    @Override
    public User getUserByFirstName(String firstName) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where firstName = :firstname", User.class);
            query.setParameter("firstname", firstName);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "firstname", firstName);
            }
            return result.get(0);
        }
    }

    @Override
    public User getUserByLastName(String lastName) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where lastName = :lastName", User.class);
            query.setParameter("lastName", lastName);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "lastName", lastName);
            }
            return result.get(0);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "email", email);
            }

            return result.get(0);
        }
    }

    @Override
    public User getUserByPhoneNUmber(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where phoneNumber = :phoneNumber", User.class);
            query.setParameter("phoneNumber", phoneNumber);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "phone_number", phoneNumber);
            }

            return result.get(0);
        }
    }

    @Override
    public List<User> getBlockedUsers() {

        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where isArchived = :true", User.class);
            query.setParameter("isArchived", true);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("User", "blocked users", "no blocked users found");
            }

            return result;
        }
    }

    @Override
    public void block(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }

    }

    @Override
    public void unblock(User userToUnBLock) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(userToUnBLock);
            session.getTransaction().commit();
        }
    }

    @Override
    public void activate(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deActivate(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> filterAllUsers(UserFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getFirstName().ifPresent(value -> {
                filters.add("u.firstName like :firstName");
                params.put("firstName", String.format("%%%s%%", value));
            });
            filterOptions.getLastName().ifPresent(value -> {
                filters.add("u.lastName like :lastName");
                params.put("lastName", String.format("%%%s%%", value));
            });
            filterOptions.getEmail().ifPresent(value -> {
                filters.add("u.email like :email");
                params.put("email", String.format("%%%s%%", value));
            });
            filterOptions.getUsername().ifPresent(value -> {
                filters.add("u.username like :username");
                params.put("username", String.format("%%%s%%", value));
            });

            filterOptions.getPhoneNumber().ifPresent(value -> {
                filters.add("u.phoneNumber like :phoneNumber");
                params.put("phoneNumber", String.format("%%%s%%", value));
            });

            filterOptions.getMinDate().ifPresent(value -> {
               // LocalDate minDate = convertStringToLocalDate(value);
                    filters.add("vs.startDateOfVisit >= :minDate");
                    params.put("minDate", value);
            });

            filterOptions.getMaxDate().ifPresent(value -> {
                filters.add("vs.startDateOfVisit <= :maxDate");
                params.put("maxDate", value);
            });

            filterOptions.getVehicle().ifPresent(value -> {
                filters.add("v.plate = :plate");
                params.put("plate", String.format("%%%s%%", value));
            });


            filterOptions.getRoleDescription().ifPresent(value -> {
                filters.add("u.role.roleName like :roleName");
                params.put("roleName", String.format("%%%s%%", value));
            });

            filterOptions.getVisitId().ifPresent(value -> {
                filters.add("vs.visitId = :visitId");
                params.put("visitId", value);
            });

            StringBuilder queryString = new StringBuilder("SELECT DISTINCT u FROM User u ");
            queryString.append("JOIN u.vehicles v ");
            queryString.append("JOIN v.visits vs ");
            if (!filters.isEmpty()) {
                queryString
                        .append("WHERE ")
                        .append(String.join(" AND ", filters));
                if (filters.contains("minDate") && filters.contains("maxDate")) {
                    queryString.append(" AND vs.startDateOfVisit >= :minDate AND vs.startDateOfVisit <= :maxDate");
                }
                if (filters.contains("plate")) {
                    queryString.append(" AND v.plate = :plate");
                }
            }

            queryString.append(generateUserOrderBy(filterOptions));

            TypedQuery<User> query = session.createQuery(queryString.toString(), User.class);

            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            return query.getResultList();

        }
    }


    private String generateUserOrderBy(UserFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "username":
                orderBy = "username";
                break;
            case "email":
                orderBy = "email";
                break;
            case "phone_number":
                orderBy = "phone_number";
                break;
            case "description":
                orderBy = "description";
                break;
            case "start_date_of_visit":
                orderBy = "start_date_of_visit";
                break;

            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().
                equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }

    private LocalDate convertStringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }
}
