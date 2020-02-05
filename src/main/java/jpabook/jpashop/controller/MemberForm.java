package jpabook.jpashop.controller;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * MemberForm
 */

@Getter
@Setter
public class MemberForm {

    @NotEmpty(message = "Not Allowed Empty")
    private String name;

    private String city;
    private String street;
    private String zipcode; 
    
}