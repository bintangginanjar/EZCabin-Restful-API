package restful.api.ezcabin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import restful.api.ezcabin.model.RegisterTaxRequest;
import restful.api.ezcabin.model.TaxResponse;
import restful.api.ezcabin.model.UpdateTaxRequest;
import restful.api.ezcabin.model.WebResponse;
import restful.api.ezcabin.service.TaxService;

@RestController
@RequestMapping("/api")
public class TaxController {

    @Autowired
    private TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(
        path = "/tax",        
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaxResponse> register(Authentication authentication, 
                                            @RequestBody RegisterTaxRequest request) {

        TaxResponse response = taxService.create(authentication, request);

        return WebResponse.<TaxResponse>builder()
                                        .status(true)
                                        .messages("Tax registration success")
                                        .data(response)
                                        .build();      
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(
        path = "/tax/{taxId}",    
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaxResponse> get(Authentication authentication, 
                                        @PathVariable("taxId") String taxId) {

        TaxResponse response = taxService.get(authentication, taxId);

        return WebResponse.<TaxResponse>builder()
                                        .status(true)
                                        .messages("Tax fetching success")
                                        .data(response)
                                        .build();      
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(
        path = "/tax/list",    
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<TaxResponse>> list(Authentication authentication) {

        List<TaxResponse> response = taxService.list(authentication);

        return WebResponse.<List<TaxResponse>>builder()
                                        .status(true)
                                        .messages("Tax list fetching success")
                                        .data(response)
                                        .build();      
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(
        path = "/tax/{taxId}",        
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaxResponse> update(Authentication authentication,
                                    UpdateTaxRequest request, 
                                    @PathVariable("taxId") String taxId) {

        request.setId(taxId);

        TaxResponse response = taxService.update(authentication, request, taxId);

        return WebResponse.<TaxResponse>builder()
                                        .status(true)
                                        .messages("Tax update success")
                                        .data(response)
                                        .build();      
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(
        path = "/tax/{taxId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaxResponse> delete(Authentication authentication, 
                                        @PathVariable("taxId") String taxId) {

        taxService.delete(authentication, taxId);

        return WebResponse.<TaxResponse>builder()
                                        .status(true)
                                        .messages("Tax fetching success")                                        
                                        .build();      
    }

}
