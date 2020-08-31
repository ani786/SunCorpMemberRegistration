package com.au.superchoice.registration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class Member {

        private String FirstName;
        private String lastName;
        private String otherNames;
        private String dateOfBirth;
        private String gender;
        private String address;
        private String emailAddress;
        private String contactNumber;
        private String fundAssignedMemberNumber;
        private String TFN;
        private String payrollNumber;
        private String employmentStatus;
        private String registrationDate;

}
