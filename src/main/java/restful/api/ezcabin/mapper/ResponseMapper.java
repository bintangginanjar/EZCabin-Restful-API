package restful.api.ezcabin.mapper;

import java.util.List;
import java.util.stream.Collectors;

import restful.api.ezcabin.entity.TaxEntity;
import restful.api.ezcabin.entity.UserEntity;
import restful.api.ezcabin.model.TaxResponse;
import restful.api.ezcabin.model.UserResponse;

public class ResponseMapper {

    public static UserResponse ToUserResponseMapper(UserEntity user) {        
        List<String> roles = user.getRoles().stream().map(p -> p.getName()).toList();

        return UserResponse.builder()                
                .username(user.getUsername())
                .role(roles)
                .build();
    }

    public static TaxResponse ToTaxResponseMapper(TaxEntity tax) {
        return TaxResponse.builder()
                .id(tax.getId())
                .name(tax.getName())
                .amount(tax.getAmount())
                .build();
    }

    public static List<TaxResponse> ToTaxResponseListMapper(List<TaxEntity> taxes) {
        return taxes.stream()
                    .map(
                        p -> new TaxResponse(
                            p.getId(),
                            p.getName(),
                            p.getAmount()
                    )).collect(Collectors.toList());
    }

}
