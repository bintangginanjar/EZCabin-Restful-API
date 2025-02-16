package restful.api.ezcabin.mapper;

import java.util.List;
import java.util.stream.Collectors;

import restful.api.ezcabin.entity.TaxEntity;
import restful.api.ezcabin.model.TaxResponse;

public class ResponseMapper {

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
