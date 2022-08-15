package models;

import java.util.List;

public class Course {

    private String section;
    private List<Book> requiredBooks;
    private List<Book> optionalBooks;

    public Course(String section, List<Book> requiredBooks, List<Book> optionalBooks) {
        this.section = section;
        this.requiredBooks =  requiredBooks;
        this.optionalBooks =  optionalBooks;
    }

}
