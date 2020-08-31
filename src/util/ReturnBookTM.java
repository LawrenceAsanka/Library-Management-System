package util;

import java.util.Date;

public class ReturnBookTM {

    private Date returnDate;
    private int issueID;
    private double fineFee;

    public ReturnBookTM() {
    }

    public ReturnBookTM(Date returnDate, int issueID, double fineFee) {
        this.returnDate = returnDate;
        this.issueID = issueID;
        this.fineFee = fineFee;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public int getIssueID() {
        return issueID;
    }

    public void setIssueID(int issueID) {
        this.issueID = issueID;
    }

    public double getFineFee() {
        return fineFee;
    }

    public void setFineFee(double fineFee) {
        this.fineFee = fineFee;
    }

    @Override
    public String toString() {
        return "ReturnBookTM{" +
                "returnDate=" + returnDate +
                ", issueID=" + issueID +
                ", fineFee=" + fineFee +
                '}';
    }
}
