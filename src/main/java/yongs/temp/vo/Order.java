package yongs.temp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yongs.temp.model.Delivery;

@AllArgsConstructor 
@NoArgsConstructor
@Data
public class Order {
	private String id;
	private long no; // currentTimeMillis();
	private int qty;
	private Product product;
	private User user;
	private Pay pay;
	private Delivery delivery;
}
