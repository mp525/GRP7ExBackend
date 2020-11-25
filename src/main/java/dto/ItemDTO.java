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
public class ItemDTO {
    private VolumeInfoDTO volumeInfo;

    public ItemDTO(VolumeInfoDTO volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public VolumeInfoDTO getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfoDTO volumeInfo) {
        this.volumeInfo = volumeInfo;
    }
    
    
    
}
