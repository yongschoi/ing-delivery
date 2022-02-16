package yongs.temp.model;

import java.util.List;

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
@Document(collection = "trace")
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Trace {
	@Id
	private String id;
	private long no;
	private int status;
	private List<String> location;
}