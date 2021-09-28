package com.example.gccoffeeclone.order.model;

import java.util.Objects;
import java.util.regex.Pattern;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.Assert;

public class Email {

    private final String address;

    public Email(String address) {
        Assert.notNull(address, "Email address should not be null");
        Assert.isTrue(
            address.length() >= 4 && address.length() <= 50,
            "Email address length must be between 4 and 50 characters.");
        Assert.isTrue(
            checkAddress(address),
            "Invalid email address"
        );
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    private static boolean checkAddress(String address) {
        return Pattern.matches("\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b", address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email = (Email) o;
        return address.equals(email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return "Email{" +
            "address='" + address + '\'' +
            '}';
    }
}