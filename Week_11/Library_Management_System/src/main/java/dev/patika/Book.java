package dev.patika;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id",columnDefinition = "serial")
    private long id;

    @Column(name = "book_name",nullable = false)
    private String name;

    @Column(name = "book_publication_year",nullable = false)
    private int publicationYear;

    @Column(name = "book_stock",nullable = false)
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_publisher_id", referencedColumnName = "publisher_id")
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_author_id", referencedColumnName = "author_id")
    private Author author;

    @OneToMany(mappedBy = "borrowingBook",cascade = CascadeType.REMOVE)
    private List<BookBorrowing> bookBorrowings;


    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "pro2category",
            joinColumns = {@JoinColumn(name = "pro2category_book_id")},
            inverseJoinColumns = {@JoinColumn(name = "pro2category_category_id")}
    )
    private List<Category> categoryList;

    public Book() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }


    public List<BookBorrowing> getBookBorrowings() {
        return bookBorrowings;
    }

    public void setBookBorrowings(List<BookBorrowing> bookBorrowings) {
        this.bookBorrowings = bookBorrowings;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
