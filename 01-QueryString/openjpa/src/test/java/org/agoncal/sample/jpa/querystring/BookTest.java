package org.agoncal.sample.jpa.querystring;


import org.junit.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         https://github.com/agoncal
 *         --
 */
public class BookTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static EntityTransaction tx;

    private TypedQuery<Book> findAllBooksNamedQuery;
    private TypedQuery<Book> findAllScifiBooksNamedQuery;
    private TypedQuery<Book> findAllBooksCriteriaQuery;
    private TypedQuery<Book> findAllScifiBooksCriteriaQuery;


    // ======================================
    // =          Lifecycle Methods         =
    // ======================================
    @BeforeClass
    public static void initEntityManager() throws Exception {
        emf = Persistence.createEntityManagerFactory("sampleJPAQueryStringPU");
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void closeEntityManager() throws SQLException {
        if (em != null) em.close();
        if (emf != null) emf.close();
    }

    @Before
    public void initTransaction() {
        tx = em.getTransaction();

        // NAMED QUERIES
        findAllBooksNamedQuery = em.createNamedQuery("findAllBooks", Book.class);
        findAllScifiBooksNamedQuery = em.createNamedQuery("findAllScifiBooks", Book.class);

        // CRITERIA QUERIES
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> q = cb.createQuery(Book.class);
        Root<Book> b = q.from(Book.class);

        // SELECT b FROM Book b ORDER BY b.id DESC
        q.select(b).orderBy(cb.desc(b.get("id")));
        findAllBooksCriteriaQuery = em.createQuery(q);
        // SELECT b FROM Book b WHERE 'scifi' member of b.tags ORDER BY b.id DESC
        Expression<List<String>> tags = b.get("tags");
        q.select(b).where(cb.isMember("scifi", tags)).orderBy(cb.desc(b.get("id")));
        findAllScifiBooksCriteriaQuery = em.createQuery(q);
    }

    // ======================================
    // =              Unit tests            =
    // ======================================

    @Test
    public void shouldFindScifiBooksWithNamedQueries() throws Exception {

        // Retrieves all the books from the database
        int nbOfBook = findAllBooksNamedQuery.getResultList().size();
        int nbOfScifiBook = findAllScifiBooksNamedQuery.getResultList().size();

        // Creates an instance of a scifi book
        Book scifiBook = new Book("H2G2", 12.5F, "Science fiction comedy book", "1-84023-742-2", 345, false, new String[]{"fun", "scifi"});

        // Persists the book to the database
        tx.begin();
        em.persist(scifiBook);
        tx.commit();

        // Retrieves all the books from the database and checks there's one extra
        assertEquals("Should have an extra book", nbOfBook + 1, findAllBooksNamedQuery.getResultList().size());
        assertEquals("Should have an extra book", nbOfScifiBook + 1, findAllScifiBooksNamedQuery.getResultList().size());

        // Creates an instance of book
        Book itBook = new Book("Java EE 6", 22.5F, "Great Java EE book", "1-64021-752-2", 474, true, new String[]{"java", "it"});

        // Persists the book to the database
        tx.begin();
        em.persist(itBook);
        tx.commit();

        // Retrieves all the books from the database and checks there's one extra
        assertEquals("Should have an extra book", nbOfBook + 2, findAllBooksNamedQuery.getResultList().size());
        assertEquals("Should have an extra book", nbOfScifiBook + 1, findAllScifiBooksNamedQuery.getResultList().size());

        // Removes all books
        tx.begin();
        em.remove(scifiBook);
        em.remove(itBook);
        tx.commit();

        // Retrieves all the books from the database and checks we have the initial number
        assertEquals("Should have an extra book", nbOfBook, findAllBooksNamedQuery.getResultList().size());
        assertEquals("Should have an extra book", nbOfScifiBook, findAllScifiBooksNamedQuery.getResultList().size());
    }

    @Test
    @Ignore("Doesnt work because the query is syntaxically incorrect 'SELECT b FROM Book b WHERE 'scifi'MEMBER OF b.tags ORDER BY b.id DESC', no space between scifi and member")
    public void shouldFindScifiBooksWithCriteriaQueries() throws Exception {

        // Retrieves all the books from the database
        int nbOfBook = findAllBooksCriteriaQuery.getResultList().size();
        int nbOfScifiBook = findAllScifiBooksCriteriaQuery.getResultList().size();

        // Creates an instance of a scifi book
        Book scifiBook = new Book("H2G2", 12.5F, "Science fiction comedy book", "1-84023-742-2", 345, false, new String[]{"fun", "scifi"});

        // Persists the book to the database
        tx.begin();
        em.persist(scifiBook);
        tx.commit();

        // Retrieves all the books from the database and checks there's one extra
        assertEquals("Should have an extra book", nbOfBook + 1, findAllBooksCriteriaQuery.getResultList().size());
        assertEquals("Should have an extra book", nbOfScifiBook + 1, findAllScifiBooksCriteriaQuery.getResultList().size());

        // Creates an instance of book
        Book itBook = new Book("Java EE 6", 22.5F, "Great Java EE book", "1-64021-752-2", 474, true, new String[]{"java", "it"});

        // Persists the book to the database
        tx.begin();
        em.persist(itBook);
        tx.commit();

        // Retrieves all the books from the database and checks there's one extra
        assertEquals("Should have an extra book", nbOfBook + 2, findAllBooksCriteriaQuery.getResultList().size());
        assertEquals("Should have an extra book", nbOfScifiBook + 1, findAllScifiBooksCriteriaQuery.getResultList().size());

        // Removes all books
        tx.begin();
        em.remove(scifiBook);
        em.remove(itBook);
        tx.commit();

        // Retrieves all the books from the database and checks we have the initial number
        assertEquals("Should have an extra book", nbOfBook, findAllBooksCriteriaQuery.getResultList().size());
        assertEquals("Should have an extra book", nbOfScifiBook, findAllScifiBooksCriteriaQuery.getResultList().size());
    }

    @Test
    public void shouldShowTheJPQLQuery() throws Exception {
        // NAMED QUERIES
        assertEquals("SELECT b FROM Book b ORDER BY b.id DESC", findAllBooksNamedQuery.unwrap(org.apache.openjpa.persistence.QueryImpl.class).getQueryString());
        assertEquals("SELECT b FROM Book b WHERE 'scifi' member of b.tags ORDER BY b.id DESC", findAllScifiBooksNamedQuery.unwrap(org.apache.openjpa.persistence.QueryImpl.class).getQueryString());

        // CRITERIA QUERIES
        assertEquals("SELECT b FROM Book b ORDER BY b.id DESC", findAllBooksCriteriaQuery.unwrap(org.apache.openjpa.persistence.QueryImpl.class).getQueryString());
        assertEquals("SELECT b FROM Book b WHERE 'scifi'MEMBER OF b.tags ORDER BY b.id DESC", findAllScifiBooksCriteriaQuery.unwrap(org.apache.openjpa.persistence.QueryImpl.class).getQueryString());
    }
}