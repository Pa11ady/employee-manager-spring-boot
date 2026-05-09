package ru.practicum.employeemanager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    Long id;
    String name;
    String surname;
    String email;
    String password;
    Role role;
}
