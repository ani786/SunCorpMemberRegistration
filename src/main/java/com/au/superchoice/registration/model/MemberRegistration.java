package com.au.superchoice.registration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class MemberRegistration {

    private String transactionIdentifier;

    private TransactionDetails transactionDetails;


}
