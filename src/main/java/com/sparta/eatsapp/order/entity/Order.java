package com.sparta.eatsapp.order.entity;

import com.sparta.eatsapp.common.Timestamped;
import com.sparta.eatsapp.menu.entity.Menu;
import com.sparta.eatsapp.order.dto.OrderRequestDto;
import com.sparta.eatsapp.restaurant.entity.Restaurant;
import com.sparta.eatsapp.review.entity.Review;
import com.sparta.eatsapp.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Long orderId;

    @Column(name = "number", nullable = false)
    private int number;

    @Column(name = "customerRequest", nullable = false, length = 50)
    private String customerRequest;

    @Column(name = "deliveryFee", length = 10)
    private int deliveryFee;

    @Column(name = "totalPrice", nullable = false, length = 10)
    private int totalPrice;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "orderStatus", nullable = false, length = 15)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "menuName", nullable = false, length = 20)
    private String menuName;

    @Column(name = "price", nullable = false, length = 15)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;

    @OneToOne
    @JoinColumn(name = "reviewId")
    private Review review;

    public Order(OrderRequestDto requestDto, Restaurant restaurant, User user, Menu menu) {
        this.restaurant = restaurant;
        this.user = user;
        this.menuName = menu.getName();
        this.price = menu.getPrice();
        this.number = requestDto.getNumber();
        this.customerRequest = requestDto.getCustomerRequest();
        this.deliveryFee = 2000;
        this.totalPrice = this.deliveryFee + (int)(this.price * this.number);
        this.orderStatus = OrderStatus.REQUEST;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
    }

    public void nextStatus() {
        if(this.orderStatus == OrderStatus.REQUEST) {
            this.orderStatus = OrderStatus.COOKING;
        } else if(this.orderStatus == OrderStatus.COOKING) {
            this.orderStatus = OrderStatus.DELIVERING;
        } else {
            this.orderStatus = OrderStatus.FINISH;
        }
    }

    public void saveReview(Review review) {
        this.review = review;
    }
}
