package com.bid.bservice.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bid.bservice.entity.BidRequest;
import com.bid.bservice.service.BidService;

@WebMvcTest(controllers = BidController.class)
public class BidControllerTest {
	
	@MockBean
	private BidService bidService;
	
	@Autowired
	MockMvc mockMvc;
	
    @Test
    public void testGetData() throws Exception {
    	
        String bidRequestJson = "{\"id\":\"1\",\"imp\":[{\"id\":\"1\",\"banner\":{\"w\":300,\"h\":250},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},{\"id\":\"2\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"2\"}}}],\"device\":{\"ua\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Safari/605.1.15\",\"geo\":{\"lat\":75,\"lon\":40,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"ip\":\"200.107.156.44\",\"devicetype\":2,\"os\":\"Mac OS X\"},\"user\":{\"id\":\"1\"},\"at\":0,\"bcat\":[\"IAB7-8\"]}";

        Mockito.when(bidService.addNewBidRequest(Mockito.any(BidRequest.class))).thenReturn(1L);
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
									        		.post("/bid/request")
									                .contentType(MediaType.APPLICATION_JSON)
									                .content(bidRequestJson);
        
        ResultActions resultActions = mockMvc.perform(request);
                
        MvcResult result = resultActions
        		.andExpect(status().isOk())
        		.andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    @Test
    public void testrepsonse() throws Exception {
    	
    	SseEmitter emitter = Mockito.mock(SseEmitter.class);
    	
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        		.get("/bid/response");
		
		ResultActions resultActions = mockMvc.perform(request);
		
		MvcResult result = resultActions
		.andExpect(status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andReturn();	
    }
}
