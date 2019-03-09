package ch.start.hack.web.rest;

import ch.start.hack.config.Constants;
import ch.start.hack.domain.Cup;
import ch.start.hack.domain.User;
import ch.start.hack.domain.enumeration.CupStatus;
import ch.start.hack.domain.pojo.CupRequest;
import ch.start.hack.repository.CupRepository;
import ch.start.hack.repository.UserRepository;
import ch.start.hack.security.AuthoritiesConstants;
import ch.start.hack.service.CupService;
import ch.start.hack.service.MailService;
import ch.start.hack.service.UserService;
import ch.start.hack.service.dto.CupDTO;
import ch.start.hack.service.dto.UserDTO;
import ch.start.hack.service.mapper.CupMapper;
import ch.start.hack.service.mapper.UserMapper;
import ch.start.hack.web.rest.errors.BadRequestAlertException;
import ch.start.hack.web.rest.errors.EmailAlreadyUsedException;
import ch.start.hack.web.rest.errors.LoginAlreadyUsedException;
import ch.start.hack.web.rest.util.HeaderUtil;
import ch.start.hack.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    private final CupResource cupResource;

    private final CupRepository cupRepository;

    private final UserRepository userRepository;

    private final MailService mailService;

    private final CupMapper cupMapper;

    private final UserMapper userMapper;

    public UserResource(UserService userService, CupResource cupResource, CupRepository cupRepository, UserRepository userRepository, MailService mailService, CupMapper cupMapper, UserMapper userMapper) {
        this.userService = userService;
        this.cupResource = cupResource;
        this.cupRepository = cupRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.cupMapper = cupMapper;
        this.userMapper = userMapper;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException       if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/users/buy-cup")
    @Timed
    public ResponseEntity<Cup> buyCap(@Valid @RequestBody CupRequest cupRequest) throws URISyntaxException {
        log.debug("REST request to buy Cap : {}", cupRequest);

        Optional<User> user = userRepository.findOneByLogin(cupRequest.getUserLogin());

        if (user.isPresent()) {

            Optional<Cup> cup = cupRepository.findCupByQrCode(cupRequest.getCupHash());

            if (!cup.isPresent()) {
                Cup newCup = new Cup();
                newCup.setQrCode(cupRequest.getCupHash());
                newCup.setUserCup(user.get());
                newCup.setStatus(CupStatus.InUse);
                CupDTO cupDto = cupResource.createCup(cupMapper.toDto(newCup)).getBody();
//                user.get().getCups().add(cupMapper.toEntity(cupDto));
//                updateUser(userMapper.userToUserDTO(user.get()));

                return ResponseEntity.created(new URI("/api/users/buy-cup:" + cupDto.getQrCode()))
                    .body(cupMapper.toEntity(cupDto));

            } else {
                cup.get().setStatus(CupStatus.InUse);
                CupDTO cupDto = cupResource.updateCup(cupMapper.toDto(cup.get())).getBody();
//                user.get().getCups().add(cupMapper.toEntity(cupDto));
//                updateUser(userMapper.userToUserDTO(user.get()));

                return ResponseEntity.created(new URI("/api/users/buy-cup:" + cupDto.getQrCode()))
                    .body(cupMapper.toEntity(cupDto));
            }
        } else {
            log.debug("Cannot find user with login: " + cupRequest.getUserLogin());

            return ResponseEntity.notFound()
                .build();
        }
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException       if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     */
    @PostMapping("/users")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert("userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/users/return-cup")
    @Timed
    public ResponseEntity<Cup> returnCap(@Valid @RequestBody CupRequest cupRequest) throws URISyntaxException {
        log.debug("REST request to return Cap : {}", cupRequest);

        Optional<Cup> cup = cupRepository.findCupByQrCode(cupRequest.getCupHash());

        if (cup.isPresent()) {
            cup.get().setStatus(CupStatus.Recycled);
            CupDTO returnedCup = cupResource.updateCup(cupMapper.toDto(cup.get())).getBody();

            return ResponseEntity.created(new URI("/api/users/return-cap" + cupMapper.toEntity(returnedCup).getQrCode()))
                .body(cupMapper.toEntity(returnedCup));
        } else {
            log.debug("Cannot find cup with hash: " + cupRequest.getCupHash());

            return ResponseEntity.notFound()
                .build();
        }
    }

    /**
     * PUT /users : Updates an existing User.
     *
     * @param userDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user
     * @throws EmailAlreadyUsedException 400 (Bad Request) if the email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already in use
     */
    @PutMapping("/users")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert("userManagement.updated", userDTO.getLogin()));
    }

    /**
     * GET /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/users/authorities")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * GET /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login)).build();
    }
}
