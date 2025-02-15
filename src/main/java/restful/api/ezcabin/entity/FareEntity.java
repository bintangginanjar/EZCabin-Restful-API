package restful.api.ezcabin.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "fares")
@Builder
public class FareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @OneToOne(mappedBy = "fareEntity", cascade = CascadeType.ALL)
    private BookingEntity bookingEntity;

    @ManyToOne
    @JoinColumn(name = "tax_id", nullable = false, referencedColumnName = "id")
    private TaxEntity taxEntity;

    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = false, referencedColumnName = "id")
    private DiscountEntity discountEntity;

    @Column(name = "final_amount")
    private Double finalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private UserEntity userEntity;

}
