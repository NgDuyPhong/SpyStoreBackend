package com.apa.users.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "apa_user")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    String username;

    String password;

    public boolean isAdmin() {
    	if (this.username == null) {
    		return false;
    	}
        return this.username.equalsIgnoreCase("admin");
    }
}
