package com.sendika.bookstore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    private String username;

    // getters/setters
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public String getAuthority(){return authority;}
    public void setAuthority(String authority){this.authority=authority;}
    public String getUsername(){return username;}
    public void setUsername(String username){this.username=username;}
}
