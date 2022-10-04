package yongs.temp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import yongs.temp.model.Delivery;
import yongs.temp.service.DeliveryService;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryController {
	@Autowired
	DeliveryService deliveryService;

	@GetMapping("/no/{no}")
	public Delivery findByNo(@PathVariable("no") long no) {
		log.debug("DeliveryController.findByNo()");
		return deliveryService.findByNo(no);
	}
	
	@PutMapping("/updateAddress") 
    public void updateAddress(@RequestBody Delivery delivery) {
		log.debug("DeliveryController.updateAddress({})", delivery);
		deliveryService.updateAddress(delivery);
    }
}
