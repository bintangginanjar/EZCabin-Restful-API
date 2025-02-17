package restful.api.ezcabin.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import restful.api.ezcabin.entity.RoleEntity;
import restful.api.ezcabin.entity.TaxEntity;
import restful.api.ezcabin.entity.UserEntity;
import restful.api.ezcabin.model.RegisterTaxRequest;
import restful.api.ezcabin.model.TaxResponse;
import restful.api.ezcabin.model.UpdateTaxRequest;
import restful.api.ezcabin.model.WebResponse;
import restful.api.ezcabin.repository.RoleRepository;
import restful.api.ezcabin.repository.TaxRepository;
import restful.api.ezcabin.repository.UserRepository;
import restful.api.ezcabin.security.JwtUtil;
import restful.api.ezcabin.security.SecurityConstants;

@EnableWebMvc
@SpringBootTest
@AutoConfigureMockMvc
public class TaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TaxRepository taxRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private final String username = "test";
    private final String password = "rahasia";

    private final String taxName = "PPN 10%";
    private final Double taxAmount = 10.00;

    @BeforeEach
    void setUp() {                

        taxRepository.deleteAll();
        userRepository.deleteAll();

        RoleEntity role = roleRepository.findByName("ROLE_ADMIN").orElse(null);

        UserEntity user = new UserEntity();
        user.setUsername(username);        
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Collections.singletonList(role)); 
        user.setIsActive(true);
        userRepository.save(user);
    }

    @Test
    void testRegisterTaxSuccess() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        RegisterTaxRequest request = new RegisterTaxRequest();
        request.setName(taxName);
        request.setAmount(taxAmount);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                post("/api/tax")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
        });
    }    

    @Test
    void testRegisterTaxUserInactive() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        user.setIsActive(false);
        userRepository.save(user);

        RegisterTaxRequest request = new RegisterTaxRequest();
        request.setName(taxName);
        request.setAmount(taxAmount);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                post("/api/tax")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testRegisterTaxBlank() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        RegisterTaxRequest request = new RegisterTaxRequest();
        request.setName("");
        request.setAmount(null);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                post("/api/tax")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testRegisterTaxInvalidToken() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        RegisterTaxRequest request = new RegisterTaxRequest();
        request.setName("");
        request.setAmount(null);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken + "a";

        mockMvc.perform(
                post("/api/tax")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testRegisterTaxNoToken() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        RegisterTaxRequest request = new RegisterTaxRequest();
        request.setName("");
        request.setAmount(null);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        mockMvc.perform(
                post("/api/tax")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))                                             
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testRegisterTaxTokenExpired() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        RegisterTaxRequest request = new RegisterTaxRequest();
        request.setName("");
        request.setAmount(null);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() - SecurityConstants.JWTexpiration);
        userRepository.save(user);

        mockMvc.perform(
                post("/api/tax")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))                                             
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testGetTaxSuccess() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                get("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
        });
    }

    @Test
    void testGetTaxUserInactive() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        user.setIsActive(false);
        userRepository.save(user);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                get("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testGetTaxBadId() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                get("/api/tax/" + tax.getId() + "a")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testGetTaxNotFound() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                get("/api/tax/111")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testGetTaxInvalidToken() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken + "a";

        mockMvc.perform(
                get("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testGetTaxTokenExpired() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() - SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                get("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testGetTaxNoToken() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/tax/" + tax.getId() + "a")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                                                                  
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testGetListTaxSuccess() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                get("/api/tax/list")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<List<TaxResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
        });
    }

    @Test
    void testGetListTaxInactiveUser() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        user.setIsActive(false);
        userRepository.save(user);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                get("/api/tax/list")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<List<TaxResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testGetListTaxInvalidToken() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken + "a";

        mockMvc.perform(
                get("/api/tax/list")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<List<TaxResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testGetListTaxTokenExpired() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() - SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                get("/api/tax/list")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
                WebResponse<List<TaxResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testGetListTaxNoToken() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        mockMvc.perform(
                get("/api/tax/list")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                                                                  
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
                WebResponse<List<TaxResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testUpdateTaxSuccess() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        UpdateTaxRequest request = new UpdateTaxRequest();
        request.setName(taxName + " updated");
        request.setAmount(taxAmount - 2);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                patch("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
        });
    }

    @Test
    void testUpdateTaxInactiveUser() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        user.setIsActive(false);
        userRepository.save(user);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        UpdateTaxRequest request = new UpdateTaxRequest();
        request.setName(taxName + " updated");
        request.setAmount(taxAmount - 2);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                patch("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testUpdateTaxInvalidToken() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        user.setIsActive(false);
        userRepository.save(user);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        UpdateTaxRequest request = new UpdateTaxRequest();
        request.setName(taxName + " updated");
        request.setAmount(taxAmount - 2);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken + "a";

        mockMvc.perform(
                patch("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testUpdateTaxNotFound() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        user.setIsActive(false);
        userRepository.save(user);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        UpdateTaxRequest request = new UpdateTaxRequest();
        request.setName(taxName + " updated");
        request.setAmount(taxAmount - 2);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                patch("/api/tax/999")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testUpdateTaxBadId() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        user.setIsActive(false);
        userRepository.save(user);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        UpdateTaxRequest request = new UpdateTaxRequest();
        request.setName(taxName + " updated");
        request.setAmount(taxAmount - 2);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                patch("/api/tax/" + tax.getId() + "a")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testUpdateTaxTokenExpired() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        user.setIsActive(false);
        userRepository.save(user);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        UpdateTaxRequest request = new UpdateTaxRequest();
        request.setName(taxName + " updated");
        request.setAmount(taxAmount - 2);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() - SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                patch("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
                WebResponse<TaxResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testDeleteTaxSuccess() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                delete("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(true, response.getStatus());
        });
    }

    @Test
    void testDeleteTaxInactiveUser() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        user.setIsActive(false);
        userRepository.save(user);

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                delete("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testDeleteTaxInvalidToken() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);        

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken + "a";

        mockMvc.perform(
                delete("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testDeleteTaxTokenExpired() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);        

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() - SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                delete("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testDeleteTaxNoToken() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);        

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        mockMvc.perform(
                delete("/api/tax/" + tax.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                                                                     
        ).andExpectAll(
                status().isForbidden()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testDeleteTaxIdNotFound() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);        

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                delete("/api/tax/999")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }

    @Test
    void testDeleteTaxBadId() throws Exception {
        UserEntity user = userRepository.findByUsername(username).orElse(null);        

        TaxEntity tax = new TaxEntity();
        tax.setName(taxName);
        tax.setAmount(taxAmount);
        tax.setUserEntity(user);
        taxRepository.save(tax);

        Authentication authentication = authenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                username, password)
                                            );

        String mockToken = jwtUtil.generateToken(authentication);

        user.setToken(mockToken);
        user.setTokenExpiredAt(System.currentTimeMillis() + SecurityConstants.JWTexpiration);
        userRepository.save(user);

        String mockBearerToken = "Bearer " + mockToken;

        mockMvc.perform(
                delete("/api/tax/" +  tax.getId() + "a")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)                        
                        .header("Authorization", mockBearerToken)                        
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
                WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals(false, response.getStatus());
        });
    }
}
