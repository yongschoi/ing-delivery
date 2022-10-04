package yongs.temp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import yongs.temp.dao.DeliveryRepository;
import yongs.temp.dao.TraceRepository;
import yongs.temp.model.Delivery;
import yongs.temp.model.Trace;
import yongs.temp.vo.Order;

@Slf4j
@Service
public class DeliveryService {
	private static final String PAY_DELIVERY_CREATE = "pay-delivery-create";
	private static final String DELIVERY_ORDER_CREATE = "delivery-order-create";
	
	private static final String ORDER_CREATE_ROLLBACK = "order-create-rollback";
	private static final String DELIVERY_CREATE_ROLLBACK = "delivery-create-rollback";
	
	private static final String DELIVERY_UPDATE_NOTICE = "delivery-update-notice";
	
	@Autowired
    DeliveryRepository repo;
	@Autowired
    TraceRepository traceRepo;
	
	@Autowired
    KafkaTemplate<String, String> kafkaTemplate;
	
	@KafkaListener(topics = PAY_DELIVERY_CREATE)
	public void create(String orderStr, Acknowledgment ack) {
		ObjectMapper mapper = new ObjectMapper();
		Order order = null;
		try {
			order = mapper.readValue(orderStr, Order.class);
			long no = System.currentTimeMillis();
			order.getDelivery().setNo(no);
			order.getDelivery().setOrderNo(order.getNo());			 			
			// 택배 API call
			// ...
			// ...

			// db 저장(mongo는 단일 doc에 대해서 TX를 지원하지 않으므로 DB저장은 마지막에 수행하고 kafka pub)
			repo.insert(order.getDelivery());
			
			// Trace 초기 데이터 생성
			Trace trace = new Trace();
			trace.setNo(no);
			// status--> 1: 상품준비중, 2:택배사발송, 3:물류센터, 4:집배센터, 5:배달중, 6:배달완료
			trace.setStatus(1);
			List<String> location = new ArrayList<String>();
			location.add("상품준비중");
			location.add("택배사발송");
			location.add("물류센터");
			location.add("집배센터");
			location.add("배달중");
			location.add("배달완료");
			trace.setLocation(location);			
 			traceRepo.insert(trace);
			
			// to order
			String _tempStr = mapper.writeValueAsString(order);
			kafkaTemplate.send(DELIVERY_ORDER_CREATE, _tempStr);
			log.debug("[DELIVERY Complete] Delivery No[" + order.getDelivery().getNo() + "] / Order No[" + order.getNo() + "]");
		} catch (Exception e) {
			kafkaTemplate.send(DELIVERY_CREATE_ROLLBACK, orderStr);
			log.debug("[DELIVERY Fail] Order No[" + order.getNo() + "]");
		}
		// 성공하든 실패하든 상관없이
		ack.acknowledge();
	}

	@KafkaListener(topics = {ORDER_CREATE_ROLLBACK})
	public void rollback(String orderStr, Acknowledgment ack) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Order order = mapper.readValue(orderStr, Order.class);
			repo.deleteByOrderNo(order.getNo());
			traceRepo.deleteByNo(order.getDelivery().getNo());
			ack.acknowledge();
			log.debug("[DELIVERY Rollback] Pay No[" + order.getDelivery().getNo() + "] / Order No[" + order.getNo() + "]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateAddress(Delivery delivery) {
		Delivery savedDelivery = repo.findByNo(delivery.getNo());
		savedDelivery.setAddress(delivery.getAddress());
		repo.save(savedDelivery);
		// broadcasting
		ObjectMapper mapper = new ObjectMapper();
		try {
			String _tempStr = mapper.writeValueAsString(savedDelivery);
			kafkaTemplate.send(DELIVERY_UPDATE_NOTICE, _tempStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
					
	}
	
	public Delivery findByNo(long no) {
		return repo.findByNo(no);
	}
}
