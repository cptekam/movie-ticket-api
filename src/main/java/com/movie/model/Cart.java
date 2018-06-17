package com.movie.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cart {
    @Id
    private int cartId;
    private int numberOfTickets;
    private int userId;
    private int showId;

    public Cart() {
    }

    public Cart(int cartId, int numberOfTickets, int userId, int showId) {
        this.cartId = cartId;
        this.numberOfTickets = numberOfTickets;
        this.userId = userId;
        this.showId = showId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", numberOfTickets=" + numberOfTickets +
                ", userId=" + userId +
                ", showId=" + showId +
                '}';
    }
}
