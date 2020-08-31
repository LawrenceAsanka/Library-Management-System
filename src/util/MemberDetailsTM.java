package util;

public class MemberDetailsTM {

    private String id;
    private String name;
    private String nic;
    private String address;
    private String contact;

    public MemberDetailsTM() {
    }

    public MemberDetailsTM(String id, String name, String nic, String address, String contact) {
        this.id = id;
        this.name = name;
        this.nic = nic;
        this.address = address;
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "MemberDetailsTM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nic='" + nic + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
