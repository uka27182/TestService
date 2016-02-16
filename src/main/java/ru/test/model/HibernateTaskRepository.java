package ru.test.model;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class HibernateTaskRepository implements  ITaskRepository{
    @Autowired(required=true)
    @Qualifier(value= "hibernate5AnnotatedSessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public Task getByUuid(String uuid) {
        Session session = this.sessionFactory.getCurrentSession();
        return session.get(Task.class, uuid);
    }

    @Override
    public Task persist(Task task) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(task);
//        session.save(task);
        return task;
    }

    @Override
    public Long getCount() {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Task.class).setProjection(Projections.rowCount());
        return (Long)criteria.uniqueResult();
    }

    @Override
    public List<Task> getAllTask() {
        Session session = this.sessionFactory.getCurrentSession();
        return session.createCriteria(Task.class).list();
    }

    @Override
    public int deleteAllTask() {
        return sessionFactory.getCurrentSession().createQuery("delete from Task").executeUpdate();
    }

    @Override
    public void saveOrUpdate(Task task) {
        Session session = this.sessionFactory.getCurrentSession();
        session.saveOrUpdate(task);
    }
}
