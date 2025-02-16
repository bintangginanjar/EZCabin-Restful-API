package restful.api.ezcabin.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import restful.api.ezcabin.entity.TaxEntity;
import restful.api.ezcabin.entity.UserEntity;
import restful.api.ezcabin.mapper.ResponseMapper;
import restful.api.ezcabin.model.RegisterTaxRequest;
import restful.api.ezcabin.model.TaxResponse;
import restful.api.ezcabin.model.UpdateTaxRequest;
import restful.api.ezcabin.repository.TaxRepository;
import restful.api.ezcabin.repository.UserRepository;

@Service
public class TaxService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaxRepository taxRepository;

    @Autowired
    private ValidationService validationService;

    public TaxService(UserRepository userRepository, TaxRepository taxRepository, ValidationService validationService) {
        this.userRepository = userRepository;
        this.taxRepository = taxRepository;
        this.validationService = validationService;
    }

    @Transactional
    public TaxResponse create(Authentication authentication, RegisterTaxRequest request) {
        validationService.validate(request);            

        UserEntity user = userRepository.findByUsername(authentication.getName())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));     

        TaxEntity tax = new TaxEntity();
        tax.setName(request.getName());
        tax.setAmount(request.getAmount());
        tax.setUserEntity(user);

        taxRepository.save(tax);

        return ResponseMapper.ToTaxResponseMapper(tax);
    }

    @Transactional(readOnly = true)
    public TaxResponse get(Authentication authentication, String strTaxId) {
        Integer taxId = 0;

        try {
            taxId = Integer.parseInt(strTaxId);       
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        UserEntity user = userRepository.findByUsername(authentication.getName())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        TaxEntity tax = taxRepository.findFirstByUserEntityAndId(user, taxId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax not found"));

        return ResponseMapper.ToTaxResponseMapper(tax);
    }

    @Transactional(readOnly = true)
    public List<TaxResponse> list(Authentication authentication) {
        UserEntity user = userRepository.findByUsername(authentication.getName())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<TaxEntity> taxes = taxRepository.findAllByUserEntity(user);

        return ResponseMapper.ToTaxResponseListMapper(taxes);
    }

    @Transactional
    public TaxResponse update(Authentication authentication, UpdateTaxRequest request, String strTaxId) {
        validationService.validate(request);     

        Integer taxId = 0;

        try {
            taxId = Integer.parseInt(strTaxId);       
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        UserEntity user = userRepository.findByUsername(authentication.getName())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        TaxEntity tax = taxRepository.findFirstByUserEntityAndId(user, taxId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax not found"));
        
        if (Objects.nonNull(request.getName())) {
            tax.setName(request.getName());
        }

        if (Objects.nonNull(request.getAmount())) {
            tax.setAmount(request.getAmount());
        }

        taxRepository.save(tax);

        return ResponseMapper.ToTaxResponseMapper(tax);
    }

    @Transactional
    public void delete(Authentication authentication, String strTaxId) {
        Integer taxId = 0;

        try {
            taxId = Integer.parseInt(strTaxId);       
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }

        UserEntity user = userRepository.findByUsername(authentication.getName())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        TaxEntity tax = taxRepository.findFirstByUserEntityAndId(user, taxId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax not found"));

        try {
            taxRepository.delete(tax);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Delete tax failed");
        }
    }

}
