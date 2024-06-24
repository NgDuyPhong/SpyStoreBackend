package com.apa.amazonsearch.items.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.apa.users.models.User;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "spy_store")
public class SpyStore {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String url;
	private int currentPage;
	private int maxPage;
	
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
}
