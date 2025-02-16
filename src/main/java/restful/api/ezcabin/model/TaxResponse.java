package restful.api.ezcabin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxResponse {
    
    private Long id;

    private String name;

    private Double amount;

}
