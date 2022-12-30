package com.gemini.Contripoint.model;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo {
    private String userId;
    private String email;
    private String name;
    private String token;
}