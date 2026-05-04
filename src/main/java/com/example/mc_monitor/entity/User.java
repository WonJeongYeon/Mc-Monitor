package com.example.mc_monitor.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Table("user")
public class User {
    private int id;
    private String uuid;
    private String userId;
    private String createTime;
}
