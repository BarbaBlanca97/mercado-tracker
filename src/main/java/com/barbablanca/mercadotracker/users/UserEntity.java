package com.barbablanca.mercadotracker.users;

import com.barbablanca.mercadotracker.products.ProductEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @NotNull
    String name;
    @NotNull
    String email;
    @NotNull
    String password;
    boolean verified;

    @ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinTable(
            name = "user_product",
            joinColumns = @JoinColumn( name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name = "product_id", referencedColumnName = "id"))
    private Set<ProductEntity> products = new HashSet<>();

    protected UserEntity() {}

    UserEntity(String name, String email, String password, boolean verified) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.verified = verified;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() { return email; }

    public String getPassword() {
        return password;
    }

    public boolean getVerified() { return verified; }

    public Set<ProductEntity> getProducts() { return products; }

    public void addProduct(ProductEntity product) {
        this.products.add(product);
        product.getSubscribers().add(this);
    }

    public void deleteProduct(ProductEntity product) {
        this.products.remove(product);
        product.getSubscribers().remove(this);
    }

    public void setPassword(String password) { this.password = password; }

    public void setVerified(boolean verified) { this.verified = verified; }
}
