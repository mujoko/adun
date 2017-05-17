package com.asli.adun.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Allan G. Ramirez (ramirezag@gmail.com)
 */
@RestController
@RequestMapping("/users")
public class UserController {
//    private UserProfileService service;
//
//    @Autowired
//    public UserController(UserProfileService userProfileService) {
//        this.service = userProfileService;
//    }
//
    @PostMapping("/nearby")
    public List<UserLocationDto> getNearbyUsers(@RequestBody @Valid WithinRadiusDto searchDto) {
//        return service.getNearbyUsers(searchDto);
    	return null;
    }
}
