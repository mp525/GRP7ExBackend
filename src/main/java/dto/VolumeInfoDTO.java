/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author Mathias
 */
public class VolumeInfoDTO {
    private IdentityIsbmDTO[] industryIdentifiers;

    public VolumeInfoDTO(IdentityIsbmDTO[] industryIdentifiers) {
        this.industryIdentifiers = industryIdentifiers;
    }

    public IdentityIsbmDTO[] getIndustryIdentifiers() {
        return industryIdentifiers;
    }

    public void setIndustryIdentifiers(IdentityIsbmDTO[] industryIdentifiers) {
        this.industryIdentifiers = industryIdentifiers;
    }
    
    
    
}
