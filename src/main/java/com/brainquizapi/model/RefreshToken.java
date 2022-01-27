package com.brainquizapi.model;
import java.time.Instant;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "refreshtoken")
@Getter @Setter
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserMasterEntity user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;


  
}