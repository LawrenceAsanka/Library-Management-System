package util;

import java.util.Date;

public class IssueDetailsTM {
    private String issue_id;
    private Date issue_date;
    private String member_id;
    private String book_id;
    private String book_name;
    private long kept_dates;

    public IssueDetailsTM() {
    }


    public IssueDetailsTM(String issue_id, Date issue_date, String member_id, String book_id, String book_name, long kept_dates) {
        this.issue_id = issue_id;
        this.issue_date = issue_date;
        this.member_id = member_id;
        this.book_id = book_id;
        this.book_name = book_name;
        this.kept_dates = kept_dates;
    }

    public String getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(String issue_id) {
        this.issue_id = issue_id;
    }

    public Date getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(Date issue_date) {
        this.issue_date = issue_date;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public long getKept_dates() {
        return kept_dates;
    }

    public void setKept_dates(long kept_dates) {
        this.kept_dates = kept_dates;
    }

    @Override
    public String toString() {
        return "IssueDetails{" +
                "issue_id='" + issue_id + '\'' +
                ", issue_date=" + issue_date +
                ", member_id='" + member_id + '\'' +
                ", book_id='" + book_id + '\'' +
                ", book_name='" + book_name + '\'' +
                ", kept_dates=" + kept_dates +
                '}';
    }
}
