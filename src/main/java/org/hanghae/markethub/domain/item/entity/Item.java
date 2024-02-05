package org.hanghae.markethub.domain.item.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae.markethub.domain.purchase.entity.Purchase;

import org.hanghae.markethub.domain.item.dto.ItemUpdateRequestDto;
import org.hanghae.markethub.domain.purchase.entity.Purchase;
import org.hanghae.markethub.domain.picture.entity.Picture;

import org.hanghae.markethub.domain.store.entity.Store;
import org.hanghae.markethub.domain.user.entity.User;
import org.hanghae.markethub.global.constant.Status;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String itemName;

	@Column(nullable = false)
	private int price;

	@Column(nullable = false)
	private int quantity;

	@Column(nullable = false)
	private String itemInfo;

	@Column(nullable = false)
	private String category;

	@Enumerated(value = EnumType.STRING)
	private Status status;

	@ManyToOne
	@JoinColumn(name ="store_id",nullable = false)
	private Store store;

	@OneToOne
	@JoinColumn(name = "purchase_id")
	private Purchase purchase;

	@ManyToOne
	@JoinColumn(name ="user_id",nullable = false)
	private User user;

	@OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
	@Builder.Default
	private List<Picture> pictures = new ArrayList<>();

	public void updateItem(ItemUpdateRequestDto requestDto) {
		this.itemName = requestDto.getItemName();
		this.price = requestDto.getPrice();
		this.quantity = requestDto.getQuantity();
		this.itemInfo = requestDto.getItemInfo();
		this.category = requestDto.getCategory();
	}

	public void deleteItem() {
		this.status = Status.DELETED;
	}
}
