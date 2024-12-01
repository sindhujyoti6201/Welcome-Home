package edu.nyu.welcomehome.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardAccessDTO {
    private String username;
    private boolean hasDeliveryAccess;
    private boolean hasDonationAccess;
    private boolean hasSupervisorAccess;
    private boolean hasManagerAccess;

    public DashboardAccessDTO(String username, List<String> roles) {
        this.username = username;
        this.hasDeliveryAccess = roles.contains("delivery") ;
        this.hasDonationAccess = roles.contains("donor") ;
        this.hasSupervisorAccess = roles.contains("supervisor") ;
        this.hasManagerAccess = roles.contains("manager");
    }
}