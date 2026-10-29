package model;
import java.io.Serializable;
import java.util.Objects;
import model.annotation.MaxLength;
public class Customer implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private static long customerCounter = 100;
    private final String customerId;
    @MaxLength(80) private String name;
    @MaxLength(120) private String email;
    private String mobile;
    private Address address;
    private static synchronized String generateCustomerId() { return "CUST" + (++customerCounter); }
    public Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId(); this.name = name; this.email = email; this.mobile = mobile;
    }
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    @Override public Customer clone() {
        try { return (Customer) super.clone(); }
        catch (CloneNotSupportedException e) { throw new AssertionError(e); }
    }
    @Override public String toString() { return "Customer{" + "customerId='" + customerId + '\'' + ", name='" + name + '\'' + ", email='" + email + '\'' + ", mobile='" + mobile + '\'' + '}'; }
    @Override public boolean equals(Object obj) { if (this == obj) return true; if (!(obj instanceof Customer other)) return false; return customerId.equals(other.customerId); }
    @Override public int hashCode() { return Objects.hash(customerId); }
    public static class Address implements Serializable {
        private static final long serialVersionUID = 1L;
        private String line; private String city; private String pincode;
        public Address(String line, String city, String pincode) { this.line=line; this.city=city; this.pincode=pincode; }
        public String getLine() { return line; } public String getCity() { return city; } public String getPincode() { return pincode; }
        @Override public String toString() { return line + ", " + city + " - " + pincode; }
    }
}
