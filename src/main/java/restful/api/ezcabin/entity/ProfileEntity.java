package restful.api.ezcabin.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
@Builder
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;
    
    private String phone;

    @Column(name = "birthdate")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthDate;

    private String address;
    private String city;

    @Column(name = "postal_code")
    private String postalCode;
    
    private String email;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "id_expired_at")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date idExpiredAt;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(updatable = true, name = "updated_at")
    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
