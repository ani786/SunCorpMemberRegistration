package com.au.superchoice.registration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class TransactionDetails {


    private EmployerForMember employerForMember;


    private FundForMember fundForMember;


    private Member  member;


}
