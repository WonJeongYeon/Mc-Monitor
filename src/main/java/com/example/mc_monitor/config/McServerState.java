package com.example.mc_monitor.config;

import com.example.mc_monitor.model.McServerStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Setter
@Component
public class McServerState {


    private McServerStatus status;

    @Bean
    public McServerStatus status() {
        return status;
    }

    public void setState(McServerStatus newStatus) {
        status = newStatus;
    }

    public void initState() {
        status = new McServerStatus();
    }

}
