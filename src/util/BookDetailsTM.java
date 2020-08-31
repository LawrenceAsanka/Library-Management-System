package util;

public class BookDetailsTM {

    private String id;
    private String isbn;
    private String name;
    private String author;
    private Double price;
    private String availability;

    public BookDetailsTM() {

    }

    public BookDetailsTM(String id, String isbn, String name, String author, Double price, String availability) {
        this.id = id;
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.price = price;
        this.availability = availability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "BookDetailsTM{" +
                "id='" + id + '\'' +
                ", isbn='" + isbn + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", availability='" + availability + '\'' +
                '}';
    }
}
