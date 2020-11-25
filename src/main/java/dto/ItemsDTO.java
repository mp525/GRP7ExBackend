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
public class ItemsDTO {
    private ItemDTO[] items;

    public ItemsDTO(ItemDTO[] items) {
        this.items = items;
    }

    public ItemDTO[] getItems() {
        return items;
    }

    public void setItems(ItemDTO[] items) {
        this.items = items;
    }
    
    
}
