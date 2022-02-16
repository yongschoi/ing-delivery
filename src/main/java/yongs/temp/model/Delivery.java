package yongs.temp.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor 
@NoArgsConstructor
@Data
@Document(collection = "delivery")
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Delivery {
	@Id
	private String id;
	private long no;
	private String address;
	private long orderNo;
}
